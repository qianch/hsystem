package com.bluebirdme.mes.device.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;
import com.bluebirdme.mes.core.dev.DevHelper;

@Desc("称重载具")
@Entity
@Table(name = "HS_WEIGHTCARRIER")
@DynamicInsert
public class WeightCarrier extends BaseEntity {

    @Desc("载具编号")
    @Column(nullable = false, unique = true)
    private String carrierCode;

    @Desc("载具名称")
    @Column(nullable = false)
    private String carrierName;

    @Desc("重量(kg)")
    @Column(nullable = false)
    private double carrierWeight;

    @Desc("所属车间")
    @Column(nullable = false)
    private String workSpace;

    @Desc("所属车间编码")
    private String workShopCode;

    @Desc("创建人")
    @Column(nullable = true)
    private String creater;

    @Desc("创建时间")
    @Column
    private Date createTime;

    @Desc("修改人")
    @Column
    private String modifyUser;

    @Desc("修改时间")
    @Column
    private Date modifyTime;

    public String getCarrierCode() {
        return carrierCode;
    }

    public void setCarrierCode(String carrierCode) {
        this.carrierCode = carrierCode;
    }

    public String getCarrierName() {
        return carrierName;
    }

    public void setCarrierName(String carrierName) {
        this.carrierName = carrierName;
    }

    public double getCarrierWeight() {
        return carrierWeight;
    }

    public void setCarrierWeight(double carrierWeight) {
        this.carrierWeight = carrierWeight;
    }

    public String getWorkSpace() {
        return workSpace;
    }

    public void setWorkSpace(String workSpace) {
        this.workSpace = workSpace;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getModifyUser() {
        return modifyUser;
    }

    public void setModifyUser(String modifyUser) {
        this.modifyUser = modifyUser;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getWorkShopCode() {
        return workShopCode;
    }

    public void setWorkShopCode(String workShopCode) {
        this.workShopCode = workShopCode;
    }

    public static void main(String[] args) throws Exception {
        DevHelper.genAll(WeightCarrier.class, "孙利");

    }
}
