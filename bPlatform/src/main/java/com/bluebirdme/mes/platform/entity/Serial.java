package com.bluebirdme.mes.platform.entity;

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
@Table(name = "Platform_Serial")
public class Serial extends BaseEntity {
    @Column(name = "preffix", nullable = false, unique = true)
    private String preffix;

    @Column(name = "maxNumber", nullable = false)
    private Integer maxNumber;

    @Column(name = "lastUpdateTime", nullable = false)
    private Date lastUpdateTime;

    public Serial() {
    }

    public String getPreffix() {
        return this.preffix;
    }

    public void setPreffix(String preffix) {
        this.preffix = preffix;
    }

    public Integer getMaxNumber() {
        return this.maxNumber;
    }

    public void setMaxNumber(Integer maxNumber) {
        this.maxNumber = maxNumber;
    }

    public Date getLastUpdateTime() {
        return this.lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }
}
