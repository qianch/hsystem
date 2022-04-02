package com.bluebirdme.mes.btwManager.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Index;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;

@Desc("btw文件")
@Entity
@Table(name = "hs_btwfile")
public class BtwFile extends BaseEntity {
    @Desc("适用客户")
    @Column(name = "consumerId", nullable = false)
    private Long consumerId;

    @Desc("适用客户名称")
    @Index(name = "consumerName")
    @Column(name = "consumerName", nullable = false)
    private String consumerName;

    @Desc("适用客户编码")
    @Index(name = "consumerCode")
    @Column(name = "consumerCode", nullable = false)
    private String consumerCode;

    @Desc("标签名称")
    @Column(name = "tagName")
    private String tagName;

    @Desc("标签类型")
    @Column(name = "tagType")
    private String tagType;

    @Desc("上传人")
    @Column(name = "uploadUser")
    private String uploadUser;

    @Desc("上传时间")
    @Column(name = "uploadDate")
    private Date uploadDate;

    @Desc("客户条码前缀")
    @Column(name = "consumerBarCodePrefix")
    private String consumerBarCodePrefix;

    @Desc("客户条码记录")
    @Column(name = "consumerBarCodeRecord")
    private Integer consumerBarCodeRecord;

    @Desc("客户条码位数")
    @Column(name = "consumerBarCodeDigit")
    private Integer consumerBarCodeDigit;

    @Desc("供销商条码前缀")
    @Column(name = "agentBarCodePrefix")
    private String agentBarCodePrefix;

    @Desc("供销商条码记录")
    @Column(name = "agentBarCodeRecord")
    private Integer agentBarCodeRecord;

    @Desc("供销商条码位数")
    @Column(name = "agentBarCodeDigit")
    private Integer agentBarCodeDigit;

    @Desc("标签实际名称")
    @Column(name = "tagActName")
    private String tagActName;

    @Desc("状态")
    @Column(name = "state")
    private Integer state;


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


    @Desc("标签打印属性")
    @Transient
    private List<BtwFilePrint> listBtwFilePrint;

    public String getUploadUser() {
        return uploadUser;
    }

    public void setUploadUser(String uploadUser) {
        this.uploadUser = uploadUser;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    public Long getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(Long consumerId) {
        this.consumerId = consumerId;
    }

    public String getConsumerName() {
        return consumerName;
    }

    public void setConsumerName(String consumerName) {
        this.consumerName = consumerName;
    }

    public String getConsumerCode() {
        return consumerCode;
    }

    public void setConsumerCode(String consumerCode) {
        this.consumerCode = consumerCode;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getTagType() {
        return tagType;
    }

    public void setTagType(String tagType) {
        this.tagType = tagType;
    }

    public String getConsumerBarCodePrefix() {
        return consumerBarCodePrefix;
    }

    public void setConsumerBarCodePrefix(String consumerBarCodePrefix) {
        this.consumerBarCodePrefix = consumerBarCodePrefix;
    }

    public Integer getConsumerBarCodeRecord() {
        return consumerBarCodeRecord;
    }

    public void setConsumerBarCodeRecord(Integer consumerBarCodeRecord) {
        this.consumerBarCodeRecord = consumerBarCodeRecord;
    }

    public Integer getConsumerBarCodeDigit() {
        return consumerBarCodeDigit;
    }

    public void setConsumerBarCodeDigit(Integer consumerBarCodeDigit) {
        this.consumerBarCodeDigit = consumerBarCodeDigit;
    }

    public String getAgentBarCodePrefix() {
        return agentBarCodePrefix;
    }

    public void setAgentBarCodePrefix(String agentBarCodePrefix) {
        this.agentBarCodePrefix = agentBarCodePrefix;
    }

    public Integer getAgentBarCodeRecord() {
        return agentBarCodeRecord;
    }

    public void setAgentBarCodeRecord(Integer agentBarCodeRecord) {
        this.agentBarCodeRecord = agentBarCodeRecord;
    }

    public Integer getAgentBarCodeDigit() {
        return agentBarCodeDigit;
    }

    public void setAgentBarCodeDigit(Integer agentBarCodeDigit) {
        this.agentBarCodeDigit = agentBarCodeDigit;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getTagActName() {
        return tagActName;
    }

    public void setTagActName(String tagActName) {
        this.tagActName = tagActName;
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

    public List<BtwFilePrint> getListBtwFilePrint() {
        return listBtwFilePrint;
    }

    public void setListBtwFilePrint(List<BtwFilePrint> listBtwFilePrint) {
        this.listBtwFilePrint = listBtwFilePrint;
    }
}
