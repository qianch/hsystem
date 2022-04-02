package com.bluebirdme.mes.common.service.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;

import javax.annotation.Resource;
import com.bluebirdme.mes.core.annotation.AnyExceptionRollback;

import org.springframework.stereotype.Service;

import com.bluebirdme.mes.audit.entity.AuditConstant;
import com.bluebirdme.mes.audit.entity.AuditInstance;
import com.bluebirdme.mes.baseInfo.entity.BCBomVersion;
import com.bluebirdme.mes.baseInfo.entity.FtcBomVersion;
import com.bluebirdme.mes.baseInfo.entity.TcBomVersion;
import com.bluebirdme.mes.common.service.IMessageCreateService;
import com.bluebirdme.mes.planner.delivery.entity.DeliveryPlan;
import com.bluebirdme.mes.planner.delivery.entity.DeliveryPlanDetails;
import com.bluebirdme.mes.platform.entity.MessageType;
import com.bluebirdme.mes.platform.service.impl.MessageServiceImpl;
import com.bluebirdme.mes.sales.entity.SalesOrder;
import com.bluebirdme.mes.sales.entity.SalesOrderDetail;
@Service
@AnyExceptionRollback
public class MessageCreateServiceImp extends MessageServiceImpl implements IMessageCreateService {
	SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd hh:mm:ss");

	// @Override
	// public void createOrderChange(SalesOrder so) {
	// Date now = new Date();
	// String content = "订单编号：" + so.getSalesOrderCode() + " 订单变更中，时间：" +
	// sdf.format(now);
	// super.createMessage(content, MessageType.ORDER_CHANGE, -1l, "系统通知", new
	// Long[] { -1l }, UUID.randomUUID().toString());
	// }

	@Override
	public void createOrderFinish(SalesOrderDetail so) {
		Date now = new Date();
		String content = "订单号：" + so.getSalesOrderSubCode() + "，产品厂内名称：" + so.getFactoryProductName() + "，已完成，时间：" + sdf.format(now);
		super.createMessage(content, MessageType.ORDER_FINISH, -1l, "系统通知", new Long[] { -1l }, UUID.randomUUID().toString());
	}

	public void createAuditMessage(AuditInstance audit, String type, boolean isAllUser) {
		if ((audit.getIsCompleted()!=null&&audit.getIsCompleted() != 1)||audit.getFinalResult() ==null) {
			return;
		}
		Date now = new Date();
		String auditResult = "未通过";
		Long[] toUser = new Long[] { audit.getCreateUserId() };
		if (isAllUser &&audit.getFinalResult() !=null &&audit.getFinalResult() == 2) {
			toUser = new Long[] { -1l };
		}
		if (audit.getFinalResult() !=null &&audit.getFinalResult() == 2) {
			auditResult = "已通过";
		}

		String content = audit.getAuditTitle() + "，审核已完成，审核结果：" + auditResult + "，时间：" + sdf.format(now);
		super.createMessage(content, type, -1l, "系统通知", toUser, UUID.randomUUID().toString());
	}

	@Override
	public void createAuditState(AuditInstance audit) {
		switch (audit.getAuditCode()) {
		/**
		 * 销售
		 */
		case AuditConstant.CODE.XS:
			createAuditMessage(audit, MessageType.ORDER_AUDIT, true);
			break;
		/**
		 * 生产
		 */
		case AuditConstant.CODE.SC:
			createAuditMessage(audit, MessageType.PRODUCE_PLAN_AUDIT, true);
			break;
		/**
		 * 翻包
		 */
		case AuditConstant.CODE.FB:
			createAuditMessage(audit, MessageType.TURGBAG_PLAN_AUDIT, true);
			break;
		/**
		 * 冻结
		 */
		case AuditConstant.CODE.DJ:
			createAuditMessage(audit, MessageType.PRODUCT_AUDIT, true);
			break;
		/**
		 * 解冻
		 */
		case AuditConstant.CODE.JD:
			createAuditMessage(audit, MessageType.PRODUCT_AUDIT, true);
			break;
		/**
		 * 内销出库
		 */
		case AuditConstant.CODE.CK:
			createAuditMessage(audit, MessageType.DELIVERY_PLAN_AUDIT, true);
			break;
		/**
		 * 外销出库
		 */
		case AuditConstant.CODE.CK1:
			createAuditMessage(audit, MessageType.DELIVERY_PLAN_AUDIT, true);
			break;
		/**
		 * 非套材常规
		 */
		case AuditConstant.CODE.FTC:
			createAuditMessage(audit, MessageType.BOM_AUDIT, false);
			break;
		/**
		 * 非套材变更试样
		 */
		case AuditConstant.CODE.FTC1:
			createAuditMessage(audit, MessageType.BOM_AUDIT, false);
			break;
		/**
		 * 非套材新品试样
		 */
		case AuditConstant.CODE.FTC2:
			createAuditMessage(audit, MessageType.BOM_AUDIT, false);
			break;
		/**
		 * 套材常规
		 */
		case AuditConstant.CODE.TC:
			createAuditMessage(audit, MessageType.BOM_AUDIT, false);
			break;
		/**
		 * 套材变更试样
		 */
		case AuditConstant.CODE.TC1:
			createAuditMessage(audit, MessageType.BOM_AUDIT, false);
			break;
		/**
		 * 套材新品试样
		 */
		case AuditConstant.CODE.TC2:
			createAuditMessage(audit, MessageType.BOM_AUDIT, false);
			break;
		/**
		 * 包材常规
		 */
		case AuditConstant.CODE.BC:
			createAuditMessage(audit, MessageType.BOM_AUDIT, false);
			break;
		/**
		 * 包材变更试样
		 */
		case AuditConstant.CODE.BC1:
			createAuditMessage(audit, MessageType.BOM_AUDIT, false);
			break;
		/**
		 * 包材新品试样
		 */
		case AuditConstant.CODE.BC2:
			createAuditMessage(audit, MessageType.BOM_AUDIT, false);
			break;
		/**
		 * 非套材包材常规
		 */
		case AuditConstant.CODE.FTCBC:
			createAuditMessage(audit, MessageType.BOM_AUDIT, false);
			break;
		/**
		 * 非套材包材变更试样
		 */
		case AuditConstant.CODE.FTCBC1:
			createAuditMessage(audit, MessageType.BOM_AUDIT, false);
			break;
		/**
		 * 非套材包材新品试样
		 */
		case AuditConstant.CODE.FTCBC2:
			createAuditMessage(audit, MessageType.BOM_AUDIT, false);
			break;
		/**
		 * 日计划
		 */
		case AuditConstant.CODE.RJH:
			break;
		/**
		 * 裁剪日计划
		 */
		case AuditConstant.CODE.CRJH:
			createAuditMessage(audit, MessageType.CUTDAILY_PLAN_AUDIT, true);
			break;
		// /**
		// * 编制日计划（一车间）
		// */
		// case AuditConstant.CODE.RJH1:
		// break;
		// /**
		// * 编制日计划（二车间）
		// */
		// case AuditConstant.CODE.RJH2:
		// break;
		// /**
		// * 编制日计划（三车间）
		// */
		// case AuditConstant.CODE.RJH3:
		// break;
		/**
		 * 销售（国内）
		 */
		case AuditConstant.CODE.XS1:
			createAuditMessage(audit, MessageType.ORDER_AUDIT, true);
			break;
		/**
		 * 销售（国外）
		 */
		case AuditConstant.CODE.XS2:
			createAuditMessage(audit, MessageType.ORDER_AUDIT, true);
			break;

		/**
		 * 国内销售变更审核
		 */
		case AuditConstant.CODE.XS1BG:
			createAuditMessage(audit, MessageType.ORDER_AUDIT, true);
			break;

		/**
		 * 国外销售变更审核
		 */
		case AuditConstant.CODE.XS2BG:
			createAuditMessage(audit, MessageType.ORDER_AUDIT, true);
			break;
		/**
		 * 成品套材常规
		 */
		case AuditConstant.CODE.CPTC:
			createAuditMessage(audit, MessageType.PRODUCT_AUDIT, false);
			break;
		/**
		 * 成品套材试用
		 */
		case AuditConstant.CODE.CPTS:
			createAuditMessage(audit, MessageType.PRODUCT_AUDIT, false);
			break;
		/**
		 * 成品非套材常规
		 */
		case AuditConstant.CODE.CPFC:
			createAuditMessage(audit, MessageType.PRODUCT_AUDIT, false);
			break;
		/**
		 * 成品非套材试用
		 */
		case AuditConstant.CODE.CPFS:
			createAuditMessage(audit, MessageType.PRODUCT_AUDIT, false);
			break;
		/**
		 * 成品胚布常规
		 */
		case AuditConstant.CODE.CPPC:
			createAuditMessage(audit, MessageType.PRODUCT_AUDIT, false);
			break;
		/**
		 * 成品胚布试用
		 */
		case AuditConstant.CODE.CPPS:
			createAuditMessage(audit, MessageType.PRODUCT_AUDIT, false);
			break;
		}
	}

	@Override
	public void createClose(String comment, String messageType) {
		Date now = new Date();
		super.createMessage(comment + "，时间：" + sdf.format(now), messageType, -1l, "系统通知", new Long[] { -1l }, UUID.randomUUID().toString());
	}

	@Override
	public void createOutDate(DeliveryPlan dp) {
		Date date = new Date();// 取时间
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(calendar.DATE, 2);// 把日期往后增加一天.整数往后推,负数往前移动
		date = calendar.getTime();
		if (dp.getDeliveryDate().before(date) && dp.getDeliveryDate().after(new Date())) {
			String comment = "出货计划发货提醒，发货单号：" + dp.getDeliveryCode() + "，发货时间：" + sdf.format(dp.getDeliveryDate());
			super.createMessage(comment, MessageType.DELIVERY_PLAN_OUTDATE, -1l, "系统通知", new Long[] { -1l }, UUID.randomUUID().toString());

		}
		if (dp.getDeliveryDate().before(date) && dp.getDeliveryDate().before(new Date())) {
			String comment = "出货计划超期提醒，发货单号：" + dp.getDeliveryCode() + "，应该发货时间：" + sdf.format(dp.getDeliveryDate());
			super.createMessage(comment, MessageType.DELIVERY_PLAN_OUTDATE, -1l, "系统通知", new Long[] { -1l }, UUID.randomUUID().toString());
		}
	}

	@Override
	public void createOutDate(SalesOrderDetail sod) {
		Date date = new Date();// 取时间
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(calendar.DATE, 2);// 把日期往后增加一天.整数往后推,负数往前移动
		date = calendar.getTime();
		if (sod.getDeliveryTime().before(date) && sod.getDeliveryTime().after(new Date())) {
			String comment = "订单发货提醒，订单号：" + sod.getSalesOrderSubCode() +"，客户产品名称：" +sod.getConsumerProductName()+"，发货时间：" + sdf.format(sod.getDeliveryTime());
			super.createMessage(comment, MessageType.ORDER_OUTDATE, -1l, "系统通知", new Long[] { -1l }, UUID.randomUUID().toString());

		}
		if (sod.getDeliveryTime().before(date) && sod.getDeliveryTime().before(new Date())) {
			String comment = "订单超期提醒，订单号：" + sod.getSalesOrderSubCode() + "，客户产品名称：" +sod.getConsumerProductName()+"，应该发货时间：" + sdf.format(sod.getDeliveryTime());
			super.createMessage(comment, MessageType.ORDER_OUTDATE, -1l, "系统通知", new Long[] { -1l }, UUID.randomUUID().toString());
		}
	}

}
