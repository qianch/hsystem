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
@Table(name = "platform_user")
@Desc("人员信息")
public class User extends BaseEntity {
    @Desc("用户拼音首字母")
    @Column(name = "pinYinIndex")
    public String pinYinIndex;

    @Desc("用户姓名")
    @Column(name = "userName", nullable = false)
    public String userName;

    @Desc("用户编码")
    @Column(name = "userCode", nullable = false)
    public String userCode;

    @Desc("登录帐号")
    @Column(name = "loginName", nullable = false)
    public String loginName;

    @Desc("登录密码")
    @Column(name = "password", nullable = true)
    public String password;

    @Desc("性别")
    @Column(name = "sex", nullable = false)
    public Integer sex;

    @Desc("邮箱")
    @Column(name = "email")
    public String email;

    @Desc("联系电话")
    @Column(name = "phone")
    public String phone;

    @Desc("创建时间")
    @Column(name = "createTime", nullable = false)
    public Date createTime;

    @Desc("状态")
    @Column(name = "status", nullable = false)
    public Integer status;

    @Desc("部门ID")
    @Column(name = "did", nullable = false)
    public Long did;

    public User() {
    }

    public Long getDid() {
        return this.did;
    }

    public void setDid(Long did) {
        this.did = did;
    }

    public String getPinYinIndex() {
        return this.pinYinIndex;
    }

    public void setPinYinIndex(String pinYinIndex) {
        this.pinYinIndex = pinYinIndex;
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

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getSex() {
        return this.sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getUserCode() {
        return this.userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }
}
