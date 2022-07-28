package com.bluebirdme.mes.planner.produce.entity;

public class PlannerCodesUtil {
    //临时计划
    public static int producePlanTemp = -1;
    //非临时计划
    public static int producePlanNotTemp = 1;
    //自动生成生产计划
    public static int producePlanAutoCreate = 1;
    //手动生成生产计划,可以没有订单号的生产计划
    public static int producePlanUserCreate = 2;
    //废弃的任务
    public static int producePlanDeprecated = -1;
    //正常自动生成的任务
    public static int producePlanDeprecatedDefult = 0;
    //套材产品
    public static int productIsTc = 1;
    //非套材产品
    public static int productIsFtc = 2;
}
