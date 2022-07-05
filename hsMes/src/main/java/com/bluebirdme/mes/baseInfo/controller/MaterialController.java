/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.baseInfo.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import com.bluebirdme.mes.baseInfo.entity.Material;
import com.bluebirdme.mes.baseInfo.service.IMaterialService;
import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.annotation.NoAuth;
import com.bluebirdme.mes.core.annotation.support.LogType;
import com.bluebirdme.mes.core.base.controller.BaseController;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.constant.Constant;
import com.bluebirdme.mes.core.valid.annotations.Valid;
import com.bluebirdme.mes.printer.entity.Printer;

/**
 * 原料Controller
 *
 * @author 高飞
 * @Date 2016-10-12 11:06:09
 */
@Controller
@RequestMapping("/material")
@Journal(name = "原料")
public class MaterialController extends BaseController {
    /**
     * 原料页面
     */
    final String index = "baseInfo/material";
    final String addOrEdit = "baseInfo/materialAddOrEdit";

    @Resource
    IMaterialService materialService;

    @Journal(name = "首页")
    @RequestMapping(method = RequestMethod.GET)
    public String index() {
        return index;
    }

    @NoAuth
    @ResponseBody
    @Journal(name = "获取原料列表信息")
    @RequestMapping("list")
    public String getMaterial(Filter filter, Page page) throws Exception {
        return GsonTools.toJson(materialService.findPageInfo(filter, page));
    }

    @NoAuth
    @ResponseBody
    @Journal(name = "获取原料列表信息")
    @RequestMapping("list1")
    public String getMaterial1(Filter filter, Page page) {
        List<Material> plist;
        plist = materialService.findAll(Material.class);
        List<HashMap<String, String>> outInfo = new ArrayList<HashMap<String, String>>();
        for (Material m : plist) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("text", m.getMaterialModel());
            map.put("value", m.getMaterialModel());
            outInfo.add(map);
        }
        return GsonTools.toJson(outInfo);
    }


    @Journal(name = "添加原料页面")
    @RequestMapping(value = "add", method = RequestMethod.GET)
    public ModelAndView _add(Material material) {
        return new ModelAndView(addOrEdit, model.addAttribute("material", material));
    }

    @ResponseBody
    @Journal(name = "保存原料", logType = LogType.DB)
    @RequestMapping(value = "add", method = RequestMethod.POST)
    @Valid
    public String add(Material material) throws Exception {
        HashMap<String, Object> map = new HashMap<>();
        map.put("materialModel", material.getMaterialModel());
        map.put("produceCategory", material.getProduceCategory());
        if (materialService.isExist(Material.class, map, true)) {
            return ajaxError("产品大类和规格型号重复");
        }
        material.setWeight(new BigDecimal(0));
        materialService.save(material);
        return GsonTools.toJson(material);
    }

    @Journal(name = "编辑原料页面")
    @RequestMapping(value = "edit", method = RequestMethod.GET)
    public ModelAndView _edit(Material material) {
        material = materialService.findById(Material.class, material.getId());
        return new ModelAndView(addOrEdit, model.addAttribute("material", material));
    }

    @ResponseBody
    @Journal(name = "编辑原料", logType = LogType.DB)
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public String edit(Material material) throws Exception {
        materialService.update2(material);
        return GsonTools.toJson(material);
    }

    @ResponseBody
    @Journal(name = "删除原料", logType = LogType.DB)
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public String edit(String ids) throws Exception {
        return ajaxError("无法删除");
    }
}