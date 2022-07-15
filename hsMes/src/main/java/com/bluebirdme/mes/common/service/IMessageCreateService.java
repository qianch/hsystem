package com.bluebirdme.mes.common.service;

import com.bluebirdme.mes.audit.entity.AuditInstance;
import com.bluebirdme.mes.planner.delivery.entity.DeliveryPlan;
import com.bluebirdme.mes.platform.service.IMessageService;
import com.bluebirdme.mes.sales.entity.SalesOrderDetail;

public interface IMessageCreateService extends IMessageService {
    /**
     * 生成订单完成消息
     *
     * @param so 完成的的SalesOrderDetail对象
     */
    void createOrderFinish(SalesOrderDetail so);

    /**
     * 生成审核消息
     *
     * @param audit 审核对象实例
     */
    void createAuditState(AuditInstance audit);

    /**
     * 生成各类关闭通知
     *
     * @param comment     通知的内容
     * @param messageType MessageType中的属性，对应发生的事件
     */
    void createClose(String comment, String messageType);

    /**
     * 生成发货计划超期提醒
     *
     * @param dp 超期的发货计划明细DeliveryPlanDetails对象
     */
    void createOutDate(DeliveryPlan dp);

    /**
     * 生成订单交货超期提醒
     *
     * @param sod 超期的订单明细SalesOrderDetail对象
     */
    void createOutDate(SalesOrderDetail sod);

}
