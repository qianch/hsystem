package com.bluebirdme.mes.common.service.impl;

import com.bluebirdme.mes.common.dao.IProcessDao;
import com.bluebirdme.mes.common.service.IProcessService;
import com.bluebirdme.mes.core.annotation.AnyExceptionRollback;
import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.planner.cut.entity.CutPlan;
import com.bluebirdme.mes.planner.produce.entity.ProducePlanDetail;
import com.bluebirdme.mes.planner.weave.entity.WeavePlan;
import com.bluebirdme.mes.sales.entity.SalesOrderDetail;
import org.springframework.stereotype.Service;
import org.xdemo.superutil.j2se.MapUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 进度更新
 *
 * @author Goofy
 * @Date 2016年12月2日 下午9:07:20
 */
@Service
@AnyExceptionRollback
public class ProcessServiceImpl extends BaseServiceImpl implements IProcessService {
    @Resource
    IProcessDao dao;

    @Override
    protected IBaseDao getBaseDao() {
        return dao;
    }

    @Override
    public <T> Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
        return null;
    }

    /**
     * 编织计划生产进度，不涉及套材的进度更新 如果是套材，暂不更新进度，由裁剪计划来更新进度
     *
     * @param weaveId    编织计划ID
     * @param rollWeight 卷重
     */
    public void updateBzsc(Long weaveId, Double rollWeight) throws Exception {
        // 编织计划
        WeavePlan wp = findById(WeavePlan.class, weaveId);
        if (wp == null) {
            throw new Exception("找不到对应的编织计划");
        }
        // 查询编织对应的某个具体的生产计划
        ProducePlanDetail ppd = findById(ProducePlanDetail.class, wp.getProducePlanDetailId());
        if (ppd == null)
            throw new Exception("找不到对应的生产计划");
        // 查询生产计划对应的
        SalesOrderDetail sod = findById(SalesOrderDetail.class, wp.getFromSalesOrderDetailId());
        if (sod == null)
            throw new Exception("找不到对应的订单");
        // 编织计划已生产的重量累加
        wp.setProducedTotalWeight((wp.getProducedTotalWeight() == null ? 0D : wp.getProducedTotalWeight()) + rollWeight);
        // 编织计划已生产的卷数累加
        wp.setProduceRollCount((wp.getProduceRollCount() == null ? 0D : wp.getProduceRollCount()) + 1);
        // 生产计划已生产的数量或重量累加，这里肯定是非套材的，套材的数量由裁剪那边登记
        if (ppd.getProductIsTc() == null || ppd.getProductIsTc() != 1) {
            // 生产计划已生产的卷数累加，这里不应该更新套材的进度，套材的进度由裁剪登记那边计算出来
            ppd.setProducedRolls((ppd.getProducedRolls() == null ? 0 : ppd.getProducedRolls()) + 1);
            // 非套材，计算重量，累加
            ppd.setProducedCount((ppd.getProducedCount() == null ? 0D : ppd.getProducedCount()) + rollWeight);
            // 订单累加
            sod.setProduceCount((sod.getProduceCount() == null ? 0D : sod.getProduceCount()) + rollWeight);
            // 已生产卷数
            sod.setProducedRolls((sod.getProducedRolls() == null ? 0 : sod.getProducedRolls()) + 1);
        }
        update(wp);
        update(ppd);
        update(sod);
    }

    /**
     * 修改重量
     * 编织计划生产进度，不涉及套材的进度更新 如果是套材，暂不更新进度，由裁剪计划来更新进度
     *
     * @param weaveId    编织计划ID
     * @param rollWeight 卷重
     */
    public void updateBzscWeight(Long weaveId, Double rollWeight) throws Exception {
        // 编织计划
        WeavePlan wp = findById(WeavePlan.class, weaveId);
        if (wp == null) {
            throw new Exception("找不到对应的编织计划");
        }
        // 查询编织对应的某个具体的生产计划
        ProducePlanDetail ppd = findById(ProducePlanDetail.class, wp.getProducePlanDetailId());
        if (ppd == null)
            throw new Exception("找不到对应的生产计划");

        // 查询生产计划对应的
        SalesOrderDetail sod = findById(SalesOrderDetail.class, wp.getFromSalesOrderDetailId());
        if (sod == null)
            throw new Exception("找不到对应的订单");
        // 编织计划已生产的重量累加
        wp.setProducedTotalWeight((wp.getProducedTotalWeight() == null ? 0D : wp.getProducedTotalWeight()) + rollWeight);
        // 生产计划已生产的数量或重量累加，这里肯定是非套材的，套材的数量由裁剪那边登记
        if (ppd.getProductIsTc() == null || ppd.getProductIsTc() != 1) {
            // 非套材，计算重量，累加
            ppd.setProducedCount((ppd.getProducedCount() == null ? 0D : ppd.getProducedCount()) + rollWeight);
            // 订单累加
            sod.setProduceCount((sod.getProduceCount() == null ? 0D : sod.getProduceCount()) + rollWeight);
        }
        update(wp);
        update(ppd);
        update(sod);
    }

    /**
     * 编织计划打包进度
     *
     * @param weaveId    编织计划ID
     * @param trayWeight 托重量
     * @throws Exception
     */
    public void updateBzdb(Long weaveId, Double trayWeight) throws Exception {
        // 编织计划
        WeavePlan wp = findById(WeavePlan.class, weaveId);
        if (wp == null) {
            throw new Exception("找不到对应的编织计划");
        }
        // 查询编织对应的某个具体的生产计划
        ProducePlanDetail ppd = findById(ProducePlanDetail.class, wp.getProducePlanDetailId());
        if (ppd == null)
            throw new Exception("找不到对应的生产计划");

        // 查询生产计划对应的
        SalesOrderDetail sod = findById(SalesOrderDetail.class, wp.getFromSalesOrderDetailId());
        if (sod == null)
            throw new Exception("找不到对应的订单");

        // 更新编织计划的打包数量
        wp.setPackagedCount((wp.getPackagedCount() == null ? 0 : wp.getPackagedCount()) + 1);

        // 生产计划已生产的数量或重量累加，这里肯定是非套材的，套材的数量由裁剪那边登记
        if (ppd.getProductIsTc() == null || ppd.getProductIsTc() != 1) {
            // 打包数量+1
            ppd.setPackagedCount((ppd.getPackagedCount() == null ? 0 : ppd.getPackagedCount()) + 1);
            // 订单已生产托数
            sod.setProducedTrays((sod.getProducedTrays() == null ? 0 : sod.getProducedTrays()) + 1);
        }
        update(wp);
        update(ppd);
        update(sod);
    }

    /**
     * 裁剪产出 更新卷数和重量 裁剪计划肯定是套材，裁剪计划无打包进度，因为一托可能是不同的部件，只有生产进度，生产了多少卷，多少托
     *
     * @param cutId      裁剪计划ID
     * @param rollWeight 卷重
     * @throws Exception
     */
    public void updateCjsc(Long cutId, Double rollWeight) throws Exception {
        // 编织计划
        CutPlan cp = findById(CutPlan.class, cutId);
        if (cp == null) {
            throw new Exception("找不到对应的裁剪计划");
        }
        // 查询编织对应的某个具体的生产计划
        ProducePlanDetail ppd = findById(ProducePlanDetail.class, cp.getProducePlanDetailId());
        if (ppd == null)
            throw new Exception("找不到对应的裁剪计划");
        // 查询生产计划对应的
        SalesOrderDetail sod = findById(SalesOrderDetail.class, cp.getFromSalesOrderDetailId());
        if (sod == null)
            throw new Exception("找不到对应的订单");
        // 更新裁剪计划生产的总重量,这里记录总共产出的重量，就不要在打包时候记录重量了
        cp.setProducedCount((cp.getProducedCount() == null ? 0 : cp.getProducedCount()) + rollWeight);
        // 裁剪计划卷数累加
        cp.setProducedRolls((cp.getProducedRolls() == null ? 0 : cp.getProducedRolls()) + 1);
        // 裁剪计划对应的生产计划产出卷数，累加1
        ppd.setProducedRolls((ppd.getProducedRolls() == null ? 0 : ppd.getProducedRolls()) + 1);
        // 已生产卷数累加
        sod.setProducedRolls((sod.getProducedRolls() == null ? 0 : sod.getProducedRolls()) + 1);
        update(cp);
        update(ppd);
        update(sod);
    }

    /**
     * 修改重量
     *
     * @param cutId      裁剪计划ID
     * @param rollWeight 卷重
     * @throws Exception
     */
    public void updateCjscWeight(Long cutId, Double rollWeight) throws Exception {
        // 编织计划
        CutPlan cp = findById(CutPlan.class, cutId);
        if (cp == null) {
            throw new Exception("找不到对应的裁剪计划");
        }
        // 查询编织对应的某个具体的生产计划
        ProducePlanDetail ppd = findById(ProducePlanDetail.class, cp.getProducePlanDetailId());
        if (ppd == null)
            throw new Exception("找不到对应的裁剪计划");
        // 查询生产计划对应的
        SalesOrderDetail sod = findById(SalesOrderDetail.class, cp.getFromSalesOrderDetailId());
        if (sod == null)
            throw new Exception("找不到对应的订单");
        // 更新裁剪计划生产的总重量,这里记录总共产出的重量，就不要在打包时候记录重量了
        cp.setProducedCount((cp.getProducedCount() == null ? 0 : cp.getProducedCount()) + rollWeight);
        Map<String, Object> bomPartCount = getLeafPart(ppd.getProcessBomCode(), ppd.getProcessBomVersion());
        Map<String, Object> producedPartCount = getLeafPart(ppd.getProcessBomCode(), ppd.getProcessBomVersion());
        Iterator<Entry<String, Object>> it = bomPartCount.entrySet().iterator();
        Entry<String, Object> entry;
        //比例数组，用于获取最小的一个值，最小的一个值，就是套数
        Double[] ratios = new Double[bomPartCount.size()];
        int i = 0;
        Long partCount;
        Long produceCount;
        BigDecimal b1;
        BigDecimal b2;
        while (it.hasNext()) {
            entry = it.next();
            double ratio = 0D;
            if (producedPartCount.get(entry.getKey()) != null) {
                partCount = (Long) entry.getValue();
                produceCount = (Long) producedPartCount.get(entry.getKey());
                if (produceCount.intValue() != 0L) {
                    b1 = new BigDecimal(Long.toString(partCount));
                    b2 = new BigDecimal(Long.toString(produceCount));
                    ratio = b1.divide(b2, 0, BigDecimal.ROUND_DOWN).doubleValue();
                }
            }
            ratios[i++] = ratio;
        }

        int min = Integer.MAX_VALUE;
        //获取最小值
        for (Double d : ratios) {
            if (d.intValue() < min) {
                min = d.intValue();
            }
        }
        //更新套材生产计划的套数
        ppd.setProducedCount((double) min);
        sod.setProduceCount((double) min);
        update(cp);
        update(ppd);
        update(sod);
    }

    /**
     * 更新裁剪打包进度，实际上裁剪计划是无打包进度的，不同部件可能打包在一托，这里只更新该计划生产了多少托
     *
     * @param cutId      裁剪计划ID
     * @param partName   部件名称
     * @param trayWeight 托重量
     * @throws Exception
     */
    public void updateCjdb(Long cutId, String partName, Double trayWeight) throws Exception {
        // 编织计划
        CutPlan cp = findById(CutPlan.class, cutId);
        if (cp == null) {
            throw new Exception("找不到对应的裁剪计划");
        }
        // 查询编织对应的某个具体的生产计划
        ProducePlanDetail ppd = findById(ProducePlanDetail.class, cp.getProducePlanDetailId());
        if (ppd == null)
            throw new Exception("找不到对应的裁剪计划");
        // 查询生产计划对应的订单明细
        SalesOrderDetail sod = findById(SalesOrderDetail.class, cp.getFromSalesOrderDetailId());
        //裁剪计划打包，更新裁剪计划的打包托数
        ppd.setPackagedCount((ppd.getPackagedCount() == null ? 0 : ppd.getPackagedCount()) + 1);
        //订单明细的打包数量，只有重量，没有套数
        sod.setPackagingCount((sod.getPackagingCount() == null ? 0 : sod.getPackagingCount()) + 1);
        update(ppd);
        update(sod);
    }

    /**
     * 获取已生产的部件数量
     *
     * @param ppdId
     * @return
     */
    public Map<String, Object> getProducedPartCount(Long ppdId) {
        Map<String, Object> ret = new HashMap<String, Object>();
        List<Map<String, Object>> list = dao.getProducedPartCount(ppdId);
        for (Map<String, Object> map : list) {
            ret.put(MapUtils.getAsString(map, "PARTNAME"), MapUtils.getAsLong(map, "COUNT"));
        }
        return ret;
    }

    /**
     * 获取bom的所有叶子节点部件以及其数量
     */
    @Override
    public Map<String, Object> getLeafPart(String code, String ver) {
        Map<String, Object> ret = new HashMap<String, Object>();
        List<Map<String, Object>> list = dao.getLeafPart(code, ver);
        Entry<String, Object> entry;
        Iterator<Entry<String, Object>> it;
        for (Map<String, Object> map : list) {
            it = map.entrySet().iterator();
            while (it.hasNext()) {
                entry = it.next();
                ret.put(entry.getKey(), entry.getValue());
            }
        }
        return ret;
    }

    /**
     * 获取bom的所有叶子节点部件以及其数量(List)
     */
    @Override
    public List<Map<String, Object>> getLeafParts(String code, String ver) {
        return dao.getLeafPart(code, ver);
    }
}
