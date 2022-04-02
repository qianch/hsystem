/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.stock.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.annotation.Resource;

import com.bluebirdme.mes.utils.HttpUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.xdemo.superutil.j2se.PathUtils;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.annotation.NoAuth;
import com.bluebirdme.mes.core.annotation.support.LogType;
import com.bluebirdme.mes.core.base.controller.BaseController;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.constant.Constant;
import com.bluebirdme.mes.core.valid.annotations.Valid;
import com.bluebirdme.mes.stock.entity.StockFabricMove;
import com.bluebirdme.mes.stock.service.IStockFabricMoveService;

/**
 * 胚布移库表Controller
 * @author 徐波
 * @Date 2017-2-11 8:53:06
 */
@Controller
@RequestMapping("/stockFabricMove")
@Journal(name="胚布移库表")
public class StockFabricMoveController extends BaseController {

	// 胚布移库表页面
	final String index = "stock/stockFabricMove";
	final String addOrEdit="stock/stockFabricMoveAddOrEdit";

	@Resource IStockFabricMoveService stockFabricMoveService;

	
	@Journal(name = "首页")
	@RequestMapping(method = RequestMethod.GET)
	public String index() {
		return index;
	}
	
	@SuppressWarnings("unchecked")
	@NoAuth
	@ResponseBody
	@Journal(name="获取胚布移库表列表信息")
	@RequestMapping("list")
	public String getStockFabricMove(Filter filter, Page page) throws Exception{
		Map<String, Object> findPageInfo = stockFabricMoveService.findPageInfo(filter, page);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> rows = (List<Map<String, Object>>) findPageInfo.get("rows");
		DecimalFormat df = new DecimalFormat("#.00");
		//计算总重量
		if(rows.size() != 0){
			Double d = 0.0;
			Integer num = 0;
			page.setAll(1);
			Map<String, Object> map2 = stockFabricMoveService.findPageInfo(filter, page);
			List<Map<String, Object>> r = (List<Map<String, Object>>) map2.get("rows");
			for (int i = 0; i < r.size(); i++) {
				if(r.get(i).get("PRODUCTWIDTH")!=null&&r.get(i).get("PRODUCTWIDTH")!=""){
					d += (Double)r.get(i).get("PRODUCTWIDTH");
					num++;
				}
			}
			Object o=df.format(d);
			map.put("FACTORYPRODUCTNAME", "合计:");
			map.put("PRODUCTMODEL", num+"    卷");
			map.put("ROLLWEIGHT", o+"    Kg");
			list.add(map);
		}
		else{
			map.put("FACTORYPRODUCTNAME", "合计:");
			map.put("PRODUCTMODEL", 0);
			map.put("ROLLWEIGHT", 0);
			list.add(map);
		}
		findPageInfo.put("footer", list);
		return GsonTools.toJson(findPageInfo);
	}
	

	@Journal(name="添加胚布移库表页面")
	@RequestMapping(value="add",method=RequestMethod.GET)
	public ModelAndView _add(StockFabricMove stockFabricMove){
		return new ModelAndView(addOrEdit,model.addAttribute("stockFabricMove", stockFabricMove));
	}
	
	@ResponseBody
	@Journal(name="保存胚布移库表",logType=LogType.DB)
	@RequestMapping(value="add",method=RequestMethod.POST)
	@Valid
	public String add(StockFabricMove stockFabricMove) throws Exception{
		stockFabricMoveService.save(stockFabricMove);
		return GsonTools.toJson(stockFabricMove);
	}
	
	@Journal(name="编辑胚布移库表页面")
	@RequestMapping(value="edit",method=RequestMethod.GET)
	public ModelAndView _edit(StockFabricMove stockFabricMove){
		stockFabricMove=stockFabricMoveService.findById(StockFabricMove.class, stockFabricMove.getId());
		return new ModelAndView(addOrEdit, model.addAttribute("stockFabricMove", stockFabricMove));
	}
	
	@ResponseBody
	@Journal(name="编辑胚布移库表",logType=LogType.DB)
	@RequestMapping(value="edit",method=RequestMethod.POST)
	@Valid
	public String edit(StockFabricMove stockFabricMove) throws Exception{
		stockFabricMoveService.update(stockFabricMove);
		return GsonTools.toJson(stockFabricMove);
	}

	@ResponseBody
	@Journal(name="删除胚布移库表",logType=LogType.DB)
	@RequestMapping(value="delete",method=RequestMethod.POST)
	public String edit(String ids) throws Exception{
		stockFabricMoveService.delete(StockFabricMove.class,ids);
		return Constant.AJAX_SUCCESS;
	}
	
	
	@SuppressWarnings("unchecked")
	@ResponseBody
	@Journal(name = "胚布领料汇总导出")
	@RequestMapping(value="export")
	public void export(Filter filter) throws Exception{
		Page page =new Page();
		page.setAll(1);

		String start = filter.get("moveTimeb");
		if(start == null){
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MONTH,-6);
			Date date = cal.getTime();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			filter.set("moveTimeb",dateFormat.format(date));
		}
		Map<String, Object> map = stockFabricMoveService.findPageInfo(filter, page);
		List<Map<String, Object>> list = (List<Map<String, Object>>) map.get("rows");
		InputStream is = new FileInputStream(PathUtils.getClassPath() + "template/stockFabricMove.xlsx");
		Workbook wb = new SXSSFWorkbook(new XSSFWorkbook(is));
		Sheet sheet = wb.getSheetAt(0);
		Row row = null;
		Cell cell = null;
		
		int i = 1;
		
		for (Map<String, Object> data : list) {
			row = sheet.createRow(i++);
			for (int j = 0; j < 20; j++) {
				cell = row.createCell(j);
				switch (j) {
				case 0://订单号
					cell.setCellValue(data.get("SALESORDERSUBCODE") == null ? "": data.get("SALESORDERSUBCODE").toString());
					break;
				case 1://条码号
					cell.setCellValue(data.get("BARCODE") == null ? "" : data.get("BARCODE").toString());
					break;
				case 2://批次号
					cell.setCellValue(data.get("BATCHCODE") == null ? "" : data.get("BATCHCODE").toString());
					break;
				case 3://厂内名称
					cell.setCellValue(data.get("FACTORYPRODUCTNAME") == null ? "" : data.get("FACTORYPRODUCTNAME").toString());
					break;
				case 4://产品规格
					cell.setCellValue(data.get("PRODUCTMODEL") == null ? "" : data.get("PRODUCTMODEL").toString());
					break;
				case 5://门幅
					cell.setCellValue(data.get("PRODUCTWIDTH") == null ? "" : data.get("PRODUCTWIDTH").toString());
					break;
				case 6://原库房
					cell.setCellValue(data.get("ORIGINWAREHOUSECODE") == null ? "" : data.get("ORIGINWAREHOUSECODE").toString());
					break;
				case 7://新库房
					cell.setCellValue(data.get("NEWWAREHOUSECODE") == null ? "" : data.get("NEWWAREHOUSECODE").toString());
					break;
				case 8://重量
					cell.setCellValue(data.get("ROLLWEIGHT") == null ? "" : data.get("ROLLWEIGHT").toString());
					break;
				case 9://质量等级
					cell.setCellValue(data.get("ROLLQUALITYGRADECODE") == null ? "" : data.get("ROLLQUALITYGRADECODE").toString());
					break;
				case 10://移库时间
					cell.setCellValue(data.get("MOVETIME") == null ? "" : data.get("MOVETIME").toString());
					break;
				case 11://操作人
					cell.setCellValue(data.get("MOVEUSERID") == null ? "" : data.get("MOVEUSERID").toString());
					break;
				case 12://定长
					cell.setCellValue(data.get("PRODUCTROLLLENGTH") == null ? "" : data.get("PRODUCTROLLLENGTH").toString());
					break;
				default:
					break;
				}
			}
		}
		HttpUtils.download(response,wb,"胚布领料汇总");
		is.close();
	}
}