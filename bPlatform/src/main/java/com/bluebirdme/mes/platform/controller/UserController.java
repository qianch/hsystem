package com.bluebirdme.mes.platform.controller;

import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.annotation.NoAuth;
import com.bluebirdme.mes.core.annotation.NoLogin;
import com.bluebirdme.mes.core.annotation.support.LogType;
import com.bluebirdme.mes.core.base.controller.BaseController;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.platform.entity.Department;
import com.bluebirdme.mes.platform.entity.User;
import com.bluebirdme.mes.platform.entity.UserRole;
import com.bluebirdme.mes.platform.service.IDepartmentService;
import com.bluebirdme.mes.platform.service.IRoleService;
import com.bluebirdme.mes.platform.service.IUserService;
import com.google.gson.GsonBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.xdemo.superutil.j2se.MD5Utils;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import javax.annotation.Resource;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author qianchen
 * @date 2020/05/21
 */
@Journal(name = "用户模块")
@Controller
@RequestMapping({"/user"})
public class UserController extends BaseController {
    @Resource
    IUserService userService;
    @Resource
    IDepartmentService departmentService;
    @Resource
    IRoleService roleService;

    public UserController() {
    }

    @Journal(name = "访问用户模块")
    @RequestMapping
    public String index() {
        return "platform/user";
    }

    @NoAuth
    @ResponseBody
    @Journal(name = "获取用户列表信息")
    @RequestMapping({"list"})
    public String getUserList(Filter filter, Page page) throws Exception {
        return (new GsonBuilder()).serializeNulls().create().toJson(userService.findPageInfo(filter, page));
    }

    @Journal(name = "跳转部门选择页面")
    @RequestMapping({"selectDepartment"})
    public ModelAndView selectDepartment(Department department) {
        department = department.getId() == null ? null : departmentService.findById(Department.class, department.getId());
        return new ModelAndView("platform/selectDepartment", this.model.addAttribute("dept", department));
    }

    @Journal(name = "添加user页面")
    @RequestMapping(value = {"add"}, method = {RequestMethod.GET})
    public ModelAndView _add(User user) {
        Department department = null;
        if (user.getDid() != null) {
            department = departmentService.findById(Department.class, user.getDid());
        }
        return new ModelAndView("platform/userAddOrEdit",
                model.addAttribute("user", user)
                        .addAttribute("action", "add")
                        .addAttribute("dept", department));
    }

    @ResponseBody
    @Journal(name = "保存用户", logType = LogType.DB)
    @RequestMapping(value = {"add"}, method = {RequestMethod.POST})
    public String add(User user) throws Exception {
        user.setPassword(MD5Utils.getStringMD5("123456", "6d02d09506f651a26bdc3fef63494e5b"));
        user.setCreateTime(new Date());
        Map<String, Object> map = new HashMap<>();
        map.put("loginName", user.getLoginName());
        if (this.userService.isExist(User.class, map)) {
            throw new Exception("登录帐号重复:" + user.getLoginName());
        } else {
            this.userService.save(new Object[]{user});
            return "{}";
        }
    }

    @Journal(name = "编辑user页面")
    @RequestMapping(value = {"edit"}, method = {RequestMethod.GET})
    public ModelAndView _edit(User user) {
        user = userService.findById(User.class, user.getId());
        Department department;
        department = departmentService.findById(Department.class, user.getDid());
        return new ModelAndView("platform/userAddOrEdit",
                model.addAttribute("user", user)
                        .addAttribute("action", "edit")
                        .addAttribute("dept", department));
    }

    @ResponseBody
    @Journal(name = "编辑用户", logType = LogType.DB)
    @RequestMapping(value = {"edit"}, method = {RequestMethod.POST})
    public String edit(User user) throws Exception {
        User _user = userService.findById(User.class, user.getId());
        Map<String, Object> map = new HashMap<>();
        map.put("loginName", user.getLoginName());
        if (!_user.getLoginName().equals(user.getLoginName()) && userService.isExist(User.class, map)) {
            throw new Exception("登录账户重复:" + user.getLoginName());
        } else {
            this.userService.update2(new Object[]{user});
            return "{}";
        }
    }

    @ResponseBody
    @Journal(name = "删除用户", logType = LogType.DB)
    @RequestMapping({"delete"})
    public String delete(@RequestBody List<User> list) throws Exception {
        this.userService.delete(list);
        return "{}";
    }

    @Journal(name = "编辑用户角色页面")
    @RequestMapping(value = {"role"}, method = {RequestMethod.GET})
    public ModelAndView _role(User user) {
        StringBuilder buffer = new StringBuilder();
        if (user != null && user.getId() != null) {
            Map<String, Object> map = new HashMap<>();
            map.put("userId", user.getId());
            List<UserRole> list = this.roleService.findListByMap(UserRole.class, map);

            for (int i = 0; i < list.size(); ++i) {
                buffer.append(i == 0 ? "" : ",").append(list.get(i).getRoleId());
            }
        }
        return new ModelAndView("platform/userRole", this.model.addAttribute("roles", buffer.toString()));
    }

    @ResponseBody
    @Journal(name = "设置用户角色", logType = LogType.DB)
    @RequestMapping(value = {"role"}, method = {RequestMethod.POST})
    public String role(Long[] uids, Long[] rids) {
        userService.saveRole(uids, rids);
        return "{}";
    }

    @ResponseBody
    @Journal(name = "重置用户密码", logType = LogType.DB)
    @RequestMapping(value = {"reset"}, method = {RequestMethod.POST})
    public String reset(User user) throws NoSuchAlgorithmException {
        user = userService.findById(User.class, user.getId());
        user.setPassword(MD5Utils.getStringMD5("123456", "6d02d09506f651a26bdc3fef63494e5b"));
        this.userService.update(user);
        return "{}";
    }

    @ResponseBody
    @Journal(name = "启用/禁用用户", logType = LogType.DB)
    @RequestMapping(value = {"enable"}, method = {RequestMethod.POST})
    public String enable(User user) throws Exception {
        this.userService.update2(user);
        return "{}";
    }

    @Journal(name = "修改密码页面")
    @NoAuth
    @RequestMapping(value = {"modifyPassword"}, method = {RequestMethod.GET})
    public String password() {
        return "platform/modifyPassword";
    }

    @Journal(name = "修改密码", logType = LogType.DB)
    @NoAuth
    @RequestMapping(value = {"modifyPassword"}, method = {RequestMethod.POST})
    @ResponseBody
    public String password(String p1, String p2) throws NoSuchAlgorithmException {
        long uid = (Long) this.session.getAttribute("userId");
        if (uid == -1L) {
            return this.ajaxError("administrator无法修改密码");
        } else {
            User user = userService.findById(User.class, uid);
            if (MD5Utils.getStringMD5(p1, "6d02d09506f651a26bdc3fef63494e5b").equals(user.getPassword())) {
                user.setPassword(MD5Utils.getStringMD5(p2, "6d02d09506f651a26bdc3fef63494e5b"));
                this.userService.update(user);
                return "{}";
            } else {
                return this.ajaxError("原密码不正确");
            }
        }
    }

    @NoLogin
    @ResponseBody
    @RequestMapping({"{id}"})
    public String xx(@PathVariable("id") Long id) {
        return GsonTools.toJson(this.userService.findById(User.class, id));
    }
}
