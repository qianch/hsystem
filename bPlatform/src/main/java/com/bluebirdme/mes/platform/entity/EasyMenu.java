package com.bluebirdme.mes.platform.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author qianchen
 * @date 2020/05/21
 */

public class EasyMenu {
    private Long id;
    private String text;
    private String iconCls;
    private Map<String, String> attributes = new HashMap();
    private List<EasyMenu> children;

    public EasyMenu() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getIconCls() {
        return this.iconCls;
    }

    public void setIconCls(String iconCls) {
        this.iconCls = iconCls;
    }

    public Map<String, String> getAttributes() {
        return this.attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public List<EasyMenu> getChildren() {
        return this.children;
    }

    public void setChildren(List<EasyMenu> children) {
        this.children = children;
    }
}
