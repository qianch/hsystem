package com.bluebirdme.mes.cut.cutTcBom.entity;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Index;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Desc("裁片名称")
@Entity
@Table(name = "hs_cut_tc_proc_bom_part_main")
@DynamicInsert
public class CutTcBomPartMain extends BaseEntity {
    @Desc("BOM主表ID")
    @Column(nullable = false)
    @Index(name = "tcBomMainId")
    private Long tcBomMainId;

    @Desc("部件名称")
    private String partName;

    @Desc("胚布规格")
    private String productModel;

    @Desc("裁片名称")
    private String cutName;

    @Desc("层数")
    private Integer layerNum;

    @Desc("备注")
    private String remark;

    @Desc("创建人")
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

    /**
     * 派工套材主键
     *
     * @return
     */
    public Long getTcBomMainId() {
        return tcBomMainId;
    }

    public void setTcBomMainId(Long tcBomMainId) {
        this.tcBomMainId = tcBomMainId;
    }


    /**
     * 部件名称
     *
     * @return
     */
    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    /**
     * 胚布规格
     *
     * @return
     */
    public String getProductModel() {
        return productModel;
    }

    public void setProductModel(String productModel) {
        this.productModel = productModel;
    }

    /**
     * 裁片名称
     *
     * @return
     */
    public String getCutName() {
        return cutName;
    }

    public void setCutName(String cutName) {
        this.cutName = cutName;
    }

    /**
     * 层数
     *
     * @return
     */
    public Integer getLayerNum() {
        return layerNum;
    }

    public void setLayerNum(Integer layerNum) {
        this.layerNum = layerNum;
    }

    /**
     * 备注
     *
     * @return
     */
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * 创建人
     *
     * @return
     */
    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    /**
     * 创建时间
     *
     * @return
     */
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 修改人
     *
     * @return
     */
    public String getModifyUser() {
        return modifyUser;
    }

    public void setModifyUser(String modifyUser) {
        this.modifyUser = modifyUser;
    }

    /**
     * 修改时间
     *
     * @return
     */
    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }
}
