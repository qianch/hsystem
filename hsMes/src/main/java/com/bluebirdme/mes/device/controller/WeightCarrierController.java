/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.device.controller;

import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.annotation.NoAuth;
import com.bluebirdme.mes.core.annotation.NoLogin;
import com.bluebirdme.mes.core.annotation.support.LogType;
import com.bluebirdme.mes.core.base.controller.BaseController;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.constant.Constant;
import com.bluebirdme.mes.core.valid.annotations.Valid;
import com.bluebirdme.mes.device.entity.WeightCarrier;
import com.bluebirdme.mes.device.service.IWeightCarrierService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 称重载具Controller
 *
 * @author 孙利
 * @Date 2017-7-10 8:44:33
 */
@Controller
@RequestMapping("/weightCarrier")
@Journal(name = "称重载具")
public class WeightCarrierController extends BaseController {
    /**
     * 称重载具页面
     */
    final String index = "device/weightCarrier";
    final String addOrEdit = "device/weightCarrierAddOrEdit";

    @Resource
    IWeightCarrierService weightCarrierService;

    @Journal(name = "首页")
    @RequestMapping(method = RequestMethod.GET)
    public String index() {
        return index;
    }

    @NoAuth
    @ResponseBody
    @Journal(name = "获取称重载具列表信息")
    @RequestMapping("list")
    public String getWeightCarrier(Filter filter, Page page) throws Exception {
        return GsonTools.toJson(weightCarrierService.findPageInfo(filter, page));
    }


    @Journal(name = "添加称重载具页面")
    @RequestMapping(value = "add", method = RequestMethod.GET)
    public ModelAndView _add(WeightCarrier weightCarrier) {
        return new ModelAndView(addOrEdit, model.addAttribute("weightCarrier", weightCarrier));
    }

    @ResponseBody
    @Journal(name = "保存称重载具", logType = LogType.DB)
    @RequestMapping(value = "add", method = RequestMethod.POST)
    @Valid
    public String add(WeightCarrier weightCarrier) throws Exception {
        weightCarrier.setCreateTime(new Date());
        weightCarrier.setCreater(session.getAttribute(Constant.CURRENT_USER_NAME).toString());
        weightCarrierService.save(weightCarrier);
        return GsonTools.toJson(weightCarrier);
    }

    @Journal(name = "编辑称重载具页面")
    @RequestMapping(value = "edit", method = RequestMethod.GET)
    public ModelAndView _edit(WeightCarrier weightCarrier) {
        weightCarrier = weightCarrierService.findById(WeightCarrier.class, weightCarrier.getId());
        return new ModelAndView(addOrEdit, model.addAttribute("weightCarrier", weightCarrier));
    }

    @ResponseBody
    @Journal(name = "编辑称重载具", logType = LogType.DB)
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    @Valid
    public String edit(WeightCarrier weightCarrier) throws Exception {
        weightCarrier.setModifyTime(new Date());
        weightCarrier.setModifyUser(session.getAttribute(Constant.CURRENT_USER_NAME).toString());
        weightCarrierService.update2(weightCarrier);
        return GsonTools.toJson(weightCarrier);
    }

    @ResponseBody
    @Journal(name = "删除称重载具", logType = LogType.DB)
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public String edit(String ids) throws Exception {
        weightCarrierService.delete(WeightCarrier.class, ids);
        return Constant.AJAX_SUCCESS;
    }

    @NoLogin
    @Journal(name = "导出Excel")
    @ResponseBody
    @RequestMapping(value = "export", method = RequestMethod.GET)
    public void export(String ids) throws Exception {
        String id[] = ids.split(",");
        SXSSFWorkbook wb = new SXSSFWorkbook();
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER); // 水平布局：居中
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setWrapText(true);
        Sheet sheet = wb.createSheet();
        Row row;
        Cell cell;
        String columnName[] = new String[]{"载具代码", "使用部门", "设备名称", "重量(kg)"};
        int r = 0;// 从第1行开始写数据
        row = sheet.createRow(r);
        cell = row.createCell(0);
        cell.setCellValue("浙江恒石纤维基业有限公司载具列表");
        cell.setCellStyle(cellStyle);
        for (int i = 1; i < 4; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(cellStyle);
        }
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 4));
        r++;
        row = sheet.createRow(r);
        for (int a = 0; a < columnName.length; a++) {
            cell = row.createCell(a);
            cell.setCellValue(columnName[a]);
            cell.setCellStyle(cellStyle);
        }

        for (String s : id) {
            WeightCarrier carrier = weightCarrierService.findById(WeightCarrier.class, s);
            r++;
            row = sheet.createRow(r);
            for (int j = 0; j < columnName.length; j++) {
                cell = row.createCell(j);
                switch (j) {
                    case 0:
                        if (carrier.getCarrierCode() != null) {
                            cell.setCellValue(carrier.getCarrierCode());
                        } else {
                            cell.setCellValue("");
                        }
                        break;
                    case 1:
                        if (carrier.getWorkSpace() != null) {
                            cell.setCellValue(carrier.getWorkSpace());
                        } else {
                            cell.setCellValue("");
                        }
                        break;
                    case 2:
                        if (carrier.getCarrierName() != null) {
                            cell.setCellValue(carrier.getCarrierName());
                        } else {
                            cell.setCellValue("");
                        }
                        break;
                    case 3:
                        cell.setCellValue(carrier.getCarrierWeight());
                        break;
                }
            }
        }
    }
}

		
