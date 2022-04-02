package com.bluebirdme.mes.platform.controller;


import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.annotation.NoAuth;
import com.bluebirdme.mes.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

/**
 * @author qianchen
 * @date 2020/05/21
 */
@Journal(name = "项目首页")
@Controller
@RequestMapping()
public class IndexController extends BaseController {
    public IndexController() {
    }

    @NoAuth
    @Journal(name = "访问首页")
    @RequestMapping(method = {RequestMethod.GET})
    public ModelAndView index(@CookieValue(value = "ui", defaultValue = "v1") String ui) {
        Locale locale = (Locale) session.getAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME);
        String frame = "frame" + (ui == null ? "" : ("v2".equals(ui) ? ".v2" : ""));
        return session.getAttribute("userId") != null ?
                new ModelAndView(frame, model.addAttribute("locale", locale.toLanguageTag())) :
                new ModelAndView("login");
    }
}
