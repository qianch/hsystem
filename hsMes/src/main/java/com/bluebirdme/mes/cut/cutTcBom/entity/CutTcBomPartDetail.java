package com.bluebirdme.mes.cut.cutTcBom.entity;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Index;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Desc("裁片明细")
@Entity
@Table(name = "hs_cut_tc_proc_bom_part_detail")
@DynamicInsert
public class CutTcBomPartDetail extends BaseEntity {
    @Desc("BOM主表ID")
    @Column(nullable = false)
    @Index(name = "tcBomMainId")
    private Long tcBomMainId;

    @Desc("裁片主表ID")
    @Column(nullable = false)
    @Index(name = "mainId")
    private Long mainId;

    @Desc("裁片明细/层")
    private String cutNameLayNo;

    @Desc("裁片层号")
    private Integer layNo;

    @Desc("卷/套")
    private Integer amount;

    @Desc("包装顺序")
    private Integer packSequence;

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
     * 裁片主表ID
     *
     * @return
     */
    public Long getMainId() {
        return mainId;
    }

    public void setMainId(Long mainId) {
        this.mainId = mainId;
    }

    /**
     * 裁片明细名称
     *
     * @return
     */
    public String getCutNameLayNo() {
        return cutNameLayNo;
    }

    public void setCutNameLayNo(String cutNameLayNo) {
        this.cutNameLayNo = cutNameLayNo;
    }

    /**
     * 层号
     *
     * @return
     */
    public Integer getLayNo() {
        return layNo;
    }

    public void setLayNo(Integer layNo) {
        this.layNo = layNo;
    }

    /**
     * 卷/套
     *
     * @return
     */
    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    /**
     * 包装顺序
     *
     * @return
     */
    public Integer getPackSequence() {
        return packSequence;
    }

    public void setPackSequence(Integer packSequence) {
        this.packSequence = packSequence;
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
