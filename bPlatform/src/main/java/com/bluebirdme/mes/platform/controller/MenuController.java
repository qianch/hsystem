package com.bluebirdme.mes.platform.controller;

import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.annotation.NoAuth;
import com.bluebirdme.mes.core.base.controller.BaseController;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.constant.RuntimeVariable;
import com.bluebirdme.mes.core.valid.annotations.Valid;
import com.bluebirdme.mes.platform.entity.Menu;
import com.bluebirdme.mes.platform.service.IMenuService;
import com.google.gson.GsonBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.xdemo.superutil.j2se.ListUtils;
import org.xdemo.superutil.thirdparty.gson.GsonKeyRename;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author qianchen
 * @date 2020/05/21
 */
@Journal(name = "菜单模块")
@Controller
@RequestMapping({"/menu"})
public class MenuController extends BaseController {
    @Resource
    IMenuService menuService;

    public MenuController() {
    }

    @Journal(name = "访问菜单模块")
    @RequestMapping
    public String index() {
        return "platform/menu";
    }

    @NoAuth
    @Journal(name = "查询菜单列表")
    @ResponseBody
    @RequestMapping({"list"})
    public String list(Filter filter, Page page) throws Exception {
        return (new GsonBuilder()).setFieldNamingStrategy(new GsonKeyRename(new HashMap() {
            {
                this.put("url", "_url");
                this.put("icon", "_icon");
            }
        })).create().toJson(this.menuService.findPageInfo(filter, page));
    }

    @Journal(name = "新增菜单页面")
    @RequestMapping(value = {"add"}, method = {RequestMethod.GET})
    public ModelAndView _add(Menu menu) {
        if (menu.getParentId() == null) {
            menu.setCode(null);
        } else {
            menu.setCode(this.menuService.getMenuCode(menu.getParentId()));
        }

        return new ModelAndView(
                "platform/menuAddOrEdit",
                model.addAttribute("menu", menu)
                        .addAttribute("action", "add"));
    }

    @ResponseBody
    @Journal(name = "保存新增的菜单")
    @RequestMapping(value = {"add"}, method = {RequestMethod.POST})
    @Valid
    public String add(Menu menu) {
        menuService.save(new Object[]{menu});
        return (new GsonBuilder()).setFieldNamingStrategy(new GsonKeyRename(new HashMap() {
            {
                this.put("url", "_url");
                this.put("icon", "_icon");
            }
        })).create().toJson(menu);
    }

    @Journal(name = "编辑菜单页面")
    @RequestMapping(value = {"edit"}, method = {RequestMethod.GET})
    public ModelAndView _edit(Menu menu) throws Exception {
        menu = this.menuService.findById(Menu.class, menu.getId());
        if (menu == null) {
            throw new Exception("未找到该菜单");
        } else {
            String title = null;
            return new ModelAndView(
                    "platform/menuAddOrEdit",
                    model.addAttribute("menu", menu)
                            .addAttribute("title", title)
                            .addAttribute("action", "edit"));
        }
    }

    @ResponseBody
    @Journal(name = "保存编辑的菜单")
    @RequestMapping(value = {"edit"}, method = {RequestMethod.POST})
    public String edit(Menu menu) throws Exception {
        menuService.update2(new Object[]{menu});
        return (new GsonBuilder()).serializeNulls().create().toJson(menu);
    }

    @Journal(name = "菜单排序")
    @ResponseBody
    @RequestMapping(value = {"sort"}, method = {RequestMethod.POST})
    public String sort(@RequestBody List<Menu> list) throws Exception {
        menuService.update2(list.toArray(new Menu[0]));
        return "{}";
    }

    @Journal(name = "删除菜单")
    @ResponseBody
    @RequestMapping(value = {"delete"}, method = {RequestMethod.DELETE})
    public String delete(@RequestBody List<Menu> list) {
        menuService.batchDelete(list);
        return "{}";
    }

    @NoAuth
    @Journal(name = "查询菜单列表")
    @ResponseBody
    @RequestMapping({"mymenu"})
    public String mymenu() throws Exception {
        if (session.getAttribute("userMenus") != null && RuntimeVariable.RUNTIME_VERSION.equals(session.getAttribute("currentRuntimeVersion"))) {
            return (String) session.getAttribute("userMenus");
        } else {
            List<Menu> list = menuService.myMenu((Long) session.getAttribute("userId"));
            return ListUtils.isEmpty(list) ? "[]" : GsonTools.toJson(getMenuTree(list));
        }
    }

    public Map<String, Object> getMenuTree(List<Menu> list) {
        Map<String, Object> map = new HashMap();
        Map<String, String> attributes = new HashMap();
        Iterator iterator = list.iterator();

        while (iterator.hasNext()) {
            Menu menu = (Menu) iterator.next();
            if (menu.getParentId() == null) {
                map.put("iconCls", menu.getIcon());
                map.put("id", menu.getId());
                map.put("text", menu.getName());
                attributes.put("url", menu.getUrl());
                map.put("attributes", attributes);
                map.put("children", children(menu, list));
                break;
            }
        }
        return map;
    }

    public List<Map<String, Object>> children(Menu parent, List<Menu> list) {
        List<Map<String, Object>> mtl = new ArrayList();
        Iterator iterator = list.iterator();

        while (iterator.hasNext()) {
            Menu menu = (Menu) iterator.next();
            if (menu.getParentId() != null && menu.getParentId().equals(parent.getId()) && menu.getIsButton() != 1) {
                Map<String, Object> map = new HashMap();
                Map<String, String> attributes = new HashMap();
                map.put("iconCls", menu.getIcon());
                map.put("id", menu.getId());
                map.put("text", menu.getName());
                attributes.put("url", menu.getUrl());
                map.put("attributes", attributes);
                map.put("children", children(menu, list));
                mtl.add(map);
            }
        }
        return mtl;
    }
}
