package com.bluebirdme.mes.platform.entity;

import com.bluebirdme.mes.core.base.entity.BaseEntity;
import org.hibernate.annotations.Index;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

/**
 * @author qianchen
 * @date 2020/05/21
 */

@Entity
@Table(name = "platform_message")
public class Message extends BaseEntity {
    @Column(name = "content", columnDefinition = "text", nullable = false)
    private String content;

    @Index(name = "messageType")
    @Column(name = "messageType", nullable = false, length = 100)
    private String messageType;

    @Column(name = "attachment", length = 250)
    private String attachment;

    @Column(name = "link", length = 250)
    private String link;

    @Column(name = "createTime", nullable = false)
    private Date createTime;

    @Column(name = "fromUser", nullable = false)
    private Long fromUser;

    @Transient
    private String fromUserName;

    @Column(name = "toUser", nullable = false)
    private Long toUser;

    @Column(name = "uuid", nullable = false)
    private String uuid;

    public Message() {
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAttachment() {
        return this.attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public String getLink() {
        return this.link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getFromUser() {
        return this.fromUser;
    }

    public void setFromUser(Long fromUser) {
        this.fromUser = fromUser;
    }

    public Long getToUser() {
        return this.toUser;
    }

    public void setToUser(Long toUser) {
        this.toUser = toUser;
    }

    public String getUuid() {
        return this.uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getFromUserName() {
        return this.fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public String getMessageType() {
        return this.messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }
}
