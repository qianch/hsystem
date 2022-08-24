/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.store.controller;

import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.annotation.NoAuth;
import com.bluebirdme.mes.core.annotation.support.LogType;
import com.bluebirdme.mes.core.base.controller.BaseController;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.constant.Constant;
import com.bluebirdme.mes.core.valid.annotations.Valid;
import com.bluebirdme.mes.store.entity.Roll;
import com.bluebirdme.mes.store.service.IRollService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import javax.annotation.Resource;

/**
 * 卷Controller
 *
 * @author 徐波
 * @Date 2016-11-9 15:32:13
 */
@Controller
@RequestMapping("/roll")
@Journal(name = "卷")
public class RollController extends BaseController {
    /**
     * 卷页面
     */
    final String index = "store/roll";
    final String addOrEdit = "store/rollAddOrEdit";

    @Resource
    IRollService rollService;

    @Journal(name = "首页")
    @RequestMapping(method = RequestMethod.GET)
    public String index() {
        return index;
    }

    @NoAuth
    @ResponseBody
    @Journal(name = "获取卷列表信息")
    @RequestMapping("list")
    public String getRoll(Filter filter, Page page) throws Exception {
        return GsonTools.toJson(rollService.findPageInfo(filter, page));
    }


    @Journal(name = "添加卷页面")
    @RequestMapping(value = "add", method = RequestMethod.GET)
    public ModelAndView _add(Roll roll) {
        return new ModelAndView(addOrEdit, model.addAttribute("roll", roll));
    }

    @ResponseBody
    @Journal(name = "保存卷", logType = LogType.DB)
    @RequestMapping(value = "add", method = RequestMethod.POST)
    @Valid
    public String add(Roll roll) throws Exception {
        rollService.save(roll);
        return GsonTools.toJson(roll);
    }

    @Journal(name = "编辑卷页面")
    @RequestMapping(value = "edit", method = RequestMethod.GET)
    public ModelAndView _edit(Roll roll) {
        roll = rollService.findById(Roll.class, roll.getId());
        return new ModelAndView(addOrEdit, model.addAttribute("roll", roll));
    }

    @ResponseBody
    @Journal(name = "编辑卷", logType = LogType.DB)
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    @Valid
    public String edit(Roll roll) throws Exception {
        rollService.update(roll);
        return GsonTools.toJson(roll);
    }

    @ResponseBody
    @Journal(name = "删除卷", logType = LogType.DB)
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public String edit(String ids) throws Exception {
        rollService.delete(Roll.class, ids);
        return Constant.AJAX_SUCCESS;
    }
}