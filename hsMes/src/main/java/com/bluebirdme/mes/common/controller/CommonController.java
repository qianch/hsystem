package com.bluebirdme.mes.common.controller;

import com.bluebirdme.mes.common.service.ICommonService;
import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.annotation.NoLogin;
import com.bluebirdme.mes.core.annotation.support.LogType;
import com.bluebirdme.mes.core.base.controller.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;

/**
 * 公用Controller
 *
 * @author Goofy
 * @Date 2017年1月5日 下午6:03:48
 */
@RestController
@RequestMapping("/common/")
public class CommonController extends BaseController {
    @Resource
    ICommonService commonService;

    @Journal(name = "关闭订单或者计划", logType = LogType.DB)
    @RequestMapping("close")
    public String close(String ids, String type) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        commonService.close(ids, type);
        return ajaxSuccess();
    }

    @NoLogin
    @RequestMapping("qrcode")
    public ModelAndView qrcode() {
        return new ModelAndView("qrcode");
    }
}
