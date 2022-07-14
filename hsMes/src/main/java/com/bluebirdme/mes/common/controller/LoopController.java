package com.bluebirdme.mes.common.controller;

import com.bluebirdme.mes.audit.service.IAuditInstanceService;
import com.bluebirdme.mes.common.service.ICommonService;
import com.bluebirdme.mes.core.base.controller.BaseController;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.constant.Constant;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 消息服务
 *
 * @author Goofy
 * @Date 2017年1月5日 下午5:10:43
 */
@RestController
@RequestMapping("/loop")
public class LoopController extends BaseController {
    @Resource
    ICommonService commonService;
    @Resource
    IAuditInstanceService auditService;

    @RequestMapping("msg")
    public String getMsg() {
        Map<String, Object> map = new HashMap<>();
        map.put("notice", commonService.getMsg());
        Filter filter = new Filter();
        filter.getFilter().put("uid", session.getAttribute(Constant.CURRENT_USER_ID) + "");
        map.put("task", auditService.auditTask(filter, new Page()));
        return GsonTools.toJson(map);
    }
}
