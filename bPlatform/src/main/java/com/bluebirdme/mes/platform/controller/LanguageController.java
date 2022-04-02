package com.bluebirdme.mes.platform.controller;

import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.base.controller.BaseController;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.utils.FilterRules;
import com.bluebirdme.mes.core.valid.annotations.Valid;
import com.bluebirdme.mes.platform.entity.Language;
import com.bluebirdme.mes.platform.service.ILanguageService;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import javax.annotation.Resource;
import java.util.List;
import java.util.Locale;

/**
 * @author qianchen
 * @date 2020/05/21
 */
@Controller
@RequestMapping({"/language"})
public class LanguageController extends BaseController {
    private static final String ADD_OR_EDIT = "platform/languageAddOrEdit";

    @Resource
    ILanguageService languageService;

    @Journal(name = "访问代码项模块")
    @RequestMapping
    public String index() {
        return "platform/languageView";
    }

    @Valid
    @Journal(name = "选择语言")
    @RequestMapping(value = "localeChange", method = {RequestMethod.GET})
    public ModelAndView languageChange(@RequestParam String locale) {
        session.setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME, Locale.forLanguageTag(locale));
        return new ModelAndView("redirect:/");
    }

    @RequestMapping(value = "add", method = {RequestMethod.GET})
    public ModelAndView _add(Language HSL) {
        return new ModelAndView(ADD_OR_EDIT, model.addAttribute("languageCode", HSL));
    }

    @ResponseBody
    @RequestMapping(value = "add", method = {RequestMethod.POST})
    public Object addLanguageCode(Language HSL) {
        languageService.save(HSL);
        return null;
    }

    @ResponseBody
    @RequestMapping(value = "list", method = {RequestMethod.GET, RequestMethod.POST})
    public String findAll(String filterRules, Page page) throws Exception {
        Filter filter = new Filter();
        if (!StringUtils.isEmpty(filterRules)) {
            JsonParser parser = new JsonParser();
            JsonArray array = parser.parse(filterRules).getAsJsonArray();
            FilterRules rule;
            Gson gson = new Gson();
            for (JsonElement obj : array) {
                rule = gson.fromJson(obj, FilterRules.class);
                filter.set(rule.getField(), "like:" + rule.getValue());
            }
        }
        return GsonTools.toJson(languageService.findPageInfo(filter, page));
    }

    @ResponseBody
    @Journal(name = "获取代码列表信息")
    @RequestMapping({"queryAllHsLanguage"})
    public String queryAllHsLanguage() {
        List<Language> list = languageService.findAll(Language.class);
        return GsonTools.toJson(list);
    }

    @ResponseBody
    @Journal(name = "获取代码列表信息")
    @RequestMapping({"queryLanguageList"})
    public String queryLanguageList(String languageCode) throws Exception {
        List<Language> list = languageService.queryLanguageList(languageCode);
        return GsonTools.toJson(list);
    }

    @RequestMapping(value = "edit", method = {RequestMethod.GET})
    public ModelAndView _update(Language languageCode) {
        languageCode = languageService.findById(Language.class, languageCode.getId());
        return new ModelAndView(ADD_OR_EDIT, model.addAttribute("languageCode", languageCode));
    }

    @ResponseBody
    @RequestMapping(value = "edit", method = {RequestMethod.POST})
    public Object updateLanguageCode(Language language) {
        languageService.update(language);
        return null;
    }

    @ResponseBody
    @RequestMapping(value = "delete", method = {RequestMethod.GET, RequestMethod.POST})
    public Object deleteLanguageCode(String ids) {
        languageService.delete(Language.class, ids);
        return null;
    }
}
