package com.bluebirdme.mes.platform.controller;

import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.annotation.NoAuth;
import com.bluebirdme.mes.core.annotation.NoLogin;
import com.bluebirdme.mes.core.base.controller.BaseController;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.sql.SQLTemplateException;
import com.bluebirdme.mes.core.utils.CacheDict;
import com.bluebirdme.mes.platform.entity.Dict;
import com.bluebirdme.mes.platform.service.IDictService;
import com.google.gson.GsonBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.xdemo.superutil.j2se.StringUtils;
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

@Journal(name = "代码项")
@Controller
@RequestMapping(value = {"/dict", "/mobile/dict"})
public class DictController extends BaseController {
    @Resource
    IDictService dictService;

    public DictController() {
    }

    @Journal(name = "访问代码项模块")
    @RequestMapping
    public String index() {
        return "platform/dict";
    }

    @Journal(name = "加载Dict的ComboTree")
    @ResponseBody
    @RequestMapping({"combotree/{code}"})
    public String combobox(@PathVariable("code") String code, String defaultId) throws SQLTemplateException {
        return GsonTools.toJson(this.dictService.combotree(code, defaultId));
    }


    @NoAuth
    @ResponseBody
    @Journal(name = "获取代码列表信息")
    @RequestMapping({"list"})
    public String getUserList(Filter filter, Page page) throws Exception {
        return (new GsonBuilder()).serializeNulls().create().toJson(this.dictService.findPageInfo(filter, page));
    }


    @NoLogin
    @ResponseBody
    @Journal(name = "获取代码列表信息")
    @RequestMapping({"queryDict"})
    public String queryDict(String rootcode) throws Exception {
        List<Map<String, Object>> list = dictService.queryDict(rootcode);
        List<Map<String, Object>> combobox = new ArrayList();
        if (rootcode.contains("alloptions")) {
            Map<String, Object> map = new HashMap();
            map.put("t", "全部");
            map.put("v", "");
            combobox.add(map);
        }
        for (Map<String, Object> m : list) {
            Map<String, Object> map = new HashMap();
            map.put("t", m.get("name_zh_CN".toUpperCase()));
            map.put("v", m.get("CODE"));
            combobox.add(map);
        }
        return GsonTools.toJson(combobox);
    }

    @NoLogin
    @ResponseBody
    @Journal(name = "获取代码列表信息")
    @RequestMapping({"queryDictAll"})
    public String queryDictAll() throws Exception {
        List<Map<String, String>> list = dictService.queryDictAll();
        List<Map<String, Object>> combobox = new ArrayList<Map<String, Object>>();
        Map<String, Object> map;
        for (Map<String, String> m : list) {
            map = new HashMap();
            map.put("t", m.get("name_zh_CN".toUpperCase()));
            map.put("v", m.get("CODE"));
            combobox.add(map);
        }
        return GsonTools.toJson(combobox);
    }

    @NoAuth
    @ResponseBody
    @Journal(name = "获取代码列表信息")
    @RequestMapping({"queryDictText"})
    public String queryDictText(String type, String value) throws Exception {
        return CacheDict.getInstance().get(type).get(value);
    }


    @Journal(name = "进入字典添加页面")
    @RequestMapping(value = {"add"}, method = {RequestMethod.GET})
    public ModelAndView _add(Dict dict) {
        if ((dict != null) && dict.getPid() != null) {
            Dict dict_ = this.dictService.findById(Dict.class, dict.getPid());
            return new ModelAndView("platform/dictAddOrEdit", this.model.addAttribute("dict", dict).addAttribute("parentName", dict_.getName_zh_CN()));
        } else {
            return new ModelAndView("platform/dictAddOrEdit", this.model.addAttribute("dict", dict));
        }
    }

    @ResponseBody
    @Journal(name = "保存字典数据")
    @RequestMapping(value = {"add"}, method = {RequestMethod.POST})
    public String add(Dict dict) {
        Map<String, Object> param = new HashMap();
        param.put("code", dict.getCode());
        if (this.dictService.isExist(Dict.class, param, false)) {
            return this.ajaxError("字典编码重复");
        } else {
            param.clear();
            param.put("name_zh_CN", dict.getName_zh_CN());
            if (this.dictService.isExist(Dict.class, param, false)) {
                return this.ajaxError("字典名称重复");
            } else {
                if (dict.getPid() != null) {
                    Dict d = dictService.findById(Dict.class, dict.getPid());
                    dict.setCode(StringUtils.trimAll(dict.getCode()).toUpperCase());
                    dict.setRootCode(d.getRootCode());
                } else {
                    dict.setCode(StringUtils.trimAll(dict.getCode()).toUpperCase());
                    dict.setRootCode(dict.getCode());
                }

                this.dictService.save(dict);
                if (dict.getDeprecated() == 1) {
                    dict.setName_zh_CN(dict.getName_zh_CN() + "[弃用]");
                }
                return GsonTools.toJson(dict);
            }
        }
    }

    @Journal(name = "进入字典编辑页面")
    @RequestMapping(value = {"edit"}, method = {RequestMethod.GET})
    public ModelAndView _edit(Dict dict) {
        dict = dictService.findById(Dict.class, dict.getId());
        int pos = dict.getCode().lastIndexOf("_") + 1;
        dict.setCode(dict.getCode().substring(pos));
        return new ModelAndView("platform/dictAddOrEdit", this.model.addAttribute("dict", dict));
    }

    @ResponseBody
    @Journal(name = "保存编辑后的字典数据")
    @RequestMapping(value = {"edit"}, method = {RequestMethod.POST})
    public String edit(Dict dict) throws Exception {
        Map<String, Object> param = new HashMap();
        param.put("code", dict.getCode());
        if (dictService.isExist(Dict.class, param, dict.getId(), false)) {
            return this.ajaxError("字典编码重复");
        } else {
            param.clear();
            param.put("name_zh_CN", dict.getName_zh_CN());
            if (this.dictService.isExist(Dict.class, param, dict.getId(), false)) {
                return this.ajaxError("字典名称重复");
            } else {
                if (dict.getPid() != null) {
                    Dict d = dictService.findById(Dict.class, dict.getPid());
                    dict.setCode(StringUtils.trimAll(dict.getCode()).toUpperCase());
                    dict.setRootCode(d.getRootCode());
                } else {
                    dict.setCode(StringUtils.trimAll(dict.getCode()).toUpperCase());
                    dict.setRootCode(dict.getCode());
                }

                dictService.update(dict);
                if (dict.getDeprecated() == 1) {
                    dict.setName_zh_CN(dict.getName_zh_CN() + "[弃用]");
                }
                return GsonTools.toJson(dict);
            }
        }
    }

    @ResponseBody
    @Journal(name = "删除字典")
    @RequestMapping(value = {"delete"}, method = {RequestMethod.POST})
    public String delete(String ids) {
        this.dictService.delete(ids);
        return "{}";
    }

    @ResponseBody
    @Journal(name = "通过字典编码Code查询，作为ZTree的数据")
    @RequestMapping({"ztree/{code}"})
    public String findByCode(@PathVariable("code") String code) {
        return GsonTools.toJson(dictService.findByCode(code.toUpperCase()));
    }
}
