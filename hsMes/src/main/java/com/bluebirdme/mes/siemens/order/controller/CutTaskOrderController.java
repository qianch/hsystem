/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.siemens.order.controller;

import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.print.DocFlavor;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.xdemo.superutil.j2se.PathUtils;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.annotation.NoAuth;
import com.bluebirdme.mes.core.annotation.NoLogin;
import com.bluebirdme.mes.core.annotation.support.LogType;
import com.bluebirdme.mes.core.base.controller.BaseController;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.constant.Constant;
import com.bluebirdme.mes.printer.entity.Printer;
import com.bluebirdme.mes.siemens.bom.entity.CutGroup;
import com.bluebirdme.mes.siemens.order.entity.CutTask;
import com.bluebirdme.mes.siemens.order.entity.CutTaskDrawings;
import com.bluebirdme.mes.siemens.order.entity.CutTaskOrder;
import com.bluebirdme.mes.siemens.order.entity.CutTaskOrderDrawings;
import com.bluebirdme.mes.siemens.order.service.ICutTaskOrderService;
import com.bluebirdme.mes.utils.HttpUtils;
import com.bluebirdme.mes.utils.MapUtils;

/**
 * 裁剪派工单Controller
 * 
 * @author 高飞
 * @Date 2017-7-31 17:04:12
 */
@Controller
@RequestMapping("/siemens/cutTaskOrder")
@Journal(name = "裁剪派工单")
public class CutTaskOrderController extends BaseController {

	// 裁剪派工单页面
	final String index = "siemens/order/cutTaskOrder";
	final String addOrEdit = "siemens/order/cutTaskOrderAddOrEdit";

	@Resource
	ICutTaskOrderService cutTaskOrderService;

	@Journal(name = "首页")
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView index() {
		List<CutGroup> list = cutTaskOrderService.findAll(CutGroup.class);
		model.addAttribute("groups", GsonTools.toJson(list));
		List<Printer> printerList = cutTaskOrderService.findAll(Printer.class);
		model.addAttribute("printers", GsonTools.toJson(printerList));
		return new ModelAndView(index, model);
	}

	@NoAuth
	@ResponseBody
	@Journal(name = "获取裁剪派工单列表信息")
	@RequestMapping("list")
	public String getCutTaskOrder(Filter filter, Page page) throws Exception {
		return GsonTools.toJson(cutTaskOrderService.findPageInfo(filter, page));
	}

	@Journal(name = "获取任务单号")
	@ResponseBody
	@RequestMapping("serial")
	public synchronized String getSerial() {
		return cutTaskOrderService.getSerial();
	}

	@ResponseBody
	@Journal(name = "删除裁剪派工单", logType = LogType.DB)
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	public String edit(String id) throws Exception {
		int count = cutTaskOrderService.getTotalPrintedCount(Long.parseLong(id));
		if (count > 0) {
			throw new Exception("已打印条码，无法删除");
		}
		cutTaskOrderService.deleteTask(id);
		return Constant.AJAX_SUCCESS;
	}

	@Journal(name = "获取裁剪任务单图纸BOM")
	@ResponseBody
	@RequestMapping("getCutTaskDrawings")
	public String getCutTaskDrawings(Long ctId) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		List<CutTaskDrawings> list = cutTaskOrderService.getCutTaskDrawings(ctId);
		List<Map<Object, Object>> retList = new ArrayList<Map<Object, Object>>();
		for (CutTaskDrawings d : list) {
			retList.add(MapUtils.entityToMap(d));
		}
		return GsonTools.toJson(retList);
	}

	@Journal(name = "获取裁剪任务单图纸BOM")
	@ResponseBody
	@RequestMapping("info")
	public String getInfo(Long ctId) throws Exception {

		Map<String, Object> ret = new HashMap<String, Object>();
		ret.put("serial", getSerial());

		List<CutTaskDrawings> list = cutTaskOrderService.getCutTaskDrawings(ctId);
		List<Map<Object, Object>> retList = new ArrayList<Map<Object, Object>>();
		Map<Object, Object> map = new HashMap<Object, Object>();
		for (CutTaskDrawings d : list) {
			map = MapUtils.entityToMap(d);
			map.remove("ctId");
			retList.add(map);
		}
		ret.put("drawings", retList);

		CutTask task = cutTaskOrderService.findById(CutTask.class, ctId);

		ret.put("assigned", task.getSuitCount() - task.getAssignSuitCount());

		return GsonTools.toJson(ret);
	}

	@Journal(name = "保存裁剪任务单")
	@ResponseBody
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public String SaveCutTaskOrder(@RequestBody CutTaskOrder cto) throws Exception {
		if (cto.getId() == null)
			cto.setCtoCode(getSerial());
		cutTaskOrderService.save(cto);
		return ajaxSuccess();
	}

	@Journal(name = "关闭/启用生产任务单", logType = LogType.DB)
	@ResponseBody
	@RequestMapping("close")
	public String close(String id, Integer closed) throws Exception {
		cutTaskOrderService.close(id, closed);
		return ajaxSuccess();
	}

	@Journal(name = "获取图号")
	@ResponseBody
	@RequestMapping("drawingsNo")
	public String drawingsNo(Long ctId) {
		List<CutTaskDrawings> list = cutTaskOrderService.getCutTaskDrawings(ctId);
		return GsonTools.toJson(list);
	}

	@Journal(name = "获取图纸")
	@ResponseBody
	@RequestMapping("drawings")
	public String drawings(Long ctoId) {
		List<CutTaskOrderDrawings> list = cutTaskOrderService.find(CutTaskOrderDrawings.class, "ctoId", ctoId);
		return GsonTools.toJson(list);
	}

	@Journal(name = "条码打印")
	@ResponseBody
	@RequestMapping("printBarcode")
	public synchronized String printBarcode(Long ctoId, String[] drawingsNo, Integer suitCount,String cutPlanId, String printer) throws Exception {
		/*
		 * DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE; PrintService[]
		 * psZebras = PrintServiceLookup.lookupPrintServices(flavor, null);
		 * boolean isContains = false; for (PrintService ps : psZebras) { if
		 * (ps.getName().equals(printer)) { System.out.println(ps.getName());
		 * isContains = true; break; } } if (!isContains) { String plist = "";
		 * for (PrintService ps : psZebras) { if (plist.length() == 0) { plist =
		 * ps.getName(); } else { plist += (";" + ps.getName()); } } return
		 * ajaxError("打印机不存在"); }
		 */

		cutTaskOrderService.printBarcode(ctoId, drawingsNo, suitCount, printer,cutPlanId, session.getAttribute(Constant.CURRENT_USER_NAME) + "");
		return ajaxSuccess();
	}

	@Journal(name = "重打条码")
	@ResponseBody
	@RequestMapping("rePrintBarcode")
	public String rePrint(Long ctoId, Long dwId, Integer rePrintCount, String printer, String user, String reason) throws Exception {
		cutTaskOrderService.rePrint(ctoId, dwId, rePrintCount, printer, user, reason);
		return ajaxSuccess();
	}

	@Journal(name = "派工单条码核对表")
	@RequestMapping("checkBarcode")
	public void exportCutTaskOrderBarcode(Long ctoId) throws Exception {
		CutTaskOrder cto = cutTaskOrderService.findById(CutTaskOrder.class, ctoId);

		InputStream is = new FileInputStream(PathUtils.getClassPath() + "/template/ctob.xlsx");
		Workbook wb = new XSSFWorkbook(is);
		// 创建一个工作表sheet
		Sheet sheet = wb.getSheetAt(0);
		// 申明行
		Row row = sheet.createRow(3);

		Cell cell = row.createCell(0);
		cell.setCellValue(cto.getIsClosed() == 0 ? "启用" : "关闭");
		cell = row.createCell(1);
		cell.setCellValue(cto.getCtoCode());
		cell = row.createCell(2);
		cell.setCellValue(cto.getCtoGroupName());
		cell = row.createCell(3);
		cell.setCellValue(cto.getCtoGroupLeader());
		cell = row.createCell(4);
		cell.setCellValue(cto.getTaskCode());
		cell = row.createCell(5);
		cell.setCellValue(cto.getOrderCode());
		cell = row.createCell(6);
		cell.setCellValue(cto.getPartName());
		cell = row.createCell(7);
		cell.setCellValue(cto.getPartName());
		cell = row.createCell(8);
		cell.setCellValue(cto.getConsumerSimpleName());
		cell = row.createCell(9);
		cell.setCellValue(cto.getConsumerCategory() == 1 ? "国内" : "国外");
		cell = row.createCell(10);
		cell.setCellValue(cto.getSuitCount());
		cell = row.createCell(11);
		cell.setCellValue(cto.getAssignSuitCount());
		cell = row.createCell(12);
		cell.setCellValue(cto.getPackedSuitCount());
		cell = row.createCell(13);
		cell.setCellValue(cto.getDeliveryDate());
		cell = row.createCell(14);
		cell.setCellValue(cto.getCreateTime());
		cell = row.createCell(15);
		cell.setCellValue(cto.getCreateUserName());
		cell = row.createCell(16);
		cell.setCellValue(cto.getIsComplete() == 0 ? "未完成" : "完成");

		List<CutTaskOrderDrawings> list = cutTaskOrderService.find(CutTaskOrderDrawings.class, "ctoId", ctoId);

		for (int i = 0; i < list.size(); i++) {
			row = sheet.createRow(6 + i);
			cell = row.createCell(0);
			cell.setCellValue(list.get(i).getFarbicModel());
			cell = row.createCell(1);
			cell.setCellValue(list.get(i).getFragmentCode());
			cell = row.createCell(2);
			cell.setCellValue(list.get(i).getFragmentName());
			cell = row.createCell(3);
			cell.setCellValue(list.get(i).getFragmentCountPerDrawings());
			cell = row.createCell(4);
			cell.setCellValue(list.get(i).getNeedToPrintCount());
			cell = row.createCell(5);
			cell.setCellValue(list.get(i).getPrintedCount());
			cell = row.createCell(6);
			cell.setCellValue(list.get(i).getExtraPrintCount());
			cell = row.createCell(7);
			cell.setCellValue(list.get(i).getRePrintCount());
			cell = row.createCell(8);
			cell.setCellValue(list.get(i).getFragmentMemo());
		}
		HttpUtils.download(response,wb, "派工单小部件条码核对表");
	}

	@NoLogin
	@ResponseBody
	@Journal(name = "根据任务单号，获取图号")
	@RequestMapping("drawing")
	public String drawingNo(String ctoCode) {
		CutTaskOrder cto = cutTaskOrderService.findOne(CutTaskOrder.class, "ctoCode", ctoCode);
		Map<String, Object> ret = new HashMap<String, Object>();
		ret.put("drawingNo", cutTaskOrderService.getDrawingNo(cto.getId()));
		return GsonTools.toJson(ret);
	}

	@NoLogin
	@ResponseBody
	@Journal(name = "打印")
	@RequestMapping("print")
	public String print(String printer,Long ctoId, Long drawingId, String order,Integer levelCount) throws Exception {
		String ip = HttpUtils.getIpAddr(request);
		cutTaskOrderService.print(printer, ip, ctoId, drawingId, order, levelCount);
		return ajaxSuccess();
	}

	@NoLogin
	@ResponseBody
	@Journal(name = "获取下一个打印的条码")
	@RequestMapping("next")
	public String next(String ctoCode, String drawingNo) {
		CutTaskOrder cto = cutTaskOrderService.findOne(CutTaskOrder.class, "ctoCode", ctoCode);
		String ip = HttpUtils.getIpAddr(request);
		Map<String, Object> ret = new HashMap<String, Object>();
		ret.put("cto", cto);
		ret.put("drawing", cutTaskOrderService.next(ip, ctoCode, drawingNo));
		return GsonTools.toJson(ret);
	}
}