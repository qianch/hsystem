/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.planner.cut.entity;

import java.util.Date;

/**
 * @author Administrator
 * @Date 2016年11月29日 上午1:24:58
 */
public interface Iplan {
    Long getId();

    Long getFromSalesOrderDetailId();

    Long getPartId();

    void setPartId(Long partId);

    Long getFromTcId();

    String getFromTcName();

    Integer getPackagedCount();

    Long getProducePlanDetailId();

    Integer getProductType();

    Integer getIsFinished();

    Long getSort();

    Long getProductId();

    String getPlanCode();

    Long getConsumerId();

    String getConsumerName();

    String getSalesOrderCode();

    String getBatchCode();

    String getProductModel();

    String getProductName();

    Double getProductWidth();

    Double getProductWeight();

    Double getProductLength();

    Double getOrderCount();

    String getBcBomVersion();

    String getBcBomCode();

    String getProcessBomVersion();

    String getProcessBomCode();

    Integer getTotalRollCount();

    Integer getTotalTrayCount();

    Double getRequirementCount();

    Double getProducedCount();

    Date getDeleveryDate();

    String getDeviceCode();

    String getComment();

    String getPartName();

    void setPartName(String name);

    void setDeviceCode(String devCode);

    Long getMirrorProcBomId();
}
