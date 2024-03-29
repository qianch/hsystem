package com.bluebirdme.mes.core.base.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import java.util.HashMap;
import java.util.Map;

/**
 * @author qianchen
 * @date 2020/05/21
 */

@Controller
@RequestMapping("/error")
public class ExceptionController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionController.class);
    private boolean isAjax = false;

    /**
     * 拦截http状态异常
     */
    @RequestMapping("{httpStatusCode}")
    public ModelAndView error(@PathVariable("httpStatusCode") Integer httpStatusCode) {
        try {
            isAjax = null != request.getHeader("X-Requested-With");
            httpStatusCode = httpStatusCode == null ? 0 : httpStatusCode;
            switch (httpStatusCode) {
                case 400 -> request.setAttribute("error", "参数错误");
                case 403 -> request.setAttribute("error", "禁止访问");
                case 404 -> request.setAttribute("error", "请求地址未找到");
                case 405 -> request.setAttribute("error", "请确认GET还是POST请求");
                case 415 -> request.setAttribute("error", "请求的类型不支持,可能请求参数不正确");
                default -> {
                }
            }
            if (isAjax) {
                return new ModelAndView("error/ajaxError", model).addObject("_HTTP_STATUS_CODE_", httpStatusCode);
            }
            response.setStatus(httpStatusCode);
            return new ModelAndView("error/" + httpStatusCode, model);
        } catch (Exception e) {
            return new ModelAndView("error/500");
        }
    }

    /**
     * 会话过期
     */
    @RequestMapping("expired")
    public ModelAndView expired() {
        isAjax = null != request.getHeader("X-Requested-With");
        model.addAttribute("error", "expired");
        if (isAjax) {
            return new ModelAndView("error/ajaxError", model);
        }
        return new ModelAndView("error/expired");
    }

    /**
     * 拦截Ajax请求异常
     */
    @ResponseBody
    @RequestMapping("ajaxError")
    public String ajaxError() {
        try {
            Map<String, Object> error = new HashMap<>(1);
            error.put("error", request.getAttribute("error"));
            try {
                int code = request.getAttribute("_HTTP_STATUS_CODE_") == null ? 500 : Integer.parseInt(request.getAttribute("_HTTP_STATUS_CODE_") + "");
                response.setStatus(code);
            } catch (Exception ex) {
                logger.error(ex.getLocalizedMessage(), ex);
            }
            return GsonTools.toJson(error);
        } catch (Exception e) {
            return ajaxError("系统异常");
        }
    }
}
