/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.planner.cut.entity;

import java.util.Date;

/**
 * 
 * @author Administrator
 * @Date 2016年11月29日 上午1:24:58
 */
public interface Iplan {
	public Long getId();
	public Long getFromSalesOrderDetailId();

	public Long getPartId();
	public void setPartId(Long partId);
	public Long getFromTcId();

	public String getFromTcName();

	public Integer getPackagedCount();

	public Long getProducePlanDetailId();

	public Integer getProductType();

	public Integer getIsFinished();

	public Long getSort();

	public Long getProductId();

	public String getPlanCode();

	public Long getConsumerId() ;

	public String getConsumerName();

	public String getSalesOrderCode();

	public String getBatchCode();

	public String getProductModel() ;

	public String getProductName();

	public Double getProductWidth();

	public Double getProductWeight();

	public Double getProductLength();

	public Double getOrderCount();

	public String getBcBomVersion();

	public String getBcBomCode();
	
	public String getProcessBomVersion() ;
	
	public String getProcessBomCode() ;

	public Integer getTotalRollCount();
	
	public Integer getTotalTrayCount() ;

	public Double getRequirementCount() ;

	public Double getProducedCount() ;

	public Date getDeleveryDate() ;

	public String getDeviceCode() ;

	public String getComment() ;
	public String getPartName();
	public void setPartName(String name);
	public void setDeviceCode(String devCode);

	public Long getMirrorProcBomId();
}
