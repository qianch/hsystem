package com.bluebirdme.mes.platform.entity;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;
import com.bluebirdme.mes.core.valid.annotations.Length;
import com.bluebirdme.mes.core.valid.annotations.NotBlank;
import com.bluebirdme.mes.core.valid.annotations.Valid;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author qianchen
 * @date 2020/05/21
 */
@Entity
@Table(name = "platform_department")
@Desc("部门")
@Valid
public class Department extends BaseEntity {
    @Desc("部门编码")
    @Column(name = "code", nullable = false)
    private String code;

    @NotBlank
    @Length(min = 1, max = 60)
    @Desc("部门名称")
    @Column(name = "name", nullable = false)
    private String name;

    @Desc("排序")
    @Column(name = "sort")
    private Integer sort;

    @Desc("部门父级ID")
    @Column(name = "pid")
    private Long pid;

    @Desc("计划单号前缀")
    @Column(name = "prefix")
    private String prefix;

    @Desc("部门类型")
    @Column(name = "type")
    private String type;

    public Department() {
    }

    public Long getPid() {
        return this.pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSort() {
        return this.sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    /**
     * 计划单号前缀
     *
     * @return
     */
    public String getPrefix() {
        return this.prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    /**
     * 部门类型
     *
     * @return
     */
    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
