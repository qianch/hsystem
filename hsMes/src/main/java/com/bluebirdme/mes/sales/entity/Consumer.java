package com.bluebirdme.mes.sales.entity;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Index;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Desc("客户管理")
@Entity
@Table(name = "hs_consumer")
@DynamicInsert
public class Consumer extends BaseEntity {
    @Desc("客户代码")
    @Column(nullable = false)
    @Index(name = "consumerCode")
    private String consumerCode;

    @Desc("客户名称")
    @Column(nullable = false)
    @Index(name = "consumerName")
    private String consumerName;

    @Desc("客户简称")
    @Column(nullable = true)
    @Index(name = "consumerSimpleName")
    private String consumerSimpleName;

    @Desc("客户等级")
    @Column(nullable = false)
    @Index(name = "consumerGrade")
    private Integer consumerGrade;

    @Desc("客户大类")
    @Column(nullable = false)
    @Index(name = "consumerCategory")
    private Integer consumerCategory;

    @Desc("客户联系人")
    @Column(nullable = false)
    private String consumerContact;

    @Desc("客户地址")
    @Column(nullable = false, length = 1000)
    private String consumerAddress;

    @Desc("联系电话")
    @Column(nullable = false)
    private String consumerPhone;

    @Desc("邮件地址")
    @Column(nullable = false)
    private String consumerEmail;

    @Desc("备注")
    @Column(nullable = false)
    @Index(name = "consumerMemo")
    private String consumerMemo;

    @Desc("客户ERP代码")
    @Column(nullable = false)
    @Index(name = "consumerCodeErp")
    private String consumerCodeErp;

    @Desc("作废状态")
    private Integer isCancellation;

    public String getConsumerCode() {
        return consumerCode;
    }

    public void setConsumerCode(String consumerCode) {
        this.consumerCode = consumerCode;
    }

    public String getConsumerName() {
        return consumerName;
    }

    public void setConsumerName(String consumerName) {
        this.consumerName = consumerName;
    }

    public Integer getConsumerGrade() {
        return consumerGrade;
    }

    public void setConsumerGrade(Integer consumerGrade) {
        this.consumerGrade = consumerGrade;
    }

    public Integer getConsumerCategory() {
        return consumerCategory;
    }

    public void setConsumerCategory(Integer consumerCategory) {
        this.consumerCategory = consumerCategory;
    }

    public String getConsumerContact() {
        return consumerContact;
    }

    public void setConsumerContact(String consumerContact) {
        this.consumerContact = consumerContact;
    }

    public String getConsumerAddress() {
        return consumerAddress;
    }

    public void setConsumerAddress(String consumerAddress) {
        this.consumerAddress = consumerAddress;
    }

    public String getConsumerPhone() {
        return consumerPhone;
    }

    public void setConsumerPhone(String consumerPhone) {
        this.consumerPhone = consumerPhone;
    }

    public String getConsumerEmail() {
        return consumerEmail;
    }

    public void setConsumerEmail(String consumerEmail) {
        this.consumerEmail = consumerEmail;
    }

    public String getConsumerMemo() {
        return consumerMemo;
    }

    public void setConsumerMemo(String consumerMemo) {
        this.consumerMemo = consumerMemo;
    }

    public String getConsumerCodeErp() {
        return consumerCodeErp;
    }

    public void setConsumerCodeErp(String consumerCodeErp) {
        this.consumerCodeErp = consumerCodeErp;
    }

    public String getConsumerSimpleName() {
        return consumerSimpleName;
    }

    public void setConsumerSimpleName(String consumerSimpleName) {
        this.consumerSimpleName = consumerSimpleName;
    }

    public Integer getIsCancellation() {
        return isCancellation;
    }

    public void setIsCancellation(Integer isCancellation) {
        this.isCancellation = isCancellation;
    }
}
