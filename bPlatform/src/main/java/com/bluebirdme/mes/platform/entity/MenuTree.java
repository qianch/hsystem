package com.bluebirdme.mes.platform.entity;

import java.util.List;

/**
 * @author qianchen
 * @date 2020/05/21
 */
public class MenuTree {
    private Long id;
    private Boolean leaf;
    private Long parent;
    private String text;
    private String icon;
    private String url;
    private List<MenuTree> children;

    public MenuTree() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getLeaf() {
        return this.leaf;
    }

    public void setLeaf(Boolean leaf) {
        this.leaf = leaf;
    }

    public Long getParent() {
        return this.parent;
    }

    public void setParent(Long parent) {
        this.parent = parent;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getIcon() {
        return this.icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<MenuTree> getChildren() {
        return this.children;
    }

    public void setChildren(List<MenuTree> children) {
        this.children = children;
    }
}
