/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.device.dao.impl;

import com.bluebirdme.mes.baseInfo.entity.FtcBomDetail;
import com.bluebirdme.mes.baseInfo.entity.TcBomVersionParts;
import com.bluebirdme.mes.core.base.dao.BaseDaoImpl;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.sql.SQL;
import com.bluebirdme.mes.core.sql.SQLTemplateException;
import com.bluebirdme.mes.device.dao.IDeviceDao;
import com.bluebirdme.mes.planner.produce.entity.ProducePlanDetail;
import com.bluebirdme.mes.planner.produce.entity.ProducePlanDetailPartCount;
import com.bluebirdme.mes.planner.weave.entity.WeavePlan;
import com.bluebirdme.mes.planner.weave.entity.WeavePlanDevices;
import com.bluebirdme.mes.produce.entity.FinishedProduct;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import org.xdemo.superutil.j2se.MathUtils;
import org.xdemo.superutil.j2se.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 宋黎明
 * @Date 2016年4月5日 下午4:35:34
 */
@Repository
public class DeviceDaoImpl extends BaseDaoImpl implements IDeviceDao {
    @Resource
    SessionFactory factory;

    @Override
    public Session getSession() {
        return factory.getCurrentSession();
    }

    @Override
    public Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
        return findPageInfo(filter, page, "device-list");
    }

    public void delete(String ids) {
        getSession().createSQLQuery(SQL.get("device-delete")).setParameterList("id", ids.split(",")).executeUpdate();
    }

    @Override
    public List<Map<String, Object>> find(String id) {
        String sql = SQL.get("find-device");
        SQLQuery query = getSession().createSQLQuery(sql);
        query.setParameter("id", id).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.list();
    }

    public Map<String, Object> getDeliveryDate(Filter filter, Page page) {
        return findPageInfo(filter, page, "device-order-earliest");
    }

    public List<Map<String, Object>> getDeviceDepartment() throws SQLTemplateException {
        return findPageData(new Filter(), new Page(), "device-department");
    }

    /**
     * dids:设备ID
     * wids:编织计划ID
     */
    public void saveDeviceAndOrder(String dids, String wids) {
        String[] didArray = dids.split(",");
        String[] widArray = wids.split(",");
        List<Object> wpds = new ArrayList<>();
        WeavePlanDevices wpd;
        WeavePlan wp;
        Map<String, Object> param = new HashMap<String, Object>();
        for (String s : didArray) {
            for (String value : widArray) {
                wp = findById(WeavePlan.class, Long.parseLong(value));
                if (wp == null) continue;
                wp.setIsSettled(1);
                update(wp);
                param.clear();
                param.put("deviceId", Long.parseLong(s));
                param.put("weavePlanId", Long.parseLong(value));
                if (has(WeavePlanDevices.class, param)) {
                    continue;
                }
                wpd = new WeavePlanDevices();
                wpd.setDeviceId(Long.parseLong(s));
                wpd.setWeavePlanId(Long.parseLong(value));
                wpds.add(wpd);
            }
        }
        saveList(wpds);
    }


    /**
     * dids:设备ID
     * wids:编织计划ID
     */
    @SuppressWarnings("unchecked")
    public void saveDeviceAndOrder2(String dids, String wids) {
        String sql = "SELECT * from Hs_Weave_Plan WHERE partid in (" + wids + ")";
        List<Map<String, Object>> list = getSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        String[] widArray = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            widArray[i] = list.get(i).get("ID").toString();
        }

        String[] didArray = dids.split(",");
        List<Object> wpds = new ArrayList<Object>();
        WeavePlanDevices wpd;
        WeavePlan wp;
        Map<String, Object> param = new HashMap<String, Object>();
        for (String s : didArray) {
            for (String value : widArray) {
                wp = findById(WeavePlan.class, Long.parseLong(value));
                if (wp == null) continue;
                wp.setIsSettled(1);
                update(wp);
                param.clear();
                param.put("deviceId", Long.parseLong(s));
                param.put("weavePlanId", Long.parseLong(value));
                if (has(WeavePlanDevices.class, param)) {
                    continue;
                }
                wpd = new WeavePlanDevices();
                wpd.setDeviceId(Long.parseLong(s));
                wpd.setWeavePlanId(Long.parseLong(value));
                wpds.add(wpd);
            }
        }
        saveList(wpds);
    }


    /**
     * dids:设备ID
     * wids:编织计划ID
     */
    public void deleteDeviceAndOrder(String dids, String wids) {
        String[] didArray = dids.split(",");
        String[] widArray = wids.split(",");
        Map<String, Object> map = new HashMap<>();
        for (String s : didArray) {
            for (String value : widArray) {
                map.clear();
                map.put("deviceId", Long.parseLong(s));
                map.put("weavePlanId", Long.parseLong(value));
                delete(WeavePlanDevices.class, map);
            }
        }
    }

    /**
     * 获取机台的计划
     *
     * @param dids
     * @return
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> findDevicePlans(String dids) {
        String sql = SQL.get("device-plans");
        Map<String, Object> ret = new HashMap<>();
        String wids = (String) getSession().createSQLQuery("SELECT GROUP_CONCAT(wpd.weavePlanId) from hs_weave_plan_devices wpd where wpd.deviceId in (" + dids + ")").uniqueResult();
        if (StringUtils.isBlank(wids)) {
            ret.put("total", 0);
            ret.put("rows", null);
            return ret;
        }
        System.out.println(wids);
        System.out.println(dids);
        List<Map<String, Object>> rows = getSession().createSQLQuery(sql).setParameterList("dids", dids.split(",")).setParameterList("ids", wids.split(",")).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        Double productWeight;
        Double productLength;
        Map<String, Object> map = new HashMap<>();
        FinishedProduct product;
        TcBomVersionParts tvp;
        for (Map<String, Object> items : rows) {
            if (items.get("PRODUCTWEIGHT") == null) {
                //套材订单
                if ("1".equals(items.get("PRODUCTISTC") + "")) {
                    productWeight = new Double(0);
                    map.clear();
                    map.put("ftcBomVersionId", Long.parseLong(items.get("PROCBOMID") + ""));
                    ProducePlanDetail ppd = findById(ProducePlanDetail.class, Long.parseLong(items.get("producePlanDetailId") + ""));
                    for (ProducePlanDetailPartCount c : ppd.getList()) {
                        tvp = findById(TcBomVersionParts.class, c.getPartId());
                        productWeight += tvp.getTcProcBomVersionPartsWeight() * c.getPlanPartCount();
                    }
                } else if ("2".equals(items.get("PRODUCTISTC") + "")) {//非套材订单
                    product = findById(FinishedProduct.class, Long.parseLong(items.get("PRODUCTID") + ""));
                    if (product.getProductRollWeight() == null) {
                        List<FtcBomDetail> bomDetails;
                        map.clear();
                        map.put("ftcBomVersionId", Long.parseLong(items.get("PROCBOMID") + ""));
                        bomDetails = findListByMap(FtcBomDetail.class, map);
                        double bomWeight = 0D;
                        for (FtcBomDetail d : bomDetails) {
                            bomWeight += d.getFtcBomDetailWeightPerSquareMetre() == null ? 0D : d.getFtcBomDetailWeightPerSquareMetre();
                        }
                        //米长*门幅*单位面积克重*卷数
                        productWeight = MathUtils.add(bomWeight * Double.parseDouble(items.get("PRODUCTWIDTH") + "") * Double.parseDouble(items.get("PRODUCTLENGTH") + ""), 0D, 2);
                        productWeight = MathUtils.div(productWeight, 1000000, 2);
                    } else {
                        productWeight = product.getProductRollWeight();
                    }

                } else {//胚布订单
                    List<FtcBomDetail> bomDetails;
                    map.clear();
                    map.put("ftcBomVersionId", Long.parseLong(items.get("PROCBOMID") + ""));
                    bomDetails = findListByMap(FtcBomDetail.class, map);
                    double bomWeight = 0D;
                    for (FtcBomDetail d : bomDetails) {
                        bomWeight += d.getFtcBomDetailWeightPerSquareMetre() == null ? 0D : d.getFtcBomDetailWeightPerSquareMetre();
                    }
                    //米长*门幅*单位面积克重*卷数
                    productWeight = MathUtils.add(bomWeight * Double.parseDouble(items.get("PRODUCTWIDTH") + "") * Double.parseDouble(items.get("PRODUCTLENGTH") + ""), 0D, 2);
                    productWeight = MathUtils.div(productWeight, 1000000, 2);
                }
                items.put("PRODUCTWEIGHT", productWeight);
            }
            if (items.get("PRODUCTLENGTH") == null) {
                if ("1".equals(items.get("PRODUCTISTC") + "")) {
                    productLength = new Double(0);
                } else if ("2".equals(items.get("PRODUCTISTC") + "")) {
                    productLength = new Double(0);
                    product = findById(FinishedProduct.class, Long.parseLong(items.get("PRODUCTID") + ""));
                    if (product.getProductRollWeight() != null) {
                        List<FtcBomDetail> bomDetails = null;
                        map.clear();
                        map.put("ftcBomVersionId", Long.parseLong(items.get("PROCBOMID") + ""));
                        bomDetails = findListByMap(FtcBomDetail.class, map);
                        double bomWeight = 0D;
                        for (FtcBomDetail d : bomDetails) {
                            bomWeight += d.getFtcBomDetailWeightPerSquareMetre() == null ? 0D : d.getFtcBomDetailWeightPerSquareMetre();
                        }
                        productLength = MathUtils.div(Double.parseDouble(items.get("PRODUCTWEIGHT") + "") * 1000000, bomWeight * Double.parseDouble(items.get("PRODUCTWIDTH") + ""), 2);
                    }
                } else {
                    List<FtcBomDetail> bomDetails;
                    map.clear();
                    map.put("ftcBomVersionId", Long.parseLong(items.get("PROCBOMID") + ""));
                    bomDetails = findListByMap(FtcBomDetail.class, map);
                    double bomWeight = 0D;
                    for (FtcBomDetail d : bomDetails) {
                        bomWeight += d.getFtcBomDetailWeightPerSquareMetre() == null ? 0D : d.getFtcBomDetailWeightPerSquareMetre();
                    }
                    productLength = MathUtils.div(Double.parseDouble(items.get("PRODUCTWEIGHT") + "") * 1000000, bomWeight * Double.parseDouble(items.get("PRODUCTWIDTH") + ""), 2);
                }
                items.put("PRODUCTLENGTH", productLength);
            }
        }
        ret.put("total", rows.size());
        ret.put("rows", rows);
        return ret;
    }

    public void deleteDevicePlans(String dids, String wids) {
        String[] didArray = dids.split(",");
        String[] widArray = wids.split(",");
        Map<String, Object> param = new HashMap<String, Object>();

        for (String s : didArray) {
            for (String value : widArray) {
                param.clear();
                param.put("deviceId", Long.parseLong(s));
                param.put("weavePlanId", Long.parseLong(value));
                delete(WeavePlanDevices.class, param);
            }
        }
    }

    @Override
    public <T> Map<String, Object> findAllDevicePlans(Filter filter, Page page) throws Exception {
        Map<String, Object> map = findPageInfo(filter, page, "device-plans-all");
        List<Map<String, Object>> list = (List<Map<String, Object>>) map.get("rows");
        if (list.size() == 0) return map;
        StringBuffer sb = new StringBuffer();
        StringBuffer sb2 = new StringBuffer();
        int i = 0;
        for (Map<String, Object> m : list) {
            if (i++ != 0) {
                sb.append("," + m.get("ID"));
                sb2.append("," + m.get("WEAVEPLANID"));
            } else {
                sb.append(m.get("ID"));
                sb2.append(m.get("WEAVEPLANID"));
            }
        }
        filter.clear();
        Filter ft = new Filter();
        ft.setOrder(filter.getOrder());
        ft.setSort(ft.getSort());
        ft.set("ids", sb.toString());
        ft.set("wids", sb2.toString());
        list = getSession().createSQLQuery(SQL.get(ft.getFilter(), "device-plans-all-count")).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        map.put("rows", list);
        return map;
    }

    public List<Map<String, Object>> findDevicePlans(Long did) {
        String sql = SQL.get("device-plans-jt");
        List<Object> ids = getSession().createSQLQuery(SQL.get("device-plans-jt-pre")).setParameter("did", did).list();
        if (ids.size() == 0) {
            ids.add(-1);
        }
        return getSession().createSQLQuery(sql).setParameter("did", did).setParameterList("ids", ids.subList(0, ids.size() < 5 ? ids.size() : 5)).setMaxResults(5).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }

    @Override
    public void setProducing(Long deviceId, Long weavePlanId) {
        String sql = "update hs_Weave_Plan_Devices set isProducing=0 where deviceId=" + deviceId;
        getSession().createSQLQuery(sql).executeUpdate();
        sql = "update hs_Weave_Plan_Devices set isProducing=1 where deviceId=" + deviceId + " and weavePlanId=" + weavePlanId;
        getSession().createSQLQuery(sql).executeUpdate();
    }

    @Override
    public List<Map<String, Object>> getFtcBcBomVersionDetail(Integer packVersionId) {
        String sql = "SELECT * from hs_ftc_bc_bom_version_detail WHERE packVersionId=" + packVersionId;
        return getSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }

    @Override
    public List<Map<String, Object>> getCutWorkShop() throws SQLTemplateException {
        return findPageData(new Filter(), new Page(), "cut-department");
    }

    @Override
    public Map<String, Object> getBjDetails(String devcode, String yx,
                                            String partname) {
        // TODO Auto-generated method stub
        String sql = SQL.get("device-plans_details");
        Map<String, Object> ret = new HashMap<>();
        String wids = (String) getSession().createSQLQuery("SELECT GROUP_CONCAT(wpd.weavePlanId) from hs_weave_plan_devices wpd where wpd.deviceId =" + devcode).uniqueResult();
        if (StringUtils.isBlank(wids)) {
            ret.put("total", 0);
            ret.put("rows", null);
            return ret;
        }
        List<Map<String, Object>> rows = getSession().createSQLQuery(sql).setParameterList("dids", devcode.split(",")).setParameterList("ids", wids.split(",")).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        Double productWeight;
        Double productLength;
        Map<String, Object> map = new HashMap<>();
        FinishedProduct product;
        TcBomVersionParts tvp;
        for (Map<String, Object> items : rows) {
            if (items.get("PRODUCTWEIGHT") == null) {
                //套材订单
                if ("1".equals(items.get("PRODUCTISTC") + "")) {
                    productWeight = new Double(0);
                    map.clear();
                    map.put("ftcBomVersionId", Long.parseLong(items.get("PROCBOMID") + ""));
                    ProducePlanDetail ppd = findById(ProducePlanDetail.class, Long.parseLong(items.get("producePlanDetailId") + ""));
                    for (ProducePlanDetailPartCount c : ppd.getList()) {
                        tvp = findById(TcBomVersionParts.class, c.getPartId());
                        productWeight += tvp.getTcProcBomVersionPartsWeight() * c.getPlanPartCount();
                    }
                } else if ("2".equals(items.get("PRODUCTISTC") + "")) {//非套材订单
                    product = findById(FinishedProduct.class, Long.parseLong(items.get("PRODUCTID") + ""));
                    if (product.getProductRollWeight() == null) {
                        List<FtcBomDetail> bomDetails = null;
                        map.clear();
                        map.put("ftcBomVersionId", Long.parseLong(items.get("PROCBOMID") + ""));
                        bomDetails = findListByMap(FtcBomDetail.class, map);
                        double bomWeight = 0D;
                        for (FtcBomDetail d : bomDetails) {
                            bomWeight += d.getFtcBomDetailWeightPerSquareMetre() == null ? 0D : d.getFtcBomDetailWeightPerSquareMetre();
                        }
                        //米长*门幅*单位面积克重*卷数
                        productWeight = MathUtils.add(bomWeight * Double.parseDouble(items.get("PRODUCTWIDTH") + "") * Double.parseDouble(items.get("PRODUCTLENGTH") + ""), 0D, 2);
                        productWeight = MathUtils.div(productWeight, 1000000, 2);
                    } else {
                        productWeight = product.getProductRollWeight();
                    }
                } else {
                    //胚布订单
                    List<FtcBomDetail> bomDetails;
                    map.clear();
                    map.put("ftcBomVersionId", Long.parseLong(items.get("PROCBOMID") + ""));
                    bomDetails = findListByMap(FtcBomDetail.class, map);
                    double bomWeight = 0D;
                    for (FtcBomDetail d : bomDetails) {
                        bomWeight += d.getFtcBomDetailWeightPerSquareMetre() == null ? 0D : d.getFtcBomDetailWeightPerSquareMetre();
                    }
                    //米长*门幅*单位面积克重*卷数
                    productWeight = MathUtils.add(bomWeight * Double.parseDouble(items.get("PRODUCTWIDTH") + "") * Double.parseDouble(items.get("PRODUCTLENGTH") + ""), 0D, 2);
                    productWeight = MathUtils.div(productWeight, 1000000, 2);
                }
                items.put("PRODUCTWEIGHT", productWeight);
            }
            if (items.get("PRODUCTLENGTH") == null) {
                if ("1".equals(items.get("PRODUCTISTC") + "")) {
                    productLength = new Double(0);
                } else if ("2".equals(items.get("PRODUCTISTC") + "")) {
                    productLength = new Double(0);
                    product = findById(FinishedProduct.class, Long.parseLong(items.get("PRODUCTID") + ""));
                    if (product.getProductRollWeight() != null) {
                        List<FtcBomDetail> bomDetails;
                        map.clear();
                        map.put("ftcBomVersionId", Long.parseLong(items.get("PROCBOMID") + ""));
                        bomDetails = findListByMap(FtcBomDetail.class, map);
                        double bomWeight = 0D;
                        for (FtcBomDetail d : bomDetails) {
                            bomWeight += d.getFtcBomDetailWeightPerSquareMetre() == null ? 0D : d.getFtcBomDetailWeightPerSquareMetre();
                        }
                        productLength = MathUtils.div(Double.parseDouble(items.get("PRODUCTWEIGHT") + "") * 1000000, bomWeight * Double.parseDouble(items.get("PRODUCTWIDTH") + ""), 2);
                    }
                } else {
                    List<FtcBomDetail> bomDetails;
                    map.clear();
                    map.put("ftcBomVersionId", Long.parseLong(items.get("PROCBOMID") + ""));
                    bomDetails = findListByMap(FtcBomDetail.class, map);
                    double bomWeight = 0D;
                    for (FtcBomDetail d : bomDetails) {
                        bomWeight += d.getFtcBomDetailWeightPerSquareMetre() == null ? 0D : d.getFtcBomDetailWeightPerSquareMetre();
                    }
                    productLength = MathUtils.div(Double.parseDouble(items.get("PRODUCTWEIGHT") + "") * 1000000, bomWeight * Double.parseDouble(items.get("PRODUCTWIDTH") + ""), 2);
                }
                items.put("PRODUCTLENGTH", productLength);
            }
        }
        ret.put("total", rows.size());
        ret.put("rows", rows);
        return ret;
    }
}
