package com.bluebirdme.mes.platform.controller;

import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.annotation.NoAuth;
import com.bluebirdme.mes.core.base.controller.BaseController;
import com.bluebirdme.mes.platform.service.ISystemInfoService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import javax.annotation.Resource;

/**
 * @author qianchen
 * @date 2020/05/21
 */
@Controller
@RequestMapping({"/systemInfo"})
public class SystemInfoController extends BaseController {
    @Resource
    ISystemInfoService systemInfo;

    public SystemInfoController() {
    }

    @NoAuth
    @ResponseBody
    @Journal(name = "查询现在的系统公告数据")
    @RequestMapping({"usercount"})
    public String findUserCount() {
        return GsonTools.toJson(systemInfo.findUserCount());
    }
}
