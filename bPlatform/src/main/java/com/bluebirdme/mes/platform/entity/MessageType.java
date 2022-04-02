package com.bluebirdme.mes.platform.entity;

/**
 * @author qianchen
 * @date 2020/05/21
 */
public abstract class MessageType {
    public static final String ORDER_CLOSE = "订单关闭";
    public static final String ORDER_AUDIT = "订单审核";
    public static final String ORDER_CHANGE = "订单变更";
    public static final String ORDER_FINISH = "订单完成";
    public static final String ORDER_OUTDATE = "订单超期";
    public static final String BOM_AUDIT = "工艺审核";
    public static final String BOM_CHANGE = "工艺变更";
    public static final String PRODUCT_AUDIT = "产品审核";
    public static final String PRODUCE_PLAN_AUDIT = "生产计划审核";
    public static final String PRODUCE_PLAN_CLOSE = "生产计划关闭";
    public static final String TURGBAG_PLAN_AUDIT = "翻包计划审核";
    public static final String CUTDAILY_PLAN_AUDIT = "裁剪计划审核";
    public static final String CUTDAILY_PLAN_CLOSE = "裁剪计划关闭";
    public static final String WEAVEDAILY_PLAN_AUDIT = "编织计划审核";
    public static final String WEAVEDAILY_PLAN_CLOSE = "编织计划关闭";
    public static final String DELIVERY_PLAN_AUDIT = "出库计划审核";
    public static final String DELIVERY_PLAN_CLOSE = "出库计划关闭";
    public static final String DELIVERY_PLAN_OUTDATE = "发货超期";

    public MessageType() {
    }

    public abstract String[] getAllType();
}
