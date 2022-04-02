package com.bluebirdme.mes.platform.entity;

import com.bluebirdme.mes.core.base.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author qianchen
 * @date 2020/05/21
 */

@Entity
@Table(name = "platform_notice")
public class Notice extends BaseEntity {
    @Column(nullable = false, name = "title")
    private String title;

    @Column(name = "inputTime")
    private Date inputTime;

    @Lob
    @Column(name = "content")
    private String content;

    @Lob
    @Column(name = "contentTxt")
    private String contentTxt;

    @Column(name = "userId")
    private Long userId;

    public Notice() {
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getContentTxt() {
        return this.contentTxt;
    }

    public void setContentTxt(String contentTxt) {
        this.contentTxt = contentTxt;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getInputTime() {
        return this.inputTime;
    }

    public void setInputTime(Date inputTime) {
        this.inputTime = inputTime;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
