package com.bluebirdme.mes.platform.controller;

import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.annotation.NoAuth;
import com.bluebirdme.mes.core.annotation.support.LogType;
import com.bluebirdme.mes.core.base.controller.BaseController;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.constant.RuntimeVariable;
import com.bluebirdme.mes.platform.entity.Role;
import com.bluebirdme.mes.platform.service.IMenuService;
import com.bluebirdme.mes.platform.service.IRoleService;
import com.google.gson.GsonBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author qianchen
 * @date 2020/05/21
 */

@Journal(name = "角色模块")
@Controller
@RequestMapping({"/role"})
public class RoleController extends BaseController {
    @Resource
    IRoleService roleService;
    @Resource
    IMenuService menuService;

    public RoleController() {
    }

    @Journal(name = "访问角色模块")
    @RequestMapping
    public String index() {
        return "platform/role";
    }

    @NoAuth
    @Journal(name = "查询角色列表")
    @ResponseBody
    @RequestMapping({"list"})
    public String list(Filter filter, Page page) throws Exception {
        return (new GsonBuilder()).serializeNulls().create().toJson(roleService.findPageInfo(filter, page));
    }

    @Journal(name = "新增角色页面")
    @RequestMapping(value = {"add"}, method = {RequestMethod.GET})
    public ModelAndView _add(Role role) throws Exception {
        return new ModelAndView("platform/roleAddOrEdit",
                model.addAttribute("role", role)
                        .addAttribute("action", "add"));
    }

    @ResponseBody
    @Journal(name = "保存角色", logType = LogType.DB)
    @RequestMapping(value = {"add"}, method = {RequestMethod.POST})
    public String add(Role role) throws Exception {
        Map<String, Object> map = new HashMap();
        map.put("name", role.getName());
        if (roleService.isExist(Role.class, map)) {
            throw new Exception("角色名称重复:" + role.getName());
        } else {
            roleService.save(new Object[]{role});
            return "{}";
        }
    }

    @Journal(name = "编辑角色页面")
    @RequestMapping(value = {"edit"}, method = {RequestMethod.GET})
    public ModelAndView _edit(Role role) throws Exception {
        role = roleService.findById(Role.class, role.getId());
        if (role == null) {
            throw new Exception("未找到该角色");
        } else {
            return new ModelAndView("platform/roleAddOrEdit",
                    model.addAttribute("role", role)
                            .addAttribute("action", "edit"));
        }
    }

    @ResponseBody
    @Journal(name = "编辑角色", logType = LogType.DB)
    @RequestMapping(value = {"edit"}, method = {RequestMethod.POST})
    public String edit(Role role) throws Exception {
        Role _role = roleService.findById(Role.class, role.getId());
        Map<String, Object> map = new HashMap<>();
        map.put("name", role.getName());
        if (!_role.getName().equals(role.getName()) && this.roleService.isExist(Role.class, map)) {
            throw new Exception("角色名称重复:" + role.getName());
        } else {
            this.roleService.update2(role);
            return "{}";
        }
    }

    @Journal(name = "删除角色", logType = LogType.DB)
    @ResponseBody
    @RequestMapping(value = {"delete"}, method = {RequestMethod.DELETE})
    public String delete(@RequestBody List<Role> list) {
        this.roleService.delete(list);
        return "{}";
    }

    @Journal(name = "角色配置界面")
    @RequestMapping(value = {"permission"}, method = {RequestMethod.GET})
    public ModelAndView _permission(Role role) throws Exception {
        List<String> ids = roleService.getPermissionByRole(role);
        return new ModelAndView("platform/rolePermission", model.addAttribute("ids", ids));
    }

    @NoAuth
    @Journal(name = "查询菜单列表")
    @ResponseBody
    @RequestMapping({"permission/list"})
    public String permissionList(Filter filter, Page page) throws Exception {
        return (new GsonBuilder()).serializeNulls().create().toJson(this.menuService.findPageInfo(filter, page));
    }

    @ResponseBody
    @Journal(name = "修改角色权限", logType = LogType.DB)
    @RequestMapping(value = {"permission"}, method = {RequestMethod.POST})
    public String permission(Role role, String ids) {
        RuntimeVariable.RUNTIME_VERSION = UUID.randomUUID().toString();
        roleService.savePermission(role, ids);
        return "{}";
    }
}
