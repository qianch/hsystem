package com.bluebirdme.mes.store.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Transient;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.planner.delivery.entity.DeliveryPlanDetails;
import com.bluebirdme.mes.printer.entity.BarCodePrintRecord;

public interface IBarcode {
	public String getIndividualOutPutString();

	public String getOutPutString();

	public String getBarcode();

	public String getSalesOrderCode();

	public Long getSalesProductId();

	public String getPartName();

	public Long getPlanId();

	public String getBatchCode();

	public Long getPartId();
	
	public Long getSalesOrderDetailId();
	
	public Long getProducePlanDetailId();


	public Long getBtwfileId();

	public Long getMirrorProcBomId();

	public  String  getCustomerBarCode();

	public  String  getAgentBarCode();


	public void setIndividualOutPutString(String individualOutputString);

	public void setProducePlanDetailId(Long producePlanDetailId);

	public void setBarcode(String barcode);

	public void setOutPutString(String outputString);

	public void setSalesOrderCode(String code);

	public void setSalesProductId(Long id);

	public void setBatchCode(String batchCode);

	public void setPartName(String partName);

	public void setPlanId(Long id);

	public void setPartId(Long partId);

	public void setPrintDate(Date printDate);
	
	public void setSalesOrderDetailId(Long salesOrderDetailId);

	public void setBtwfileId(Long btwfileId);

	public void setMirrorProcBomId(Long mirrorProcBomId);

	public void setCustomerBarCode(String customerBarCode);

	public void setAgentBarCode(String agentBarCode);

}
