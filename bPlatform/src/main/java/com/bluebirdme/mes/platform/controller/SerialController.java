package com.bluebirdme.mes.platform.controller;

import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.annotation.NoAuth;
import com.bluebirdme.mes.platform.service.ISerialService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @author qianchen
 * @date 2020/05/21
 */
@Controller
@RequestMapping({"/serial"})
public class SerialController {
    @Resource
    ISerialService serialService;

    public SerialController() {
    }

    @NoAuth
    @Journal(name = "获取递增的单号")
    @ResponseBody
    @RequestMapping(value = {"{preffix}-{length}"}, method = {RequestMethod.GET})
    public String index(@PathVariable("preffix") String preffix, @PathVariable("length") Integer length) {
        return serialService.getSerialNumber(preffix, length);
    }
}
