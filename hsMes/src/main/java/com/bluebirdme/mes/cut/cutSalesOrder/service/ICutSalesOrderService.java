/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.cut.cutSalesOrder.service;

import com.bluebirdme.mes.core.base.service.IBaseService;
import com.bluebirdme.mes.cut.cutSalesOrder.entity.CutSalesOrder;

/**
 * @author 季晓龙
 * @Date 2020-9-4 11:10:00
 */
public interface ICutSalesOrderService extends IBaseService {



    /**
     * 保存裁剪订单信息
     * saveCutSalesOrder
     *
     * @param cutSalesOrder
     * @return
     */
    public String saveCutSalesOrder(CutSalesOrder cutSalesOrder, String userId) throws Exception;


    /**
     * 删除裁剪订单
     *
     * @param ids 裁片id
     * @return
     */
    public String doDeleteCutSalesOrder(String ids) throws Exception;

}
