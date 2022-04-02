package com.bluebirdme.mes.stock.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.bluebirdme.mes.utils.HttpUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xdemo.superutil.j2se.PathUtils;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.annotation.NoAuth;
import com.bluebirdme.mes.core.base.controller.BaseController;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.stock.service.IProductInRecordService;

@Controller
@RequestMapping("shopReport")
@Journal(name = "车间入库汇总报表")
public class ShopSummaryReportController extends BaseController {
	private static Logger logger = LoggerFactory.getLogger(ShopSummaryReportController.class);
	@Resource
	IProductInRecordService productInRecordService;
	
	// 车间入库汇总报表（产品大类、订单号、批次号、厂内名称）页面
	final String shopSummaryReport = "stock/shopSummaryReport/shopSummaryReport";
	
	// 车间入库汇总报表（产品大类、厂内名称汇总重量）页面
	final String shopCategorySummaryReport = "stock/shopSummaryReport/shopCategorySummaryReport";
	
	/**
	 * 车间入库汇总报表（产品大类、订单号、批次号、厂内名称）页面
	 * @return
	 */
	@Journal(name = "车间入库汇总报表（产品大类、订单号、批次号、厂内名称）页面")
	@RequestMapping(value = "shopSummaryReport",method = RequestMethod.GET)
	public String produceSundrySummaryView() {
		return shopSummaryReport;
	}
	
	/**
	 * 车间入库汇总报表（产品大类、厂内名称汇总重量）页面
	 * @return
	 */
	@Journal(name = "车间入库汇总报表（产品大类、厂内名称汇总重量）页面")
	@RequestMapping(value = "shopCategorySummaryReport",method = RequestMethod.GET)
	public String shopCategorySummaryView() {
		return shopCategorySummaryReport;
	}
	
	/**
	 * 车间入库汇总报表（产品大类、订单号、批次号、厂内名称）列表
	 * @param filter
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@NoAuth
	@ResponseBody
	@Journal(name = "车间入库汇总报表（车间、厂内名称、大类）列表")
	@RequestMapping("sscList")
	public String shopStorageCategory(Filter filter, Page page) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			String start = filter.get("start").toString();
			String end = filter.get("end");
			if(end == null){
				filter.set("end", sdf.format(new Date()));
			}else{
				filter.set("end", end);
			}
			if(start != null){
				filter.set("start", start);
			}
			//总计
			Map<String, Object> findPageInfo = productInRecordService.shopStorageCategoryStatistics(filter, page);
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			Map<String, Object> map = new HashMap<String, Object>();
			List<Map<String, Object>> rows = (List<Map<String, Object>>) findPageInfo.get("rows");
			DecimalFormat df = new DecimalFormat("#.00");
			findPageInfo.put("footer", list);
			if(rows.size() != 0){
				page.setAll(1);
				Map<String, Object> map2 = productInRecordService.shopStorageCategoryStatistics(filter, page);
				List<Map<String, Object>> r = (List<Map<String, Object>>) map2.get("rows");
				Double weight = 0.0;
				for(int i=0;i<r.size();i++){
					weight += (Double)r.get(i).get("WEIGHT");
				}
				
				Object i = df.format(weight);
				map.put("CATEGORYNAME", "总计：");
				map.put("WEIGHT", i+"  KG");
				list.add(map);
			}else{
				map.put("CATEGORYNAME", "总计：");
				map.put("WEIGHT", 0+"  KG");
				list.add(map);
			}
			findPageInfo.put("footer", list);
			return GsonTools.toJson(findPageInfo);
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage(),e);
		}
		return GsonTools.toJson("");
	}
	
	/**
	 * 车间入库汇总报表（产品大类、订单号、批次号、厂内名称）列表
	 * @param filter
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@NoAuth
	@ResponseBody
	@Journal(name = "车间入库汇总报表（产品大类、订单号、批次号、厂内名称）列表")
	@RequestMapping("ShopStatisticsList")
	public String getProductStock(Filter filter, Page page) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			String start = filter.get("start").toString();
			String end = filter.get("end");
			if(end == null){
				filter.set("end", sdf.format(new Date()));
				filter.set("endTime", sdf.format(new Date()));
			}else{
				filter.set("end", end);
				filter.set("endTime", end);
			}
			if(!"".equals(start)){
				filter.set("start", start);
				filter.set("startTime", start);
			}
			//总计
			Map<String, Object> findPageInfo = productInRecordService.productsShopStatistics(filter, page);
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			Map<String, Object> map = new HashMap<String, Object>();
			List<Map<String, Object>> rows = (List<Map<String, Object>>) findPageInfo.get("rows");
			DecimalFormat df = new DecimalFormat("#.00");
			findPageInfo.put("footer", list);
			if(rows.size() != 0){
				page.setAll(1);
				Map<String, Object> map2 = productInRecordService.productsShopStatistics(filter, page);
				List<Map<String, Object>> r = (List<Map<String, Object>>) map2.get("rows");
				Double tnum = 0.0;
				Double weight = 0.0;
				for(int i=0;i<r.size();i++){
					String object = r.get(i).get("TNUM").toString();
					tnum += Double.parseDouble(object);
					weight += (Double)r.get(i).get("WEIGHT");
				}
				
				Object s = df.format(tnum);
				Object i = df.format(weight);
				map.put("CONSUMERNAME", "总计：");
				map.put("TNUM", s+"  托");
				map.put("WEIGHT", i+"  KG");
				list.add(map);
			}else{
				map.put("CONSUMERNAME", "总计：");
				map.put("TNUM", 0+"  托");
				map.put("WEIGHT", 0+"  KG");
				list.add(map);
			}
			findPageInfo.put("footer", list);
			return GsonTools.toJson(findPageInfo);
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage(),e);
		}
		return GsonTools.toJson("");
	}

	@SuppressWarnings("unchecked")
	@ResponseBody
	@Journal(name = "车间入库汇总报表（产品大类、订单号、批次号、厂内名称）导出")
	@RequestMapping(value="export")
	public void export2(Filter filter) throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Page page =new Page();
		page.setAll(1);
		
		String start = filter.get("start").toString();
		String end = filter.get("end");
		if(end == null){
			filter.set("end", sdf.format(new Date()));
			filter.set("endTime", sdf.format(new Date()));
		}else{
			filter.set("end", end);
			filter.set("endTime", end);
		}
		if(!"".equals(start)){
			filter.set("start", start);
			filter.set("startTime", start);
		}
		Map<String, Object> map = productInRecordService.productsShopStatistics(filter, page);
		List<Map<String, Object>> list = (List<Map<String, Object>>) map.get("rows");
		InputStream is = new FileInputStream(PathUtils.getClassPath() + "template/productShopStatistics.xlsx");
		Workbook wb = new SXSSFWorkbook(new XSSFWorkbook(is));
		Sheet sheet = wb.getSheetAt(0);
		Row row = null;
		Cell cell = null;
		int i = 1;
		for (Map<String, Object> data : list) {
			row = sheet.createRow(i++);
			for (int j = 0; j < 15; j++) {
				cell = row.createCell(j);
				switch (j) {
				case 0://计划单号
					cell.setCellValue(data.get("PRODUCEPLANCODE") == null ? "" : data.get("PRODUCEPLANCODE").toString());
					break;
				case 1://订单号
					cell.setCellValue(data.get("SALESORDERCODE") == null ? "" : data.get("SALESORDERCODE").toString());
					break;
				case 2://客户订单号
					cell.setCellValue(data.get("SALESORDERSUBCODEPRINT") == null ? "" : data.get("SALESORDERSUBCODEPRINT").toString());
					break;
				case 3://客户名称
					cell.setCellValue(data.get("CONSUMERNAME") == null ? "" : data.get("CONSUMERNAME").toString());
					break;
				case 4://产品大类
					cell.setCellValue(data.get("CATEGORYNAME") == null ? "" : data.get("CATEGORYNAME").toString());
					break;
				case 5://成品类别代码
					cell.setCellValue(data.get("CATEGORYCODE") == null ? "" : data.get("CATEGORYCODE").toString());
					break;
				case 6://客户产品名称
					cell.setCellValue(data.get("CONSUMERPRODUCTNAME") == null ? "" : data.get("CONSUMERPRODUCTNAME").toString());
					break;
				case 7://厂内名称
					cell.setCellValue(data.get("FACTORYPRODUCTNAME") == null ? "" : data.get("FACTORYPRODUCTNAME").toString());
					break;
				case 8://产品规格
					cell.setCellValue(data.get("PRODUCTMODEL") == null ? "" : data.get("PRODUCTMODEL").toString());
					break;
				case 9://批次号
					cell.setCellValue(data.get("BATCHCODE") == null ? "" : data.get("BATCHCODE").toString());
					break;
				case 10://质量等级
					cell.setCellValue(data.get("ROLLQUALITYGRADECODE") == null ? "" : data.get("ROLLQUALITYGRADECODE").toString());
					break;
				case 11://车间
					cell.setCellValue(data.get("WORKSHOPNAME") == null ? "" : data.get("WORKSHOPNAME").toString());
					break;
				case 12://托数量
					cell.setCellValue(data.get("TNUM") == null ? "0" : data.get("TNUM").toString());
					break;
				case 13://重量
					cell.setCellValue(data.get("WEIGHT") == null ? "0" : data.get("WEIGHT").toString());
					break;
				}
			}
		}
		HttpUtils.download(response,wb,"车间入库汇总报表（产品大类、订单号、批次号、厂内名称）");
		is.close();
	}
	
	

	@SuppressWarnings("unchecked")
	@ResponseBody
	@Journal(name = "车间入库汇总报表（车间、厂内名称、大类）导出")
	@RequestMapping(value="export1")
	public void export1(Filter filter) throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Page page =new Page();
		page.setAll(1);
		
		String start = filter.get("start").toString();
		String end = filter.get("end");
		
		if(end == null){
			filter.set("end", sdf.format(new Date()));
		}else{
			filter.set("end", end);
		}
		if(!"".equals(start)){
			filter.set("start", start);
		}
		
		Map<String, Object> map = productInRecordService.shopStorageCategoryStatistics(filter, page);
		List<Map<String, Object>> list = (List<Map<String, Object>>) map.get("rows");
		InputStream is = new FileInputStream(PathUtils.getClassPath() + "template/shop-storage-category.xlsx");
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
				case 0://产品大类
					cell.setCellValue(data.get("CATEGORYNAME") == null ? "" : data.get("CATEGORYNAME").toString());
					break;
				case 1://产品类别代码
					cell.setCellValue(data.get("CATEGORYCODE") == null ? "" : data.get("CATEGORYCODE").toString());
					break;
				case 2://客户产品名称
					cell.setCellValue(data.get("CONSUMERPRODUCTNAME") == null ? "" : data.get("CONSUMERPRODUCTNAME").toString());
					break;
				case 3://厂内名称
					cell.setCellValue(data.get("FACTORYPRODUCTNAME") == null ? "" : data.get("FACTORYPRODUCTNAME").toString());
					break;
				case 4://产品规格
					cell.setCellValue(data.get("PRODUCTMODEL") == null ? "" : data.get("PRODUCTMODEL").toString());
					break;
				case 5://车间
					cell.setCellValue(data.get("WORKSHOPNAME") == null ? "" : data.get("WORKSHOPNAME").toString());
					break;
				case 6://重量
					cell.setCellValue(data.get("WEIGHT").toString());
					break;
				default:
					break;
				}
			}
		}
		HttpUtils.download(response,wb,"车间入库汇总报表（车间、厂内名称、大类）导出");
		is.close();
	}
}