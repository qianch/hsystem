/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.device.entity;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Index;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * 设备信息
 *
 * @author 宋黎明
 * @Date 2016年09月29日 上午10:02:34
 */
@Desc("设备信息")
@Entity
@Table(name = "HS_DEVICE")
@DynamicInsert
public class Device extends BaseEntity {
    @Desc("设备资产编号")
    @Column(nullable = false)
    private String deviceAssetCode;

    @Desc("设备代码")
    @Column(nullable = false)
    @Index(name = "deviceCode")
    private String deviceCode;

    @Desc("资产名称")
    @Column(nullable = false)
    private String deviceName;

    @Desc("设备类别")
    @Column(nullable = false)
    @Index(name = "deviceCatetoryId")
    private Long deviceCatetoryId;

    @Desc("单位")
    @Column
    private String deviceUnit;

    @Desc("数量")
    @Column(nullable = false)
    private Double deviceCount;

    @Desc("供应商")
    @Column
    private String deviceSupplier;

    @Desc("使用时间")
    @Column(nullable = false)
    private Date deviceUsingDate;

    @Desc("所属部门")
    @Column(nullable = false)
    private Long deviceDepartmentId;

    @Desc("规格型号")
    @Column(nullable = false)
    private String specModel;

    @Desc("机台屏幕ip")
    @Column(nullable = false)
    private String machineScreenIp;

    @Desc("机台屏幕名称")
    @Column(nullable = false)
    private String machineScreenName;

    @Desc("正在生产的订单号")
    @Column
    private String productionSaleorderCode;

    /**
     * **get**
     */
    public String getDeviceAssetCode() {
        return deviceAssetCode;
    }

    /**
     * **set**
     */
    public void setDeviceAssetCode(String deviceAssetCode) {
        this.deviceAssetCode = deviceAssetCode;
    }

    /**
     * **get**
     */
    public String getDeviceCode() {
        return deviceCode;
    }

    /**
     * **set**
     */
    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    /**
     * **get**
     */
    public String getDeviceName() {
        return deviceName;
    }

    /**
     * **set**
     */
    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }


    /**
     * **get**
     */
    public String getDeviceUnit() {
        return deviceUnit;
    }

    /**
     * **set**
     */
    public void setDeviceUnit(String deviceUnit) {
        this.deviceUnit = deviceUnit;
    }


    /**
     * **get**
     */
    public Double getDeviceCount() {
        return deviceCount;
    }

    /**
     * **set**
     */
    public void setDeviceCount(Double deviceCount) {
        this.deviceCount = deviceCount;
    }

    /**
     * **get**
     */
    public String getDeviceSupplier() {
        return deviceSupplier;
    }

    /**
     * **set**
     */
    public void setDeviceSupplier(String deviceSupplier) {
        this.deviceSupplier = deviceSupplier;
    }

    /**
     * **get**
     */
    public Date getDeviceUsingDate() {
        return deviceUsingDate;
    }

    /**
     * **set**
     */
    public void setDeviceUsingDate(Date deviceUsingDate) {
        this.deviceUsingDate = deviceUsingDate;
    }


    /**
     * **get**
     */
    public Long getDeviceCatetoryId() {
        return deviceCatetoryId;
    }

    /**
     * **set**
     */
    public void setDeviceCatetoryId(Long deviceCatetoryId) {
        this.deviceCatetoryId = deviceCatetoryId;
    }

    /**
     * **get**
     */
    public Long getDeviceDepartmentId() {
        return deviceDepartmentId;
    }

    /**
     * **set**
     */
    public void setDeviceDepartmentId(Long deviceDepartmentId) {
        this.deviceDepartmentId = deviceDepartmentId;
    }

    /**
     * **get**
     */
    public String getSpecModel() {
        return specModel;
    }

    /**
     * **set**
     */
    public void setSpecModel(String specModel) {
        this.specModel = specModel;
    }


    public String getMachineScreenIp() {
        return machineScreenIp;
    }

    public void setMachineScreenIp(String machineScreenIp) {
        this.machineScreenIp = machineScreenIp;
    }

    public String getMachineScreenName() {
        return machineScreenName;
    }

    public void setMachineScreenName(String machineScreenName) {
        this.machineScreenName = machineScreenName;
    }

    public String getProductionSaleorderCode() {
        return productionSaleorderCode;
    }

    public void setProductionSaleorderCode(String productionSaleorderCode) {
        this.productionSaleorderCode = productionSaleorderCode;
    }
}
