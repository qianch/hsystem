package com.bluebirdme.mes.printer.entity;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Desc("每日各类型条码打印数量")
@Entity
@Table(name = "HS_daily_print_count")
public class DailyPrintCount extends BaseEntity {
    public DailyPrintCount() {
    }

    public DailyPrintCount(String barcodeType, String barcodeDate, int count) {
        this.barcodeType = barcodeType;
        this.barcodeDate = barcodeDate;
        this.barcodeCount = count;
    }

    @Desc("条码类型")
    private String barcodeType;
    @Desc("条码日期")
    private String barcodeDate;
    @Desc("条码数量")
    private Integer barcodeCount;

    public String getBarcodeType() {
        return barcodeType;
    }

    public void setBarcodeType(String barcodeType) {
        this.barcodeType = barcodeType;
    }

    public String getBarcodeDate() {
        return barcodeDate;
    }

    public void setBarcodeDate(String barcodeDate) {
        this.barcodeDate = barcodeDate;
    }

    public Integer getBarcodeCount() {
        return barcodeCount;
    }

    public void setBarcodeCount(Integer barcodeCount) {
        this.barcodeCount = barcodeCount;
    }
}
