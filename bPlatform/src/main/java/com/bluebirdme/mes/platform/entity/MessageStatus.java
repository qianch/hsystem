package com.bluebirdme.mes.platform.entity;

import com.bluebirdme.mes.core.base.entity.BaseEntity;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author qianchen
 * @date 2020/05/21
 */
@Entity
@Table(name = "platform_message_status")
@DynamicInsert
public class MessageStatus extends BaseEntity {
    @Column(name = "userId", nullable = false)
    private Long userId;

    @Column(name = "msgid", nullable = false)
    private Long msgid;

    @Column(name = "isDeleted", columnDefinition = "int default 0")
    private Integer isDeleted;

    public MessageStatus() {
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getMsgid() {
        return this.msgid;
    }

    public void setMsgid(Long msgid) {
        this.msgid = msgid;
    }

    public Integer getIsDeleted() {
        return this.isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }
}
