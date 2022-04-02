package com.bluebirdme.mes.platform.entity;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;
import org.xdemo.superutil.thirdparty.excel.Excel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author qianchen
 * @date 2020/05/21
 */

@Desc("异常信息")
@Entity
@Table(name = "platform_exception_message")
public class ExceptionMessage extends BaseEntity {
    @Excel(name = "类")
    @Column(name = "clazz", nullable = false)
    private String clazz;

    @Excel(name = "方法")
    @Column(name = "method", nullable = false)
    private String method;

    @Excel(name = "行号")
    @Column(name = "lineNumber", nullable = false)
    private Integer lineNumber;

    @Excel(name = "错误消息")
    @Column(name = "msg", nullable = false)
    private String msg;

    @Excel(name = "发生日期")
    @Column(name = "occurDate", nullable = false)
    private Date occurDate;

    public ExceptionMessage() {
    }

    public String getClazz() {
        return this.clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public String getMethod() {
        return this.method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Integer getLineNumber() {
        return this.lineNumber;
    }

    public void setLineNumber(Integer lineNumber) {
        this.lineNumber = lineNumber;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Date getOccurDate() {
        return this.occurDate;
    }

    public void setOccurDate(Date occurDate) {
        this.occurDate = occurDate;
    }
}
