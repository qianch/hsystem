/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.cut.cutSalesOrder.service.impl;

import com.bluebirdme.mes.core.annotation.AnyExceptionRollback;
import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.cut.cutSalesOrder.dao.ICutSalesOrderDao;
import com.bluebirdme.mes.cut.cutSalesOrder.entity.CutSalesOrder;
import com.bluebirdme.mes.cut.cutSalesOrder.service.ICutSalesOrderService;
import com.bluebirdme.mes.planner.produce.entity.ProducePlanDetail;
import com.bluebirdme.mes.produce.entity.FinishedProduct;
import com.bluebirdme.mes.sales.entity.Consumer;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author 季晓龙
 * @Date 2020-09-04 9:30:07
 */
@Service
@AnyExceptionRollback
public class CutSalesOrderServiceImpl extends BaseServiceImpl implements ICutSalesOrderService {
    @Resource
    ICutSalesOrderDao CutSalesOrderDao;

    @Override
    protected IBaseDao getBaseDao() {
        return CutSalesOrderDao;
    }

    @Override
    public String saveCutSalesOrder(CutSalesOrder cutSalesOrder, String userId) {
        String result = "";
        ProducePlanDetail producePlanDetail = findById(ProducePlanDetail.class, cutSalesOrder.getProducePlanDetailId());
        FinishedProduct fp = findById(FinishedProduct.class, producePlanDetail.getProductId());
        Consumer c = findById(Consumer.class, fp.getProductConsumerId());
        cutSalesOrder.setCustomerCode(c.getConsumerCode().trim());
        cutSalesOrder.setCustomerName(c.getConsumerSimpleName().trim());
        cutSalesOrder.setSalesOrderDetailId(producePlanDetail.getFromSalesOrderDetailId());
        if (cutSalesOrder.getId() != null) {
            cutSalesOrder.setModifyTime(new Date());
            cutSalesOrder.setModifyUser(userId);
            update(cutSalesOrder);
        } else {
            cutSalesOrder.setCreater(userId);
            cutSalesOrder.setCreateTime(new Date());
            cutSalesOrder.setModifyTime(new Date());
            cutSalesOrder.setModifyUser(userId);
            save(cutSalesOrder);
        }
        return result;
    }

    @Override
    public String doDeleteCutSalesOrder(String ids) {
        String[] ids_temp = ids.split(",");
        for (String s : ids_temp) {
            CutSalesOrder cutSalesOrder = findById(CutSalesOrder.class, Long.parseLong(s));
            if (cutSalesOrder == null) {
                continue;
            }
            delete(cutSalesOrder);
        }
        return "";
    }
}
