package com.bluebirdme.mes.planner.material.controller;

import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.annotation.NoLogin;
import com.bluebirdme.mes.core.base.controller.BaseController;
import com.bluebirdme.mes.planner.material.service.IMrpService;
import com.bluebirdme.mes.planner.produce.entity.ProducePlan;
import com.bluebirdme.mes.utils.HttpUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 物料需求计划Controller
 * 
 * @author Goofy
 * @Date 2016年10月21日 上午9:33:59
 */
@Controller
@RequestMapping("/planner/mrp")
public class MrpController extends BaseController {
	private final String mrpPage = "/planner/mrp/mrpList";

	@Resource
	IMrpService mrpService;

	@Journal(name = "访问物料需求计划页面")
	@RequestMapping(method = RequestMethod.GET)
	public String index() {
		return mrpPage;
	}

	@Deprecated
	@NoLogin
	@RequestMapping("create")
	public void createMrp(Long id) throws Exception {
		ProducePlan pp = mrpService.findById(ProducePlan.class, id);
		mrpService.createRequirementPlans(pp);
	}

	@Journal(name = "查询物料需求计划数据")
	@ResponseBody
	@RequestMapping("list")
	public String listMrp(Long[] ids) {
		return GsonTools.toJson(mrpService.findRequirementPlans(ids));
	}
	
	@SuppressWarnings({ "deprecation", "unchecked" })
	@NoLogin
	@Journal(name = "导出")
	@ResponseBody
	@RequestMapping(value = "export", method = RequestMethod.GET)
	public void export(Long[] ids) throws Exception {
		ProducePlan pp=mrpService.findById(ProducePlan.class, ids[0]);
		Workbook wb = new SXSSFWorkbook();
		Sheet sheet=wb.createSheet();
		Row row = null;
		Cell cell = null;
		int rowIndex=0;
		row=sheet.createRow(rowIndex++);
		row.setHeight((short)1000);
		cell = row.createCell(0);
		cell.setCellValue("浙江恒石纤维基业有限公司物料需求单");
		CellStyle titleStyle = wb.createCellStyle();
		titleStyle.setAlignment(HorizontalAlignment.CENTER); // 水平布局：居中
		titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		titleStyle.setWrapText(false);
		
		XSSFFont titleFont=(XSSFFont) wb.createFont();
		titleFont.setBold(true);
		titleFont.setFontHeightInPoints((short)15);
		
		titleStyle.setFont(titleFont);
		
		cell.setCellStyle(titleStyle);
		
		CellStyle miniTitleStyle=wb.createCellStyle();
		miniTitleStyle.setWrapText(true);
		XSSFFont miniTitleFont=(XSSFFont) wb.createFont();
		miniTitleFont.setBold(true);
		miniTitleStyle.setFont(miniTitleFont);;
		
		cell.setCellStyle(titleStyle);
		for (int i = 1; i < 5; i++) {
			cell = row.createCell(i);
			cell.setCellStyle(titleStyle);
		}
		
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 5));
		
		Map<String,Object> mrp= mrpService.findRequirementPlans(ids);
		
		List<Map<String,Object>> _mrpList=(List<Map<String, Object>>) mrp.get("yuanliao");
		
		/**
		 * 原料
		 */
		writeRow(sheet.createRow(rowIndex++), miniTitleStyle,"原料需求");
		writeRow(sheet.createRow(rowIndex++),miniTitleStyle,"规格型号","需求数量","单位");
		for(Map<String,Object> map:_mrpList){
			writeRow(sheet.createRow(rowIndex++),null,map.get("MATERIALMODEL"),map.get("MATERIALTOTALWEIGHT"),"KG");
		}
		
		writeRow(sheet.createRow(rowIndex++),null,"");
		
		/**
		 * 包材
		 */
		writeRow(sheet.createRow(rowIndex++), miniTitleStyle,"包材需求");
		writeRow(sheet.createRow(rowIndex++),miniTitleStyle,"物料代码","标准码","包材名称","规格型号","材质","需求数量","单位");
		_mrpList=(List<Map<String, Object>>) mrp.get("baocai");
		for(Map<String,Object> map:_mrpList){
			writeRow(sheet.createRow(rowIndex++),null,map.get("MTCODE"),map.get("STCODE"),map.get("PACKMATERIALNAME"),map.get("PACKMATERIALMODEL"),map.get("PACKMATERIALATTR"),map.get("PACKMATERIATOTALCOUNT"),map.get("PACKMATERIALUNIT"));
		}
		
		HttpUtils.download(response,wb, pp.getProducePlanCode()+(ids.length==1?"":"等")+"的物料需求");
	}
	
	void writeRow(Row row,CellStyle style, Object... values) {
		int i = 0;
		Cell cell=null;
		for (Object v : values) {
			if (v == null) {
				v = "";
			}
			cell=row.createCell(i++);
			cell.setCellStyle(style);
			cell.setCellValue(v + "");
		}
	}
}
