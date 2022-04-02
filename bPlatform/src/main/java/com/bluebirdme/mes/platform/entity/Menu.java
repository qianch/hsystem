package com.bluebirdme.mes.platform.entity;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;
import com.bluebirdme.mes.core.valid.annotations.NotBlank;
import com.bluebirdme.mes.core.valid.annotations.NotNull;
import com.bluebirdme.mes.core.valid.annotations.Valid;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author qianchen
 * @date 2020/05/21
 */

@Entity
@Table(name = "platform_menu")
@Desc("菜单权限")
@DynamicInsert
@Valid
public class Menu extends BaseEntity {
    @NotBlank
    @Desc("菜单名称")
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Desc("菜单代码")
    @Column(name = "code")
    private String code;

    @Desc("父级ID")
    @Column(name = "parentId")
    private Long parentId;

    @Desc("菜单URL")
    @Column(name = "url")
    private String url;

    @Desc("图标")
    @Column(name = "icon")
    private String icon;

    @Desc("是否按钮")
    @Column(name = "isButton", nullable = false, columnDefinition = "int default 0")
    private Integer isButton;

    @Desc("按钮排序")
    @Column(name = "sortOrder")
    private Integer sortOrder;

    @Desc("按钮编码")
    @Column(name = "buttonCode")
    private String buttonCode;

    @Column(name = "levelCount")
    @Desc("层级")
    private Integer levelCount;

    public Menu() {
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIcon() {
        return this.icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Long getParentId() {
        return this.parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Integer getIsButton() {
        return this.isButton;
    }

    public void setIsButton(Integer isButton) {
        this.isButton = isButton;
    }

    public String getButtonCode() {
        return this.buttonCode;
    }

    public void setButtonCode(String buttonCode) {
        this.buttonCode = buttonCode;
    }

    public Integer getLevelCount() {
        return this.levelCount;
    }

    public void setLevelCount(Integer levelCount) {
        this.levelCount = levelCount;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getSortOrder() {
        return this.sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }
}
