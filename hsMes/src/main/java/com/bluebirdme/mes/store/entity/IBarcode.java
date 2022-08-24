package com.bluebirdme.mes.store.entity;

import java.util.Date;

public interface IBarcode {
    String getIndividualOutPutString();

    String getOutPutString();

    String getBarcode();

    String getSalesOrderCode();

    Long getSalesProductId();

    String getPartName();

    Long getPlanId();

    String getBatchCode();

    Long getPartId();

    Long getSalesOrderDetailId();

    Long getProducePlanDetailId();


    Long getBtwfileId();

    Long getMirrorProcBomId();

    String getCustomerBarCode();

    String getAgentBarCode();


    void setIndividualOutPutString(String individualOutputString);

    void setProducePlanDetailId(Long producePlanDetailId);

    void setBarcode(String barcode);

    void setOutPutString(String outputString);

    void setSalesOrderCode(String code);

    void setSalesProductId(Long id);

    void setBatchCode(String batchCode);

    void setPartName(String partName);

    void setPlanId(Long id);

    void setPartId(Long partId);

    void setPrintDate(Date printDate);

    void setSalesOrderDetailId(Long salesOrderDetailId);

    void setBtwfileId(Long btwfileId);

    void setMirrorProcBomId(Long mirrorProcBomId);

    void setCustomerBarCode(String customerBarCode);

    void setAgentBarCode(String agentBarCode);
}
