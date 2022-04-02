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
@Entity
@Table(name = "platform_log")
@Desc("系统日志")
public class Log extends BaseEntity {
    @Excel(name = "用户ID")
    @Column(name = "userId", nullable = false)
    private Long userId;

    @Excel(name = "用户名")
    @Column(name = "userName")
    private String userName;

    @Excel(name = "用户账户")
    @Column(name = "loginName", nullable = false)
    private String loginName;

    @Excel(name = "操作")
    @Column(name = "operate")
    private String operate;

    @Excel(name = "参数")
    @Column(name = "params")
    private String params;

    @Excel(name = "参数值")
    @Column(name = "paramsValue")
    private String paramsValue;

    @Excel(name = "记录日期")
    @Column(name = "logDate", nullable = false)
    private Date logDate;

    @Excel(name = "客户端IP")
    @Column(name = "ip")
    private String ip;

    @Excel(name = "客户端标识")
    @Column(name = "requestIdentity")
    private String requestIdentity;

    public Log() {
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLoginName() {
        return this.loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getOperate() {
        return this.operate;
    }

    public void setOperate(String operate) {
        this.operate = operate;
    }

    public String getParams() {
        return this.params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public Date getLogDate() {
        return this.logDate;
    }

    public void setLogDate(Date logDate) {
        this.logDate = logDate;
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getParamsValue() {
        return this.paramsValue;
    }

    public void setParamsValue(String paramsValue) {
        this.paramsValue = paramsValue;
    }

    public String getIp() {
        return this.ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getRequestIdentity() {
        return this.requestIdentity;
    }

    public void setRequestIdentity(String requestIdentity) {
        this.requestIdentity = requestIdentity;
    }
}
