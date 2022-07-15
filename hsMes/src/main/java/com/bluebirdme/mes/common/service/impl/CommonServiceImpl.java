/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.common.service.impl;

import com.bluebirdme.mes.common.dao.ICommonDao;
import com.bluebirdme.mes.common.service.ICommonService;
import com.bluebirdme.mes.common.service.IMessageCreateService;
import com.bluebirdme.mes.core.annotation.AnyExceptionRollback;
import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.planner.cut.entity.CutPlan;
import com.bluebirdme.mes.planner.produce.entity.ProducePlanDetail;
import com.bluebirdme.mes.planner.weave.entity.WeavePlan;
import com.bluebirdme.mes.planner.weave.entity.WeavePlanDevices;
import com.bluebirdme.mes.platform.entity.MessageType;
import com.bluebirdme.mes.sales.entity.SalesOrderDetail;
import com.bluebirdme.mes.sales.entity.SalesOrderDetailTemp;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 高飞
 * @Date 2017-1-5 18:10:36
 */
@Service
@AnyExceptionRollback
public class CommonServiceImpl extends BaseServiceImpl implements ICommonService {
    @Resource
    ICommonDao commonDao;

    @Resource
    IMessageCreateService msgCreateService;

    @Override
    protected IBaseDao getBaseDao() {
        return commonDao;
    }

    @Override
    public <T> Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
        return commonDao.findPageInfo(filter, page);
    }

    public List<Map<String, Object>> getMsg() {
        return commonDao.getMsg();
    }

    public void close(String ids, String type) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        Class clazz = null;
        switch (type.toUpperCase()) {
            case "ORDER" -> clazz = SalesOrderDetail.class;
            case "PRODUCE" -> clazz = ProducePlanDetail.class;
            case "WEAVE" -> clazz = WeavePlan.class;
            case "CUT" -> clazz = CutPlan.class;
            default -> {
            }
        }

        Object obj;
        String[] _ids = ids.split(",");
        for (String id : _ids) {
            if (type.equalsIgnoreCase("CUT")) {
                CutPlan cutPlan = findById(CutPlan.class, Long.parseLong(id));
                List<SalesOrderDetailTemp> list = find(SalesOrderDetailTemp.class, "productBatchCode", cutPlan.getBatchCode());
                for (SalesOrderDetailTemp s : list) {
                    s.setClosed(1);
                    update(s);
                }
            }
            obj = findById(clazz, Long.parseLong(id));
            obj.getClass().getMethod("setClosed", Integer.class).invoke(obj, new Object[]{1});
            update(obj);
            Map<String, Object> param = new HashMap<>();

            switch (type.toUpperCase()) {
                case "ORDER" -> {
                    String order = (String) obj.getClass().getMethod("getSalesOrderSubCode", null).invoke(obj, new Object[]{});
                    String factoryProductName = (String) obj.getClass().getMethod("getFactoryProductName", null).invoke(obj, new Object[]{});
                    String comment = "订单关闭通知," + "订单号:" + order + ",产品:" + factoryProductName;
                    msgCreateService.createClose(comment, MessageType.ORDER_CLOSE);
                }
                case "PRODUCE" -> {
                    String code = (String) obj.getClass().getMethod("getPlanCode", null).invoke(obj, new Object[]{});
                    String factoryProductName = (String) obj.getClass().getMethod("getFactoryProductName", null).invoke(obj, new Object[]{});
                    String batch = (String) obj.getClass().getMethod("getBatchCode", null).invoke(obj, new Object[]{});
                    String comment = "生产计划关闭通知," + "计划单号:" + code + ",产品:" + factoryProductName + "，批次号:" + batch;
                    msgCreateService.createClose(comment, MessageType.PRODUCE_PLAN_CLOSE);
                }
                case "WEAVE" -> {
                    String code = (String) obj.getClass().getMethod("getPlanCode", null).invoke(obj, new Object[]{});
                    String factoryProductName = (String) obj.getClass().getMethod("getProductName", null).invoke(obj, new Object[]{});
                    String batch = (String) obj.getClass().getMethod("getBatchCode", null).invoke(obj, new Object[]{});
                    String comment = "编织计划关闭通知," + "计划单号:" + code + ",产品:" + factoryProductName + "，批次号:" + batch;
                    msgCreateService.createClose(comment, MessageType.WEAVEDAILY_PLAN_CLOSE);
                    param.clear();
                    param.put("weavePlanId", Long.parseLong(id));
                    delete(WeavePlanDevices.class, param);
                }
                case "CUT" -> {
                    String code = (String) obj.getClass().getMethod("getPlanCode", null).invoke(obj, new Object[]{});
                    String factoryProductName = (String) obj.getClass().getMethod("getProductName", null).invoke(obj, new Object[]{});
                    String batch = (String) obj.getClass().getMethod("getBatchCode", null).invoke(obj, new Object[]{});
                    String comment = "裁剪计划关闭通知," + "计划单号:" + code + ",产品:" + factoryProductName + "，批次号:" + batch;
                    msgCreateService.createClose(comment, MessageType.CUTDAILY_PLAN_CLOSE);
                }
                default -> {
                }
            }
        }
    }
}
