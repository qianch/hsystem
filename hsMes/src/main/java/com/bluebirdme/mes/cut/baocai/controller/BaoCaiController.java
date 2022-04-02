/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.cut.baocai.controller;


import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.annotation.NoAuth;
import com.bluebirdme.mes.core.annotation.NoLogin;
import com.bluebirdme.mes.core.annotation.support.LogType;
import com.bluebirdme.mes.core.base.controller.BaseController;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.constant.Constant;
import com.bluebirdme.mes.core.valid.annotations.Valid;
import com.bluebirdme.mes.cut.baocai.entity.BaoCaiKu;
import com.bluebirdme.mes.cut.baocai.service.IBaoCaiService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Map;

/**
 * 出货计划Controller
 *
 * @author 徐波
 * @Date 2016-11-2 9:30:06
 */
@Controller
@RequestMapping("cut/baocai")
@Journal(name = "包材")
public class BaoCaiController extends BaseController {
    // 裁剪图纸bom
    final String index = "cut/baocai/baocai";
    //新增修改页面
    final String addOrEdit = "cut/baocai/baoCaiAddOrEdit";

    @Resource
    IBaoCaiService iBaoCaiService;

    @Journal(name = "首页")
    @RequestMapping(method = RequestMethod.GET)
    public String index() {
        return index;
    }

    @NoAuth
    @ResponseBody
    @Journal(name = "获取裁剪图纸套材bom信息")
    @RequestMapping("list")
    public String getBaoCaiKu(Filter filter, Page page) throws Exception {
        Map<String, Object> pageInfo = iBaoCaiService.findPageInfo(filter, page);
        return GsonTools.toJson(pageInfo);
    }


    @ResponseBody
    @Journal(name = "根据主表Id查询裁剪图纸套材bom信息")
    @RequestMapping(value = "findPackingDetailByPackingID", method = RequestMethod.POST)
    public String findPackingDetailByPackingID(Long packingID) {
        return GsonTools.toJson(iBaoCaiService.findPackingDetailByPackingID(packingID));
    }

    @NoLogin
    @Journal(name = "根据出货单id导出成品出库单汇总")
    @ResponseBody
    @RequestMapping(value = "exportDetail")
    public void exportDetail(Filter filter) {


    }

    @Journal(name = "添加加载裁剪图纸信息")
    @RequestMapping(value = "add", method = RequestMethod.GET)
    public ModelAndView _add(BaoCaiKu baocaiku) {
        return new ModelAndView(addOrEdit, model.addAttribute("baoCaiKu", baocaiku));
    }


    @Journal(name = "加载裁剪图纸信息")
    @RequestMapping(value = "baoCaiKuAddOrEditPage", method = RequestMethod.GET)
    public ModelAndView cutTcBomMainAddOrEditPage(BaoCaiKu baocaiku) {
        if (baocaiku.getId() != null) {
            baocaiku = iBaoCaiService.findById(BaoCaiKu.class, baocaiku.getId());
        }
        return new ModelAndView(addOrEdit, model.addAttribute("baoCaiKu", baocaiku));
    }


    @ResponseBody
    @Journal(name = "保存裁剪图纸信息", logType = LogType.DB)
    @RequestMapping(value = "saveCutTcBomMain", method = RequestMethod.POST)
    @Valid
    public String saveBaoCaiKu(@RequestBody BaoCaiKu baoCaiKu) throws Exception {
        String userName = session.getAttribute(Constant.CURRENT_USER_NAME).toString();
        String result = iBaoCaiService.saveBaoCaiKu(baoCaiKu, userName);
        return GsonTools.toJson(result);
    }

    @ResponseBody
    @Journal(name = "作废裁剪图纸bom", logType = LogType.DB)
    @RequestMapping(value = "cancel", method = RequestMethod.POST)
    public String cancel(String ids) {
        String ids_temp[] = ids.split(",");
        Serializable ids_target[] = new Serializable[ids_temp.length];
        for (int i = 0; i < ids_temp.length; i++) {
            BaoCaiKu baoCaiKu = iBaoCaiService.findById(BaoCaiKu.class, Long.parseLong(ids_temp[i]));
            iBaoCaiService.update(baoCaiKu);
        }
        return Constant.AJAX_SUCCESS;
    }
}
