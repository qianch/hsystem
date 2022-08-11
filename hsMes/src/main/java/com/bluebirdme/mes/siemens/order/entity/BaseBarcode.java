package com.bluebirdme.mes.siemens.order.entity;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;
import org.hibernate.annotations.Index;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * 裁剪条码
 *
 * @author Goofy
 * @Date 2017年8月2日 下午4:22:00
 */
@MappedSuperclass
public class BaseBarcode extends BaseEntity {

    @Desc("条码号")
    @Column(nullable = false)
    @Index(name = "barcode")
    private String barcode;
    @Desc("打印时间")
    @Column(nullable = false)
    private String printTime;
    @Desc("打印人员")
    @Column(nullable = false)
    private String printUser;
    @Desc("打印类型")
    @Column(nullable = false)
    private String printType;// 补打，重打，正常打印


    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getPrintTime() {
        return printTime;
    }

    public void setPrintTime(String printTime) {
        this.printTime = printTime;
    }

    public String getPrintUser() {
        return printUser;
    }

    public void setPrintUser(String printUser) {
        this.printUser = printUser;
    }

    public String getPrintType() {
        return printType;
    }

    public void setPrintType(String printType) {
        this.printType = printType;
    }

}
