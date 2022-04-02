package com.bluebirdme.mes.platform.entity;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @author qianchen
 * @date 2020/05/21
 */
@Desc("字典")
@Entity
@Table(name = "PLATFORM_DICT")
public class Dict extends BaseEntity {
    @Desc("字典根条目代码")
    @Column(name = "rootCode")
    private String rootCode;

    @Desc("字典代码")
    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @Desc("字典名称")
    @Column(name = "name_zh_CN", nullable = false, unique = true)
    private String name_zh_CN;

    @Desc("字典名称")
    @Column(name = "name_en_US", unique = true)
    private String name_en_US;

    @Desc("字典名称")
    @Column(name = "name_th_TH", unique = true)
    private String name_th_TH;

    @Desc("字典状态")
    @Column(name = "deprecated", nullable = false)
    private Integer deprecated;

    @Desc("字典阿拉伯名称")
    @Column(name = "name_Ar_EG")
    private String name_Ar_EG;

    @Transient
    private String children;

    @Desc("父级ID")
    @Column(name = "pid")
    private Long pid;

    @Desc("排序")
    @Column(name = "sort")
    private Integer sort;

    public Dict() {
    }

    public String getRootCode() {
        return this.rootCode;
    }

    public void setRootCode(String rootCode) {
        this.rootCode = rootCode;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getDeprecated() {
        return this.deprecated;
    }

    public void setDeprecated(Integer deprecated) {
        this.deprecated = deprecated;
    }

    public String getChildren() {
        return this.children;
    }

    public void setChildren(String children) {
        this.children = children;
    }

    public Long getPid() {
        return this.pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public String getName_zh_CN() {
        return this.name_zh_CN;
    }

    public void setName_zh_CN(String name_zh_CN) {
        this.name_zh_CN = name_zh_CN;
    }

    public String getName_en_US() {
        return this.name_en_US;
    }

    public void setName_en_US(String name_en_US) {
        this.name_en_US = name_en_US;
    }

    public String getName_th_TH() {
        return this.name_th_TH;
    }

    public void setName_th_TH(String name_th_TH) {
        this.name_th_TH = name_th_TH;
    }
}
