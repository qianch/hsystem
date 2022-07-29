/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.produce.controller;

import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.annotation.NoAuth;
import com.bluebirdme.mes.core.annotation.support.LogType;
import com.bluebirdme.mes.core.base.controller.BaseController;
import com.bluebirdme.mes.core.constant.Constant;
import com.bluebirdme.mes.core.valid.annotations.Valid;
import com.bluebirdme.mes.produce.entity.ProduceCalendar;
import com.bluebirdme.mes.produce.service.IProduceCalendarService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 排产日历Controller
 *
 * @author 高飞
 * @Date 2016-11-1 13:05:53
 */
@Controller
@RequestMapping("/produce/calendar")
@Journal(name = "排产日历")
public class ProduceCalendarController extends BaseController {
    /**
     * 排产日历页面
     */
    final String index = "produce/produceCalendar";
    final String addOrEdit = "produce/produceCalendarAddOrEdit";

    @Resource
    IProduceCalendarService produceCalendarService;

    private final String[] COLORS = {"#6CC303", "#231D72", "#D8DD6A", "#E4EBA6", "#A2D343", "#0A5675", "#80108D", "#306BFD", "#1CC798", "#A99DB6", "#96CB37", "#05EEA4", "#5A0F6E", "#BAB889", "#DFAFE3", "#7CF9CB", "#3F35F6", "#599E4B", "#F620F0", "#2CA0DB", "#57CDA5", "#2695AD", "#264D55", "#30B2BD", "#A8D24F", "#896054", "#A97A35", "#108A33", "#BC85C5", "#F67E6A", "#18FF02", "#B1D2CB", "#791D58", "#2C9B80", "#69BC9E", "#F3D4F6", "#A0756C", "#505D7D", "#75E2D6", "#D6B980", "#5C3DFC", "#D83F51", "#541EA5", "#F0D539", "#E9F5F9", "#3057F4", "#47F13C", "#F757BF", "#F385C2", "#53F24B", "#5BC62F", "#93CBFF", "#5D0FF3", "#291CA5", "#79CB8B", "#4DE4C5", "#4E2148", "#41CA45", "#960C69", "#5015BE", "#540D99", "#C38E5E", "#39DD94", "#C63475", "#285FBE", "#E82625", "#E5A52F", "#434345", "#57015C", "#3E00A6", "#BF2C72", "#D719E1", "#04A1AD", "#EF0684", "#7FF55F", "#B8FBC3", "#A4C175", "#164FFD", "#C05F39", "#00216B", "#82FF6C", "#E463BB", "#7D9212", "#4F5371", "#D8EFBF", "#EFE00F", "#EFA699", "#448C06", "#E4A481", "#9E2FE8", "#36D614", "#0FB24B", "#D2B56B", "#8F87C1", "#D6BB82", "#B0CC17", "#3D0973", "#9D6E33", "#97B00F", "#8949CF", "#42E88C", "#2729C1", "#09A97D", "#DA5F6F", "#60262F", "#3FF0AF", "#1727BD", "#EE09DE", "#07743F", "#CDC0AF", "#A9D37B", "#4BD6CC", "#FDA688", "#DD9A4A", "#AF455F", "#ECCD6D", "#C36332", "#E5E426", "#0B6B28", "#CA1F69", "#92812B", "#A1B507", "#D146D0", "#0A4ACE", "#084E9E", "#E1C716", "#095D78", "#60F1BF", "#6F73E7", "#5D436F", "#9C3155", "#3DD71A", "#20294D", "#8A7AD5", "#631742", "#DA2000", "#1E8742", "#43B631", "#656102", "#820E66", "#461702", "#639D18", "#0A2A07", "#5A9FB5", "#814051", "#CBBF0E", "#616A2A", "#6C221B", "#BD7BAB", "#8F96B6", "#4D1AB6", "#2544A7", "#BFEC1E", "#F96EA6", "#6CCC46", "#FC6069", "#F01ABE", "#C31DB2", "#4D6F7D", "#919AC3", "#F23747", "#6E09B1", "#27598C", "#CAD42A", "#B29F87", "#5BABA8", "#4D376A", "#62B2DC", "#24DCFA", "#E0EF2D", "#E4591E", "#221A3C", "#33627E", "#D99A12", "#423A70", "#D69369", "#2DB55D", "#1057E5", "#DF4863", "#58D12F", "#173939", "#9594BA", "#925560", "#174142", "#EA0D79", "#A11FAA", "#DC6B69", "#C38B4C", "#8098E7", "#51F634", "#331DBE", "#4AC0D9", "#17F6A9", "#D40405", "#1D787E", "#5C6825", "#7F9428", "#92F921", "#8B9080", "#D9868E"};

    @Journal(name = "首页")
    @RequestMapping(method = RequestMethod.GET)
    public String index() {
        return index;
    }

    @NoAuth
    @ResponseBody
    @Journal(name = "获取排产日历列表信息")
    @RequestMapping("list")
    public String getProduceCalendar(String start, String end) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<Map<String, Object>> list = produceCalendarService.findList(start, end);
        List<Map<String, Object>> retList = new ArrayList<>();
        Map<String, Object> ret;
        int i = 0;
        for (Map<String, Object> map : list) {
            ret = new HashMap<>();
            if (map.get("STARTTIME") instanceof Date) {
                ret.put("start", sdf.format(map.get("STARTTIME")));
            }
            if (map.get("ENDTIME") instanceof Date) {
                ret.put("end", sdf.format(map.get("ENDTIME")));
            }
            ret.put("title", map.get("SALESORDERCODE") + "(" + map.get("CONSUMERNAME") + ")");
            ret.put("backgroundColor", COLORS[i++]);
            retList.add(ret);
        }
        return GsonTools.toJson(retList);
    }


    @Journal(name = "添加排产日历页面")
    @RequestMapping(value = "add", method = RequestMethod.GET)
    public ModelAndView _add(ProduceCalendar produceCalendar) {
        return new ModelAndView(addOrEdit, model.addAttribute("produceCalendar", produceCalendar));
    }

    @ResponseBody
    @Journal(name = "保存排产日历", logType = LogType.DB)
    @RequestMapping(value = "add", method = RequestMethod.POST)
    @Valid
    public String add(ProduceCalendar produceCalendar) throws Exception {
        Map<String, Object> param = new HashMap<>();
        param.put("salesOrderCode", produceCalendar.getSalesOrderCode());
        if (produceCalendarService.isExist(ProduceCalendar.class, param, true)) {
            return ajaxError("该订单已加入生产日历");
        }
        produceCalendarService.save(produceCalendar);
        return GsonTools.toJson(produceCalendar);
    }

    @Journal(name = "编辑排产日历页面")
    @RequestMapping(value = "edit", method = RequestMethod.GET)
    public ModelAndView _edit(ProduceCalendar produceCalendar) {
        Map<String, Object> map = new HashMap<>();
        map.put("salesOrderCode", produceCalendar.getSalesOrderCode());
        produceCalendar = produceCalendarService.findUniqueByMap(ProduceCalendar.class, map);
        return new ModelAndView(addOrEdit, model.addAttribute("produceCalendar", produceCalendar));
    }

    @ResponseBody
    @Journal(name = "编辑排产日历", logType = LogType.DB)
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    @Valid
    public String edit(ProduceCalendar produceCalendar) throws Exception {
        Map<String, Object> param = new HashMap<>();
        param.put("salesOrderCode", produceCalendar.getSalesOrderCode());
        if (produceCalendarService.isExist(ProduceCalendar.class, param, produceCalendar.getId(), true)) {
            return ajaxError("该订单已加入生产日历");
        }
        produceCalendarService.update(produceCalendar);
        return GsonTools.toJson(produceCalendar);
    }

    @ResponseBody
    @Journal(name = "删除排产日历", logType = LogType.DB)
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public String delete(ProduceCalendar cal) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("salesOrderCode", cal.getSalesOrderCode());
        cal = produceCalendarService.findUniqueByMap(ProduceCalendar.class, map);
        produceCalendarService.delete(cal);
        return Constant.AJAX_SUCCESS;
    }

    public static String randomColor() {
        String r, g, b;
        Random random = new Random();
        r = Integer.toHexString(random.nextInt(256)).toUpperCase();
        g = Integer.toHexString(random.nextInt(256)).toUpperCase();
        b = Integer.toHexString(random.nextInt(256)).toUpperCase();
        r = r.length() == 1 ? "0" + r : r;
        g = g.length() == 1 ? "0" + g : g;
        b = b.length() == 1 ? "0" + b : b;
        return r + g + b;
    }
}