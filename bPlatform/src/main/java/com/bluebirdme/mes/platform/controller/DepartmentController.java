package com.bluebirdme.mes.platform.controller;

import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.annotation.NoAuth;
import com.bluebirdme.mes.core.annotation.NoLogin;
import com.bluebirdme.mes.core.annotation.support.LogType;
import com.bluebirdme.mes.core.base.controller.BaseController;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.exception.BusinessException;
import com.bluebirdme.mes.core.valid.annotations.Valid;
import com.bluebirdme.mes.platform.entity.Department;
import com.bluebirdme.mes.platform.service.IDepartmentService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.xdemo.superutil.j2se.StringUtils;
import org.xdemo.superutil.thirdparty.PinYinUtils;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author qianchen
 * @date 2020/05/21
 */

@Journal(name = "部门模块")
@Controller
@RequestMapping({"/department", "/mobile/department"})
public class DepartmentController extends BaseController {
    @Resource
    IDepartmentService departmentService;

    public DepartmentController() {
    }

    @Journal(name = "访问部门模块")
    @RequestMapping
    public String index() {
        return "platform/department";
    }

    @NoAuth
    @ResponseBody
    @Journal(name = "获取部门列表信息")
    @RequestMapping({"list"})
    public String getDepartment(Filter filter, Page page) throws Exception {
        return (new GsonBuilder()).serializeNulls().create().toJson(this.departmentService.findPageInfo(filter, page));
    }

    @Journal(name = "添加部门页面")
    @RequestMapping(value = {"add"}, method = {RequestMethod.GET})
    public ModelAndView _add(Department department) {
        return new ModelAndView("platform/departmentAddOrEdit", this.model.addAttribute("department", department).addAttribute("action", "add"));
    }

    @ResponseBody
    @Journal(name = "保存部门", logType = LogType.DB)
    @RequestMapping(value = {"add"}, method = {RequestMethod.POST})
    @Valid
    public String add(Department department) throws Exception {
        if (StringUtils.isBlank(department.getCode())) {
            department.setCode(PinYinUtils.getPinYinHeadChar(department.getName()).toUpperCase());
        }
        Map<String, Object> map = new HashMap<>();
        map.put("name", department.getName());
        if (this.departmentService.isExist(Department.class, map)) {
            throw new BusinessException("部门名称重复:" + department.getName());
        }
        this.departmentService.save(department);
        return (new GsonBuilder()).serializeNulls().create().toJson(department);
    }

    @Journal(name = "编辑部门页面")
    @RequestMapping(value = {"edit"}, method = {RequestMethod.GET})
    public ModelAndView _edit(Department department) {
        department = this.departmentService.findById(Department.class, department.getId());
        return new ModelAndView("platform/departmentAddOrEdit", this.model.addAttribute("department", department).addAttribute("action", "edit"));
    }

    @ResponseBody
    @Journal(name = "编辑部门", logType = LogType.DB)
    @RequestMapping(value = {"edit"}, method = {RequestMethod.POST})
    public String edit(Department department) throws Exception {
        Department _department = this.departmentService.findById(Department.class, department.getId());
        Map<String, Object> map = new HashMap<>();
        map.put("name", department.getName());
        if (!_department.getName().equals(department.getName()) && this.departmentService.isExist(Department.class, map)) {
            throw new BusinessException("部门名称重复:" + department.getName());
        }
        this.departmentService.update2(department);
        return (new GsonBuilder()).serializeNulls().create().toJson(department);
    }

    @NoAuth
    @ResponseBody
    @Journal(name = "批量更新部门层级关系")
    @RequestMapping(value = {"batchUpdateDepartmentLevel"}, method = {RequestMethod.POST})
    public String batchUpdateDepartmentLevel(@RequestBody List<Department> list) {
        this.departmentService.batchUpdateDepartmentLevel(list);
        return "{}";
    }

    @ResponseBody
    @Journal(name = "编辑部门信息")
    @RequestMapping({"editDepartmentById"})
    public String editDepartmentById(String id) {
        Department department = this.departmentService.findById(Department.class, Long.parseLong(id));
        return (new Gson()).toJson(department);
    }

    @ResponseBody
    @Journal(name = "删除部门", logType = LogType.DB)
    @RequestMapping(value = {"delete"}, method = {RequestMethod.DELETE})
    public String deleteDepartmentById(@RequestBody List<Department> list) throws Exception {
        this.departmentService.delete(list);
        return "{}";
    }

    @NoLogin
    @ResponseBody
    @RequestMapping({"{id}"})
    public String xx(@PathVariable("id") Long id) {
        return GsonTools.toJson(this.departmentService.findById(Department.class, id));
    }

    @NoAuth
    @ResponseBody
    @Journal(name = "获取代码列表信息")
    @RequestMapping({"queryDepartment"})
    public String queryDepartment(String type) throws Exception {
        List<Map<String, Object>> list = departmentService.queryDepartment(type);
        List<Map<String, Object>> combobox = new ArrayList<>();
        Map<String, Object> map;
        if (type.contains("alloptions")) {
            map = new HashMap<>();
            map.put("t", "全部");
            map.put("v", "");
            combobox.add(map);
        }
        for (Map<String, Object> m : list) {
            map = new HashMap<>();
            map.put("t", m.get("NAME"));
            map.put("v", m.get("CODE"));
            combobox.add(map);
        }
        return GsonTools.toJson(combobox);
    }


    @RequestMapping({"queryAllDepartment"})
    public String queryAllDepartment() throws Exception {
        List<Map<String, Object>> list = departmentService.queryAllDepartment();
        List<Map<String, Object>> combobox = new ArrayList<>();
        Map<String, Object> map;
        for (Map<String, Object> m : list) {
            map = new HashMap<>();
            map.put("t", m.get("NAME"));
            map.put("v", m.get("CODE"));
            combobox.add(map);
        }
        return GsonTools.toJson(combobox);
    }

    @NoAuth
    @ResponseBody
    @Journal(name = "获取代码列表信息")
    @RequestMapping({"queryAllDepartmentID"})
    public String queryAllDepartmentID() throws Exception {
        List<Map<String, Object>> list = departmentService.queryAllDepartment();
        List<Map<String, Object>> combobox = new ArrayList<>();
        Map<String, Object> map;
        for (Map<String, Object> m : list) {
            map = new HashMap<>();
            map.put("t", m.get("NAME"));
            map.put("v", m.get("ID"));
            combobox.add(map);
        }
        return GsonTools.toJson(combobox);
    }

    @NoLogin
    @NoAuth
    @ResponseBody
    @Journal(name = "获取代码列表信息")
    @RequestMapping(value = "queryDepartmentByType")
    public String queryDepartmentByType(String type) throws Exception {
        List<Map<String, Object>> list = departmentService.queryAllDepartmentByType(type);
        List<Map<String, Object>> combobox = new ArrayList<>();
        Map<String, Object> map;
        for (Map<String, Object> m : list) {
            map = new HashMap<>();
            map.put("t", m.get("NAME"));
            map.put("v", m.get("CODE"));
            combobox.add(map);
        }
        return GsonTools.toJson(combobox);
    }
}
