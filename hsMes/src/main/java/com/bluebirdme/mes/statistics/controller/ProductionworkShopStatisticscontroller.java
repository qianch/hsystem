/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.statistics.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import com.bluebirdme.mes.statistics.service.ITotalStatisticsService;

/**
 * 综合统计Controller
 * 
 * @author 徐波
 * @Date 2016-11-26 14:44:04
 */
@Controller
@RequestMapping("/shopStatistics")
@Journal(name = "车间生产统计")
public class ProductionworkShopStatisticscontroller extends BaseController {
	private static Logger logger = LoggerFactory.getLogger(ProductionworkShopStatisticscontroller.class);
	// 车间生产统计(产品大类、订单号、批次号、车间)页面
	final String index = "statistics/shopStatistics/shopStatistics";
	// 车间生产统计（产品大类、厂内名称）页面
	final String index1 = "statistics/shopStatistics/genericFactorySummary";
	// 生产领料统计页面
	final String pickingStatistics = "statistics/pickingStatistics/pickingStatistics";
	
	@Resource
	ITotalStatisticsService totalStatisticsService;

	/**
	 * 生产领料统计
	 * @return
	 */
	@Journal(name = "生产领料统计")
	@RequestMapping(value = "pickingStatistics", method = RequestMethod.GET)
	public String pickingStatistics() {
		return pickingStatistics;
	}

	/**
	 * 车间生产统计（产品大类、厂内名称）页面
	 * @return
	 */
	@Journal(name = "车间生产统计（产品大类、厂内名称）")
	@RequestMapping(value = "genericFactorySummary", method = RequestMethod.GET)
	public String genericFactorySummary() {
		return index1;
	}
	
	/**
	 * 车间生产统计(产品大类、订单号、批次号、车间)页面
	 * @return
	 */
	@Journal(name = "车间生产统计(产品大类、订单号、批次号、车间)")
	@RequestMapping(value = "shopSummary", method = RequestMethod.GET)
	public String dailyIndex() {
		return index;
	}
	
	/**
	 * 生产领料汇总
	 * @param filter
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@NoAuth
	@ResponseBody
	@Journal(name = "生产领料汇总")
	@RequestMapping("pickingStatisticsList")
	public String pickingStatisticsList(Filter filter, Page page) throws Exception {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//24小时制  
		if(filter.get("start")!=null){
		    long startTimes= simpleDateFormat.parse(filter.get("start").toString()).getTime(); 
			filter.set("start", String.valueOf(startTimes));
		}
		if(filter.get("end")!=null){
			long startTimes= simpleDateFormat.parse(filter.get("end")).getTime(); 
			filter.set("end", String.valueOf(startTimes));
		}
		Map<String, Object> findPageInfo = totalStatisticsService.getPickingStatistics(filter, page);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> rows = (List<Map<String, Object>>) findPageInfo.get("rows");
		DecimalFormat df = new DecimalFormat("#.00");
		if(rows.size() != 0){
			page.setAll(1);
			Map<String, Object> map2 = totalStatisticsService.getPickingStatistics(filter, page);
			List<Map<String, Object>> r = (List<Map<String, Object>>) map2.get("rows");
			Double d = 0.0;
			for(int i = 0; i < r.size(); i++){
			Object	o=r.get(i).get("OUTWEIGHT");
				d += Double.parseDouble(o.toString());;
			}
			Object o=df.format(d);
			map.put("PRODUCECATEGORY", "总计：");
			map.put("OUTWEIGHT", o);
			list.add(map);
		}
		else{
			map.put("PRODUCECATEGORY", "总计：");
			map.put("OUTWEIGHT", 0);
			list.add(map);
		}
		findPageInfo.put("footer", list);
//		return GsonTools.toJson(totalStatisticsService.getPickingStatistics(filter, page));
		return GsonTools.toJson(findPageInfo);
		
	}
	
	/**
	 * （产品大类、厂内名称）列表
	 * @param filter
	 * @param page
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@NoAuth
	@ResponseBody
	@Journal(name = "车间生产统计（产品大类、厂内名称）列表")
	@RequestMapping("gfylist")
	public String genericFactorySummaryList(Filter filter, Page page) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		/*获取上个月最后一天,设定默认开始时间*/
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 1); 
		calendar.add(Calendar.DATE, -1);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH)+1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		String date = year + "-"+month+"-"+day+" 12:00:00";
		try {
			String start = filter.get("start");
			String end = filter.get("end");
			if(end == null){
				filter.set("end", sdf.format(new Date()));
				filter.set("endTime", sdf.format(new Date()));
			}else{
				filter.set("end", end);
				filter.set("endTime", end);
			}
			if(start != null){
				filter.set("start", start);
				filter.set("startTime", start);
			}else{
				filter.set("start", date);
				filter.set("startTime", date);
			}
			Map<String, Object> findPageInfo = totalStatisticsService.genericFactorySummary(filter, page);
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			Map<String, Object> map = new HashMap<String, Object>();
			List<Map<String, Object>> rows = (List<Map<String, Object>>) findPageInfo.get("rows");
			
			DecimalFormat df = new DecimalFormat("#.00");
			//计算总重量、总托数
			if(rows.size() != 0){
				page.setAll(1);
				Map<String, Object> map2 = totalStatisticsService.genericFactorySummary(filter, page);
				List<Map<String, Object>> r = (List<Map<String, Object>>) map2.get("rows");
				Double weight = 0.0;
				Double tnum = 0.0;
				for(int i = 0; i < r.size(); i++){
					if(r.get(i).get("FACTORYPRODUCTNAME") != ""){
						weight += (Double)r.get(i).get("PRODUCTWEIGHT");
						Object object = r.get(i).get("TNUM");
						tnum += Double.parseDouble(object.toString());;
					}
				}
				Object w=df.format(weight);
				map.put("CATEGORYNAME", "总计：");
				map.put("PRODUCTWEIGHT", w+"  Kg");
				map.put("TNUM", tnum+"  托");
				list.add(map);
			}else{
				map.put("CATEGORYNAME", "总计：");
				map.put("PRODUCTWEIGHT", 0+"  Kg");
				map.put("TNUM", 0+"  托");
			}
			findPageInfo.put("footer", list);
			return GsonTools.toJson(findPageInfo);
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage(),e);
		}
		return GsonTools.toJson("");
	}

	/**
	 * 车间生产统计(产品大类、订单号、次号、车间)列表
	 * @param filter
	 * @param page
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@NoAuth
	@ResponseBody
	@Journal(name = "车间生产统计(产品大类、订单号、批次号、车间)列表")
	@RequestMapping("shopSummarylist")
	public String shopSummarylist(Filter filter, Page page) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		/*获取上个月最后一天,设定默认开始时间*/
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 1); 
		calendar.add(Calendar.DATE, -1);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH)+1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		String date = year + "-"+month+"-"+day+" 12:00:00";
		try {
			String start = filter.get("start");
			String end = filter.get("end");
			if(end == null){
				filter.set("end", sdf.format(new Date()));
				filter.set("endTime", sdf.format(new Date()));
			}else{
				filter.set("end", end);
				filter.set("endTime", end);
			}
			if(start != null){
				filter.set("start", start);
				filter.set("startTime", start);
			}else{
				filter.set("start", date);
				filter.set("startTime", date);
			}
			Map<String, Object> findPageInfo = totalStatisticsService.productsShopSummary(filter, page);
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			Map<String, Object> map = new HashMap<String, Object>();
			List<Map<String, Object>> rows = (List<Map<String, Object>>) findPageInfo.get("rows");
			
			DecimalFormat df = new DecimalFormat("#.00");
			//计算总重量、总托数
			if(rows.size() != 0){
				page.setAll(1);
				Map<String, Object> map2 = totalStatisticsService.productsShopSummary(filter, page);
				List<Map<String, Object>> r = (List<Map<String, Object>>) map2.get("rows");
				Double weight = 0.0;
				Double tnum = 0.0;
				for(int i = 0; i < r.size(); i++){
					if(r.get(i).get("FACTORYPRODUCTNAME") != ""){
						weight += (Double)r.get(i).get("PRODUCTWEIGHT");
						Object object = r.get(i).get("TNUM");
						tnum += Double.parseDouble(object.toString());;
					}
				}
				Object w=df.format(weight);
				map.put("FACTORYPRODUCTNAME", "总计：");
				map.put("PRODUCTWEIGHT", w+"  Kg");
				map.put("TNUM", tnum+"  托");
				list.add(map);
			}else{
				map.put("FACTORYPRODUCTNAME", "总计：");
				map.put("PRODUCTWEIGHT", 0+"  Kg");
				map.put("TNUM", 0+"  托");
			}
			findPageInfo.put("footer", list);
			return GsonTools.toJson(findPageInfo);
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage(),e);
		}
		return GsonTools.toJson("");
	}
	
	/**
	 * 车间生产统计(产品大类、订单号、次号、车间)
	 * @param filter
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@ResponseBody
	@Journal(name = "车间生产统计(产品大类、订单号、批次号、车间)导出")
	@RequestMapping(value="export")
	public void export(Filter filter) throws Exception{
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
		
		Map<String, Object> map = totalStatisticsService.productsShopSummary(filter, page);
		List<Map<String, Object>> list = (List<Map<String, Object>>) map.get("rows");
		InputStream is = new FileInputStream(PathUtils.getClassPath() + "template/productsShopSummary.xlsx");
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
				case 0://计划单号
					cell.setCellValue(data.get("PRODUCEPLANCODE") == null ? "" : data.get("PRODUCEPLANCODE").toString());
					break;
				case 1://订单号码
					cell.setCellValue(data.get("SALESORDERCODE").toString());
					cell.setCellValue(data.get("CATEGORYNAME") == null ? "" : data.get("CATEGORYNAME").toString());
					break;
				case 2://批次号
					cell.setCellValue(data.get("BATCHCODE") == null ? "" : data.get("BATCHCODE").toString());
					break;
				case 3://车间
					cell.setCellValue(data.get("NAME").toString());
					cell.setCellValue(data.get("CATEGORYNAME") == null ? "" : data.get("CATEGORYNAME").toString());
					break;
				case 4://产品大类
					cell.setCellValue(data.get("CATEGORYNAME") == null ? "" : data.get("CATEGORYNAME").toString());
					break;
				case 5://成品类别代码
					cell.setCellValue(data.get("CATEGORYCODE") == null ? "" : data.get("CATEGORYCODE").toString());
					break;
				case 6://重量
					cell.setCellValue(data.get("PRODUCTWEIGHT") == null ? "" : data.get("PRODUCTWEIGHT").toString());
					break;
				case 7://托数量
					cell.setCellValue(data.get("TNUM") == null ? "0" : data.get("TNUM").toString());
					break;
				case 8://客户名称
					cell.setCellValue(data.get("CONSUMERNAME") == null ? "" : data.get("CONSUMERNAME").toString());
					break;
				case 9://客户产品名称
					cell.setCellValue(data.get("CONSUMERPRODUCTNAME") == null ? "" : data.get("CONSUMERPRODUCTNAME").toString());
					break;
				case 10://产品规格
					cell.setCellValue(data.get("PRODUCTMODEL") == null ? "" : data.get("PRODUCTMODEL").toString());
					break;
				case 11://质量等级
					cell.setCellValue(data.get("ROLLQUALITYGRADECODE") == null ? "" : data.get("ROLLQUALITYGRADECODE").toString());
					break;
				default:
					break;
				}
			}
		}
		HttpUtils.download(response,wb,"车间生产统计(产品大类、订单号、批次号、车间)");
		is.close();
	}
	
	
	
	/**
	 * 车间生产统计（产品大类、厂内名称）导出
	 * @param filter
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@ResponseBody
	@Journal(name = "车间生产统计（产品大类、厂内名称）导出")
	@RequestMapping(value="export1")
	public void export1(Filter filter) throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Page page =new Page();
		page.setAll(1);
		
		String start = filter.get("start");
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
		
		Map<String, Object> map = totalStatisticsService.genericFactorySummary(filter, page);
		List<Map<String, Object>> list = (List<Map<String, Object>>) map.get("rows");
		InputStream is = new FileInputStream(PathUtils.getClassPath() + "template/genericFactorySummary.xlsx");
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
				case 1://成品类别代码
					cell.setCellValue(data.get("CATEGORYCODE") == null ? "" : data.get("CATEGORYCODE").toString());
					break;
				case 2://客户产品名称
					cell.setCellValue(data.get("CONSUMERPRODUCTNAME") == null ? "" : data.get("CONSUMERPRODUCTNAME").toString());
					break;
				case 3://场内名称
					cell.setCellValue(data.get("FACTORYPRODUCTNAME") == null ? "" : data.get("FACTORYPRODUCTNAME").toString());
					break;
				case 4://车间名称
					cell.setCellValue(data.get("NAME") == null ? "" : data.get("NAME").toString());
					break;
				case 5://产品规格
					cell.setCellValue(data.get("PRODUCTMODEL") == null ? "" : data.get("PRODUCTMODEL").toString());
					break;
				case 6://托
					cell.setCellValue(data.get("TNUM") == null ? "0" : data.get("TNUM").toString());
					break;
				case 7://重量
					cell.setCellValue(data.get("PRODUCTWEIGHT") == null ? "0" : data.get("PRODUCTWEIGHT").toString());
					break;
				case 8://质量等级
					cell.setCellValue(data.get("ROLLQUALITYGRADECODE") == null ? "" : data.get("ROLLQUALITYGRADECODE").toString());
					break;
				default:
					break;
				}
			}
		}
		HttpUtils.download(response,wb,"车间生产统计（产品大类、厂内名称）");
		is.close();
	}
	
	/**
	 * 生成领料汇总导出
	 * @param filter
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@ResponseBody
	@Journal(name = "生产领料汇总导出")
	@RequestMapping(value="export2")
	public void export2(Filter filter) throws Exception{
		Page page =new Page();
		page.setAll(1);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//24小时制  
		if(filter.get("start")!=null){
		    long startTimes= simpleDateFormat.parse(filter.get("start").toString()).getTime(); 
			filter.set("start", String.valueOf(startTimes));
		}
		if(filter.get("end")!=null){
			long startTimes= simpleDateFormat.parse(filter.get("end")).getTime(); 
			filter.set("end", String.valueOf(startTimes));
		}
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		Map<String, Object> map = totalStatisticsService.getPickingStatistics(filter, page);
		List<Map<String, Object>> list = (List<Map<String, Object>>) map.get("rows");
		InputStream is = new FileInputStream(PathUtils.getClassPath() + "template/pickingStatistics.xlsx");
		Workbook wb = new SXSSFWorkbook(new XSSFWorkbook(is));
		Sheet sheet = wb.getSheetAt(0);
		Row row = null;
		Cell cell = null;
		
		int i = 1;
		
		for (Map<String, Object> data : list) {
			row = sheet.createRow(i++);
			for (int j = 0; j < 10; j++) {
				cell = row.createCell(j);
				switch (j) {
				case 0://原料大类
					cell.setCellValue(data.get("PRODUCECATEGORY") == null ? "" : data.get("PRODUCECATEGORY").toString());
					break;
				case 1://规格型号
					cell.setCellValue(data.get("MATERIALMODEL") == null ? "" : data.get("MATERIALMODEL").toString());
					break;
				case 2://重量
					cell.setCellValue(data.get("OUTWEIGHT").toString());
					break;
				case 3://出库时间
					if(data.get("OUTTIME")!=null){
						if(Long.parseLong(data.get("OUTTIME").toString())!=0){
							c.setTimeInMillis(Long.parseLong(data.get("OUTTIME").toString())); 
							Date date = (Date) c.getTime();
							cell.setCellValue(sf.format(date));
						}else{
							cell.setCellValue("");
						}
					}
					break;
				case 4://领料车间
					cell.setCellValue(data.get("WORKSHOP") == null ? "" :data.get("WORKSHOP").toString());
					break;
				default:
					break;
				}
			}
		}
		HttpUtils.download(response,wb,"生产领料汇总");
		is.close();
	}
}