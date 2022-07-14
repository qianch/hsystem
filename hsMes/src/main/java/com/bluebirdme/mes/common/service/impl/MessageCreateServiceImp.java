package com.bluebirdme.mes.common.service.impl;

import com.bluebirdme.mes.audit.entity.AuditConstant;
import com.bluebirdme.mes.audit.entity.AuditInstance;
import com.bluebirdme.mes.common.service.IMessageCreateService;
import com.bluebirdme.mes.core.annotation.AnyExceptionRollback;
import com.bluebirdme.mes.planner.delivery.entity.DeliveryPlan;
import com.bluebirdme.mes.platform.entity.MessageType;
import com.bluebirdme.mes.platform.service.impl.MessageServiceImpl;
import com.bluebirdme.mes.sales.entity.SalesOrderDetail;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;

@Service
@AnyExceptionRollback
public class MessageCreateServiceImp extends MessageServiceImpl implements IMessageCreateService {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    @Override
    public void createOrderFinish(SalesOrderDetail so) {
        Date now = new Date();
        String content = "订单号：" + so.getSalesOrderSubCode() + "，产品厂内名称：" + so.getFactoryProductName() + "，已完成，时间：" + sdf.format(now);
        super.createMessage(content, MessageType.ORDER_FINISH, -1l, "系统通知", new Long[]{-1L}, UUID.randomUUID().toString());
    }

    public void createAuditMessage(AuditInstance audit, String type, boolean isAllUser) {
        if ((audit.getIsCompleted() != null && audit.getIsCompleted() != 1) || audit.getFinalResult() == null) {
            return;
        }
        Date now = new Date();
        String auditResult = "未通过";
        Long[] toUser = new Long[]{audit.getCreateUserId()};
        if (isAllUser && audit.getFinalResult() != null && audit.getFinalResult() == 2) {
            toUser = new Long[]{-1l};
        }
        if (audit.getFinalResult() != null && audit.getFinalResult() == 2) {
            auditResult = "已通过";
        }

        String content = audit.getAuditTitle() + "，审核已完成，审核结果：" + auditResult + "，时间：" + sdf.format(now);
        super.createMessage(content, type, -1l, "系统通知", toUser, UUID.randomUUID().toString());
    }

    @Override
    public void createAuditState(AuditInstance audit) {
        switch (audit.getAuditCode()) {
            case AuditConstant.CODE.XS:
            case AuditConstant.CODE.XS1:
            case AuditConstant.CODE.XS2:

            case AuditConstant.CODE.XS1BG:

            case AuditConstant.CODE.XS2BG:
                createAuditMessage(audit, MessageType.ORDER_AUDIT, true);
                break;
            case AuditConstant.CODE.SC:
                createAuditMessage(audit, MessageType.PRODUCE_PLAN_AUDIT, true);
                break;
            case AuditConstant.CODE.FB:
                createAuditMessage(audit, MessageType.TURGBAG_PLAN_AUDIT, true);
                break;
            case AuditConstant.CODE.DJ:
            case AuditConstant.CODE.JD:
                createAuditMessage(audit, MessageType.PRODUCT_AUDIT, true);
                break;
            case AuditConstant.CODE.CK:
            case AuditConstant.CODE.CK1:
                createAuditMessage(audit, MessageType.DELIVERY_PLAN_AUDIT, true);
                break;
            case AuditConstant.CODE.FTC:
            case AuditConstant.CODE.FTC1:
            case AuditConstant.CODE.FTC2:
            case AuditConstant.CODE.TC:
            case AuditConstant.CODE.TC1:
            case AuditConstant.CODE.TC2:
            case AuditConstant.CODE.BC:
            case AuditConstant.CODE.BC1:
            case AuditConstant.CODE.FTCBC:
            case AuditConstant.CODE.FTCBC1:
            case AuditConstant.CODE.FTCBC2:
                createAuditMessage(audit, MessageType.BOM_AUDIT, false);
                break;
            case AuditConstant.CODE.BC2:
                createAuditMessage(audit, MessageType.BOM_AUDIT, false);
                break;
            case AuditConstant.CODE.RJH:
                break;
            case AuditConstant.CODE.CRJH:
                createAuditMessage(audit, MessageType.CUTDAILY_PLAN_AUDIT, true);
                break;
            case AuditConstant.CODE.CPTC:
            case AuditConstant.CODE.CPTS:
            case AuditConstant.CODE.CPFC:
            case AuditConstant.CODE.CPFS:
            case AuditConstant.CODE.CPPC:
            case AuditConstant.CODE.CPPS:
                createAuditMessage(audit, MessageType.PRODUCT_AUDIT, false);
                break;
        }
    }

    @Override
    public void createClose(String comment, String messageType) {
        Date now = new Date();
        super.createMessage(comment + "，时间：" + sdf.format(now), messageType, -1L, "系统通知", new Long[]{-1L}, UUID.randomUUID().toString());
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
            super.createMessage(comment, MessageType.DELIVERY_PLAN_OUTDATE, -1L, "系统通知", new Long[]{-1L}, UUID.randomUUID().toString());

        }
        if (dp.getDeliveryDate().before(date) && dp.getDeliveryDate().before(new Date())) {
            String comment = "出货计划超期提醒，发货单号：" + dp.getDeliveryCode() + "，应该发货时间：" + sdf.format(dp.getDeliveryDate());
            super.createMessage(comment, MessageType.DELIVERY_PLAN_OUTDATE, -1L, "系统通知", new Long[]{-1L}, UUID.randomUUID().toString());
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
            String comment = "订单发货提醒，订单号：" + sod.getSalesOrderSubCode() + "，客户产品名称：" + sod.getConsumerProductName() + "，发货时间：" + sdf.format(sod.getDeliveryTime());
            super.createMessage(comment, MessageType.ORDER_OUTDATE, -1L, "系统通知", new Long[]{-1L}, UUID.randomUUID().toString());

        }
        if (sod.getDeliveryTime().before(date) && sod.getDeliveryTime().before(new Date())) {
            String comment = "订单超期提醒，订单号：" + sod.getSalesOrderSubCode() + "，客户产品名称：" + sod.getConsumerProductName() + "，应该发货时间：" + sdf.format(sod.getDeliveryTime());
            super.createMessage(comment, MessageType.ORDER_OUTDATE, -1L, "系统通知", new Long[]{-1L}, UUID.randomUUID().toString());
        }
    }
}
