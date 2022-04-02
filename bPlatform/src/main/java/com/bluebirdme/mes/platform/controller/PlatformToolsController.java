package com.bluebirdme.mes.platform.controller;

import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.annotation.NoLogin;
import com.bluebirdme.mes.core.base.controller.BaseController;
import com.google.gson.GsonBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author qianchen
 * @date 2020/05/21
 */
@Journal(name = "平台工具模块")
@Controller
@RequestMapping({"/tools"})
public class PlatformToolsController extends BaseController {
    @Resource
    private RequestMappingHandlerMapping handlerMapping;

    public PlatformToolsController() {
    }

    @NoLogin
    @Journal(name = "查询系统中所有的功能菜单URL和说明")
    @ResponseBody
    @RequestMapping({"url"})
    public String url() {
        Map<RequestMappingInfo, HandlerMethod> map = this.handlerMapping.getHandlerMethods();
        Iterator<Entry<RequestMappingInfo, HandlerMethod>> it = map.entrySet().iterator();
        HandlerMethod method;
        Class<? extends Object> clazz = null;
        HashMap urlMap = new HashMap();

        while (true) {
            do {
                if (!it.hasNext()) {
                    return (new GsonBuilder()).create().toJson(urlMap);
                }
                Entry entry = it.next();
                method = (HandlerMethod) entry.getValue();
            } while (null != clazz && clazz.getName().equals(method.getBeanType().getName()));

            clazz = method.getBeanType();
            Method[] methods = clazz.getMethods();

            Journal journal = clazz.getAnnotation(Journal.class);
            String base = clazz.getAnnotation(RequestMapping.class).value()[0].substring(1);
            urlMap.put(base, journal == null ? "" : journal.name());

            int length = methods.length;
            for (int i = 0; i < length; ++i) {
                Method m = methods[i];
                RequestMapping rm = m.getAnnotation(RequestMapping.class);
                journal = m.getAnnotation(Journal.class);
                if (rm != null && rm.value().length != 0) {
                    String sub = m.getAnnotation(RequestMapping.class).value() == null ? "/" : m.getAnnotation(RequestMapping.class).value()[0];
                    urlMap.put(base + "/" + sub, journal == null ? "" : journal.name());
                }
            }
        }
    }
}
