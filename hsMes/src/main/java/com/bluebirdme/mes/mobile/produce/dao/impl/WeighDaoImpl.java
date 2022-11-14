/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2017版权所有
 */
package com.bluebirdme.mes.mobile.produce.dao.impl;

import com.bluebirdme.mes.core.base.dao.BaseDaoImpl;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.mobile.produce.dao.IWeighDao;
import com.bluebirdme.mes.planner.produce.entity.ProducePlan;
import com.bluebirdme.mes.planner.weave.entity.WeavePlan;
import com.bluebirdme.mes.platform.entity.Department;
import com.bluebirdme.mes.platform.entity.User;
import com.bluebirdme.mes.produce.entity.FinishedProduct;
import com.bluebirdme.mes.produce.entity.FinishedProductMirror;
import com.bluebirdme.mes.sales.entity.Consumer;
import com.bluebirdme.mes.sales.entity.SalesOrder;
import com.bluebirdme.mes.sales.entity.SalesOrderDetail;
import com.bluebirdme.mes.statistics.entity.TotalStatistics;
import com.bluebirdme.mes.store.entity.Roll;
import com.bluebirdme.mes.store.entity.RollBarcode;
import com.bluebirdme.mes.utils.DateUtils;
import com.bluebirdme.mes.utils.DateUtils.DateField;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Goofy
 * @Date 2017年11月22日 下午1:36:50
 */
@Repository
public class WeighDaoImpl extends BaseDaoImpl implements IWeighDao {
    @Override
    public boolean shouldWeigh(String barcode, String deviceCode) throws ParseException {
        RollBarcode rbc = findOne(RollBarcode.class, "barcode", barcode);
        int index = getDeviceRollIndex(rbc.getPlanId(), deviceCode);
        int rule = getRollWeighRule(barcode);
        boolean isGroupFirstRoll = isGroupFirstRoll(rbc.getPlanId(), deviceCode);
        // 规则不称
        if (rule == 2)
            return false;
        // 除了不称的，首卷必称
        if (index == 1)
            return true;
        // 全称||班组更换||顺序号逢1必称||抽称的第二卷
        return rule == 0 || isGroupFirstRoll || (index % 10 == 1) || (index == 2 && rule == 1);
    }

    @Override
    public double getAvg(Long planId, String deviceCode) {
        String sql = "SELECT CAST(AVG(rollAutoWeight) as DECIMAL(9,1)) from hs_roll_view where planId=" + planId + " and rollDeviceCode='" + deviceCode + "' and rollQualityGradeCode='A' and isAbnormalRoll=0";
        String avg = null;
        if (null != getSession().createSQLQuery(sql).uniqueResult()) {
            avg = String.valueOf(getSession().createSQLQuery(sql).uniqueResult());
        }
        return avg == null ? 0D : new Double(avg.replaceAll(",", ""));
    }


    @Override
    public boolean isGroupFirstRoll(Long planId, String deviceCode) throws ParseException {
        Date date = new Date();
        //当前时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = "yyyy-MM-dd";
        String today = DateUtils.now(format);
        int hour = DateUtils.fieldValue(date, DateField.HOUR);
        long now = System.currentTimeMillis();
        long morningStart = sdf.parse(today + " 08:00:00").getTime();
        long morningEnd = sdf.parse(today + " 20:00:00").getTime();
        String sql;
        //当前是早班
        if (now >= morningStart && now < morningEnd) {
            sql = "SELECT count(*) FROM hs_roll_view WHERE planId = " + planId + " AND rollDeviceCode = '" + deviceCode + "' AND rollOutPutTime >= '" + sdf.format(morningStart) + "' AND rollOutPutTime < '" + sdf.format(morningEnd) + "' AND isAbnormalRoll = 0 AND rollAutoWeight is not null AND rollQualityGradeCode='A'";
        } else {
            long nightEnd = 0;
            long nightStart = 0;
            //当天8点前，那么是前一天的晚班
            if (hour < 8) {
                nightEnd = morningStart;
                nightStart = morningStart - 12 * 60 * 60 * 1000;
            } else {
                //当天晚班首卷
                nightStart = morningEnd;
                nightEnd = morningEnd + 12 * 60 * 60 * 1000;
            }
            sql = "SELECT count(*) FROM hs_roll_view WHERE planId = " + planId + " AND rollDeviceCode = '" + deviceCode + "' AND rollOutPutTime >= '" + sdf.format(nightStart) + "' AND rollOutPutTime < '" + sdf.format(nightEnd) + "' AND isAbnormalRoll = 0 AND rollAutoWeight is not null AND rollQualityGradeCode='A'";
        }
        int sn = ((BigInteger) getSession().createSQLQuery(sql).uniqueResult()).intValue();
        return sn == 0;
    }

    @Override
    public boolean isNormalWeight(String barcode) {
        String sql = "SELECT count(1) from hs_roll_view where rollBarcode='" + barcode + "' and rollAutoWeight<=maxWeight and rollAutoWeight>=minWeight";
        int sn = ((BigInteger) getSession().createSQLQuery(sql).uniqueResult()).intValue();
        return sn == 0;
    }

    @Override
    public int getDeviceRollIndex(Long planId, String deviceCode) {
        String sql = "SELECT count(*) from hs_roll_view where planId=" + planId + " and rollDeviceCode='" + deviceCode + "' and rollQualityGradeCode='A' and isAbnormalRoll=0";
        int sn = ((BigInteger) getSession().createSQLQuery(sql).uniqueResult()).intValue();
        return sn + 1;
    }

    /**
     * 获取编织任务的卷序号
     *
     * @param planId
     * @return
     */
    public int getWeaveRollIndex(Long planId) {
        String sql = "SELECT count(*) from hs_roll_view where planId=" + planId + " and isAbnormalRoll=0";
        int sn = ((BigInteger) getSession().createSQLQuery(sql).uniqueResult()).intValue();
        return sn + 1;
    }

    @Override
    public Double getRollTheoryWeight(String barcode) {
        HashMap<String, Object> map = new HashMap<>();
        String sql = null;
        RollBarcode rbc = findOne(RollBarcode.class, "barcode", barcode);
        SalesOrderDetail salesOrderDetail = findById(SalesOrderDetail.class, rbc.getSalesOrderDetailId());
        map.put("productId", rbc.getSalesProductId());
        map.put("salesOrderDetailId", rbc.getSalesOrderDetailId());
        List<FinishedProductMirror> list = findListByMap(FinishedProductMirror.class, map);
        if (list.size() == 0) {
            map.clear();
            map.put("productId", rbc.getSalesProductId());
            map.put("salesOrderId", salesOrderDetail.getSalesOrderId());
            list = findListByMap(FinishedProductMirror.class, map);
        }
        if (list.size() > 0) {
            sql = "SELECT cast(CASE WHEN x is null then y when x=0 then y else x end as DECIMAL(9,1)) weight from ( SELECT (SELECT productRollWeight from hs_finishproduct_mirror where id=" + list.get(0).getId()
                    + ") x , (SELECT sum(bom.ftcBomDetailWeightPerSquareMetre)*fp.productRollLength*fp.productWidth/1000000 from hs_finishproduct_mirror fp LEFT JOIN hs_ftc_proc_bom_detail_mirror bom on bom.ftcBomVersionId=fp.procBomId where fp.id=" + list.get(0).getId() + ") y ) z";
        } else {
            FinishedProduct fp = findOne(FinishedProduct.class, "id", rbc.getSalesProductId());
            sql = "SELECT cast(CASE WHEN x is null then y when x=0 then y else x end as DECIMAL(9,1)) weight from ( SELECT (SELECT productRollWeight from hs_finishproduct where id=" + fp.getId()
                    + ") x , (SELECT sum(bom.ftcBomDetailWeightPerSquareMetre)*fp.productRollLength*fp.productWidth/1000000 from hs_finishproduct fp LEFT JOIN hs_ftc_proc_bom_detail bom on bom.ftcBomVersionId=fp.procBomId where fp.id=" + fp.getId() + ") y ) z";
        }
        String weight = null;
        if (null != getSession().createSQLQuery(sql).uniqueResult()) {
            weight = String.valueOf(getSession().createSQLQuery(sql).uniqueResult());
        } else {
            weight = "0.0";
        }
        return new Double(weight.replace(",", "").trim());
    }

    @Override
    public int getRollWeighRule(String barcode) {
        HashMap<String, Object> map = new HashMap<>();
        RollBarcode rbc = findOne(RollBarcode.class, "barcode", barcode);
        SalesOrderDetail salesOrderDetail = findById(SalesOrderDetail.class, rbc.getSalesOrderDetailId());
        map.put("productId", rbc.getSalesProductId());
        map.put("salesOrderDetailId", rbc.getSalesOrderDetailId());
        List<FinishedProductMirror> list = findListByMap(FinishedProductMirror.class, map);
        if (list.size() == 0) {
            map.clear();
            map.put("productId", rbc.getSalesProductId());
            map.put("salesOrderId", salesOrderDetail.getSalesOrderId());
            list = findListByMap(FinishedProductMirror.class, map);
        }
        if (list.size() > 0) {
            return list.get(0).getProductWeigh();
        } else {
            FinishedProduct fp = findOne(FinishedProduct.class, "id", rbc.getSalesProductId());
            return fp.getProductWeigh();
        }
    }

    @Override
    public String hasWaitForWeighRoll(Long planId, String deviceCode) {
        String sql = "SELECT GROUP_CONCAT(rollBarcode) codes from hs_roll_view where planId=" + planId + " and rollDeviceCode='" + deviceCode + "' and rollWeighState=1 and rollAutoWeight is null ";
        return (String) getSession().createSQLQuery(sql).uniqueResult();
    }

    /**
     * 保存称重的重量
     *
     * @param barcode
     * @param weight
     * @param qualityGrade
     * @throws Exception
     */
    public void saveWeight(String barcode, Double weight, String qualityGrade) throws Exception {
        Roll r = findOne(Roll.class, "rollBarcode", barcode);
        if (r == null) {
            throw new Exception("条码尚未登记");
        }
        if (r.getRollWeight() != null) {
            throw new Exception("该条码已经有重量了,重量为: " + r.getRollWeight() + "，不允许重复称重");
        }

        RollBarcode rbc = findOne(RollBarcode.class, "barcode", barcode);
        if (rbc == null) {
            throw new Exception("条码不存在");
        }

        SalesOrderDetail salesOrderDetail = findById(SalesOrderDetail.class, rbc.getSalesOrderDetailId());
        if (null != salesOrderDetail.getMirrorProductId()) {
            FinishedProductMirror product = findById(FinishedProductMirror.class, salesOrderDetail.getMirrorProductId());
            // 称重规则
            int rule = product.getProductWeigh();
            r.setRollQualityGradeCode(qualityGrade);
            if (!qualityGrade.equals("A")) {
                r.setIsAbnormalRoll(1);
            }
            if (product.getMinWeight() == null || product.getMaxWeight() == null) {
                throw new Exception("产品重量的上下偏差未设定");
            }
            //0:全称 1:抽称 2:不称 3:首卷必称
            switch (rule) {
                case 0:
                case 1://抽称规则,全称规则 取重量一致
                    r.setRollAutoWeight(weight);
                    //定长
                    if (product.getProductRollWeight() == null) {
                        r.setRollWeight(weight);
                    } else {
                        //定重产品
                        if (weight >= product.getMinWeight() && weight <= product.getMaxWeight()) {
                            r.setRollWeight(getRollTheoryWeight(barcode));//取理论重量
                        } else {//超出偏差取实际值
                            r.setRollWeight(weight);
                            r.setIsAbnormalRoll(1);//后续不计入平均值
                        }
                    }
                    break;
                case 2://不称
                    throw new Exception("该卷无需称重");
                case 3:
                    //定长
                    if (product.getProductRollWeight() == null) {
                        r.setRollWeight(weight);
                        r.setRollAutoWeight(weight);
                    } else {
                        //定重产品
                        if (weight >= product.getMinWeight() && weight <= product.getMaxWeight()) {
                            r.setRollWeight(getRollTheoryWeight(barcode));//取理论重量
                        } else {//超出偏差取实际值
                            r.setRollWeight(weight);
                            r.setIsAbnormalRoll(1);//后续不计入平均值
                        }
                        r.setRollAutoWeight(weight);
                    }
                    break;
                default:
                    break;
            }
            update(r);//更新卷信息
            //保存生产统计
            TotalStatistics ts = findOne(TotalStatistics.class, "rollBarcode", barcode);
            if (ts == null) {
                ts = new TotalStatistics();
                ts.setBarcodeType("roll");
                ts.setBatchCode(rbc.getBatchCode());
                SalesOrderDetail sod = findById(SalesOrderDetail.class, rbc.getSalesOrderDetailId());
                SalesOrder so = findById(SalesOrder.class, sod.getSalesOrderId());
                Consumer c = findById(Consumer.class, so.getSalesOrderConsumerId());
                WeavePlan wp = findById(WeavePlan.class, rbc.getPlanId());
                ProducePlan pp = findOne(ProducePlan.class, "producePlanCode", wp.getPlanCode());
                ts.setCONSUMERNAME(c.getConsumerSimpleName());
                ts.setDeviceCode(r.getRollDeviceCode());
                ts.setIsPacked(0);
                User user = findById(User.class, r.getRollUserId());
                ts.setLoginName(user.getUserName());
                Department d = findById(Department.class, user.getDid());
                ts.setName(d.getName());
                ts.setWorkShopCode(d.getCode());
                ts.setProducePlanCode(pp.getProducePlanCode());
                ts.setProductLength(product.getProductRollLength());
                ts.setProductModel(product.getProductModel());
                ts.setProductName(product.getFactoryProductName());
                ts.setProductWeight(r.getRollAutoWeight());
                ts.setProductWidth(product.getProductWidth());
                ts.setRollBarcode(barcode);
                ts.setRollOutputTime(r.getRollOutputTime());
                ts.setRollQualityGradeCode(r.getRollQualityGradeCode());
                ts.setRollWeight(r.getRollWeight());
                ts.setSalesOrderCode(sod.getSalesOrderSubCode());
                ts.setState(0);
                ts.setRollQualityGradeCode(qualityGrade);
                ts.setIsLocked(-1);
                save(ts);
            } else {
                ts.setProductWeight(r.getRollAutoWeight());
                ts.setRollWeight(r.getRollWeight());
                ts.setRollQualityGradeCode(qualityGrade);
                update(ts);
            }
        } else {
            //兼容
            FinishedProduct product = findById(FinishedProduct.class, rbc.getSalesProductId());
            // 称重规则
            int rule = product.getProductWeigh();
            r.setRollQualityGradeCode(qualityGrade);
            if (!qualityGrade.equals("A")) {
                r.setIsAbnormalRoll(1);
            }
            if (product.getMinWeight() == null || product.getMaxWeight() == null) {
                throw new Exception("产品重量的上下偏差未设定");
            }
            //0:全称 1:抽称 2:不称 3:首卷必称
            switch (rule) {
                case 0:
                case 1://抽称规则,全称规则 取重量一致
                    r.setRollAutoWeight(weight);
                    //定长
                    if (product.getProductRollWeight() == null) {
                        r.setRollWeight(weight);
                    } else {
                        //定重产品
                        if (weight >= product.getMinWeight() && weight <= product.getMaxWeight()) {
                            r.setRollWeight(getRollTheoryWeight(barcode));//取理论重量
                        } else {//超出偏差取实际值
                            r.setRollWeight(weight);
                            r.setIsAbnormalRoll(1);//后续不计入平均值
                        }
                    }
                    break;
                case 2://不称
                    throw new Exception("该卷无需称重");
                case 3:
                    //定长
                    if (product.getProductRollWeight() == null) {
                        r.setRollWeight(weight);
                        r.setRollAutoWeight(weight);
                    } else {
                        //定重产品
                        if (weight >= product.getMinWeight() && weight <= product.getMaxWeight()) {
                            r.setRollWeight(getRollTheoryWeight(barcode));//取理论重量
                        } else {//超出偏差取实际值
                            r.setRollWeight(weight);
                            r.setIsAbnormalRoll(1);//后续不计入平均值
                        }
                        r.setRollAutoWeight(weight);
                    }
                    break;
                default:
                    break;
            }
            update(r);//更新卷信息
            //保存生产统计
            TotalStatistics ts = findOne(TotalStatistics.class, "rollBarcode", barcode);
            if (ts == null) {
                ts = new TotalStatistics();
                ts.setBarcodeType("roll");
                ts.setBatchCode(rbc.getBatchCode());
                SalesOrderDetail sod = findById(SalesOrderDetail.class, rbc.getSalesOrderDetailId());
                SalesOrder so = findById(SalesOrder.class, sod.getSalesOrderId());
                Consumer c = findById(Consumer.class, so.getSalesOrderConsumerId());
                WeavePlan wp = findById(WeavePlan.class, rbc.getPlanId());
                ProducePlan pp = findOne(ProducePlan.class, "producePlanCode", wp.getPlanCode());
                ts.setCONSUMERNAME(c.getConsumerSimpleName());
                ts.setDeviceCode(r.getRollDeviceCode());
                ts.setIsPacked(0);
                User u = findById(User.class, r.getRollUserId());
                ts.setLoginName(u.getUserName());
                ts.setName(pp.getWorkshop());
                ts.setProducePlanCode(pp.getProducePlanCode());
                ts.setProductLength(product.getProductRollLength());
                ts.setProductModel(product.getProductModel());
                ts.setProductName(product.getFactoryProductName());
                ts.setProductWeight(r.getRollAutoWeight());
                ts.setProductWidth(product.getProductWidth());
                ts.setRollBarcode(barcode);
                ts.setRollOutputTime(r.getRollOutputTime());
                ts.setRollQualityGradeCode(r.getRollQualityGradeCode());
                ts.setRollWeight(r.getRollWeight());
                ts.setSalesOrderCode(sod.getSalesOrderSubCode());
                ts.setState(0);
                ts.setRollQualityGradeCode(qualityGrade);
                ts.setIsLocked(-1);
                save(ts);
            } else {
                ts.setProductWeight(r.getRollAutoWeight());
                ts.setRollWeight(r.getRollWeight());
                ts.setRollQualityGradeCode(qualityGrade);
                update(ts);
            }
        }
    }

    public boolean validDevice(String barcode, String deviceCode) {
        String sql = "SELECT count(1) from hs_weave_plan_devices where weavePlanId=(SELECT planId from hs_roll_barcode where barcode='" + barcode + "') and deviceId=(SELECT id from hs_device where deviceCode='" + deviceCode + "')";
        int sn = ((BigInteger) getSession().createSQLQuery(sql).uniqueResult()).intValue();
        return sn != 0;
    }

    @Resource
    SessionFactory factory;

    @Override
    public Session getSession() {
        return factory.getCurrentSession();
    }

    @Override
    public Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
        return null;
    }
}
