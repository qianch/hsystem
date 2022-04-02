package com.bluebirdme.mes.platform.entity;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author qianchen
 * @date 2020/05/21
 */
@Entity
@Table(name = "platform_subscription")
public class Subscription extends BaseEntity {
    @Desc("用户id")
    @Column(nullable = false)
    private Long userId;

    @Desc("消息类型")
    @Column(nullable = false)
    private String messageType;

    @Desc("订阅时间")
    @Column(nullable = false)
    private Date subDate;

    public Subscription() {
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getMessageType() {
        return this.messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public Date getSubDate() {
        return this.subDate;
    }

    public void setSubDate(Date subDate) {
        this.subDate = subDate;
    }
}
