package com.bluebirdme.mes.store.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Index;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;


/**
 * 箱中的卷信息
 *
 * @author 宋黎明
 * @Date 2016年11月08日 上午10:02:34
 */
@Desc("箱中的卷信息")
@Entity
@Table(name = "hs_Box_Roll")
@DynamicInsert
public class BoxRoll extends BaseEntity {
    @Desc("箱条码")
    @Index(name = "boxBarcode")
    private String boxBarcode;

    @Desc("卷条码")
    @Index(name = "rollBarcode")
    private String rollBarcode;

    @Desc("部件条码")
    @Index(name = "partBarcode")
    private String partBarcode;

    @Desc("打包人")
    @Index(name = "packagingStaff")
    private Long packagingStaff;

    @Desc("打包时间")
    @Index(name = "packagingTime")
    private Date packagingTime;

    public Long getPackagingStaff() {
        return packagingStaff;
    }

    public void setPackagingStaff(Long packagingStaff) {
        this.packagingStaff = packagingStaff;
    }

    public Date getPackagingTime() {
        return packagingTime;
    }

    public void setPackagingTime(Date packagingTime) {
        this.packagingTime = packagingTime;
    }

    /**
     * **get**
     */
    public String getBoxBarcode() {
        return boxBarcode;
    }

    /**
     * **set**
     */
    public void setBoxBarcode(String boxBarcode) {
        this.boxBarcode = boxBarcode;
    }

    /**
     * **get**
     */
    public String getRollBarcode() {
        return rollBarcode;
    }

    /**
     * **set**
     */
    public void setRollBarcode(String rollBarcode) {
        this.rollBarcode = rollBarcode;
    }

    /**
     * **get**
     */
    public String getPartBarcode() {
        return partBarcode;
    }

    /**
     * **set**
     */
    public void setPartBarcode(String partBarcode) {
        this.partBarcode = partBarcode;
    }
}
