package com.bluebirdme.mes.common.controller;

import com.bluebirdme.mes.common.service.IFixService;
import com.bluebirdme.mes.core.annotation.NoLogin;
import com.bluebirdme.mes.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;

/**
 * 类注释
 *
 * @author Goofy
 * @Date 2017年7月13日 上午9:30:18
 */
@Controller
@RequestMapping("/fix")
public class FixController extends BaseController {
    @Resource
    IFixService fixService;

    @NoLogin
    @RequestMapping(value = "wp/{batchCodes}")
    public void fixWeavePlanRollNO(@PathVariable("batchCodes") String[] batchCodes) {
        fixService.fixRollNO(batchCodes);
    }

    @NoLogin
    @RequestMapping(value = "rollsCountInTray")
    public void fixRollCountInTray() {
        fixService.fixRollCountInTray();
    }

    @NoLogin
    @RequestMapping(value = "produceToWeave")
    public void produceToWeave(Long producePlanDetailId) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        fixService.produceToWeave(producePlanDetailId);
    }
}
