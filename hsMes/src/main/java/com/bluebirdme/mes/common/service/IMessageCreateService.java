package com.bluebirdme.mes.common.service;

import com.bluebirdme.mes.audit.entity.AuditInstance;
import com.bluebirdme.mes.baseInfo.entity.BCBomVersion;
import com.bluebirdme.mes.baseInfo.entity.FtcBomVersion;
import com.bluebirdme.mes.baseInfo.entity.TcBomVersion;
import com.bluebirdme.mes.core.base.service.IBaseService;
import com.bluebirdme.mes.planner.delivery.entity.DeliveryPlan;
import com.bluebirdme.mes.planner.delivery.entity.DeliveryPlanDetails;
import com.bluebirdme.mes.planner.produce.entity.ProducePlanDetail;
import com.bluebirdme.mes.platform.service.IMessageService;
import com.bluebirdme.mes.sales.entity.SalesOrder;
import com.bluebirdme.mes.sales.entity.SalesOrderDetail;

public interface IMessageCreateService extends IMessageService {
	/**
	 * 生成订单变更消息
	 * @param so 发生变更的SalesOrder对象
	 * 
	 */
//	public void createOrderChange(SalesOrder so);
	/**
	 * 生成订单完成消息
	 * @param so 完成的的SalesOrderDetail对象
	 */
	public void createOrderFinish(SalesOrderDetail so);
	/**
	 * 生成审核消息
	 * @param audit 审核对象实例
	 */
	public void createAuditState(AuditInstance audit);
	/**
	 * 生成套材BOM变更消息
	 * @param tv 变更的TcBomVersion对象
	 */
//	public void createTcBomChange(TcBomVersion tv);
//	/**
//	 * 生成非套材BOM变更消息
//	 * @param ftv 变更的FtcBomVersion对象
//	 */
//	public void createFtcBomChange(FtcBomVersion ftv);
//	/**
//	 * 生成包材BOM变更消息
//	 * @param bc 变更的BCBomVersion对象
//	 */
//	public void createBcBomChange(BCBomVersion bc);
	/**
	 * 生成各类关闭通知
	 * @param comment 通知的内容
	 * @param messageType MessageType中的属性，对应发生的事件
	 */
	public void createClose(String comment,String messageType);
	/**
	 * 生成发货计划超期提醒
	 * @param dp 超期的发货计划明细DeliveryPlanDetails对象
	 */
	public void createOutDate(DeliveryPlan dp);
	/**
	 * 生成订单交货超期提醒
	 * @param sod 超期的订单明细SalesOrderDetail对象
	 */
	public void createOutDate(SalesOrderDetail sod);
	
}
