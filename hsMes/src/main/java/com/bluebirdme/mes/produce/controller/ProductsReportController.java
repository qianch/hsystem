package com.bluebirdme.mes.produce.controller;

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
import com.bluebirdme.mes.produce.service.IFinishedProductService;

/**
 * 成品报表Controller
 * @author king
 *
 */
@Controller
@RequestMapping("/productsReport")
@Journal(name = "成品报表")
public class ProductsReportController extends BaseController {
	
	@Resource
	IFinishedProductService finishProductService;
	
	// 产成品汇总(成品类别)页面
	final String  productsSummaryView = "produce/productsSummary/productsSummary";
	// 产成品汇总页面（根据厂内名称）
	final String  fgSummaryView = "produce/finishedFactorySummary/finishedFactorySummary";
	// 产成品汇总页面（订单号、批次号、厂内名称）
	final String  produceSundrySummaryView = "produce/produceSundrySummary/produceSundrySummary";
	// 产成品汇总(按客户统计)
	final String pcssView = "produce/customerStockStatistics/customerStockStatistics";
	
	/**
	 * 产成品汇总(按客户统计)
	 * @return
	 */
	@Journal(name = "产成品汇总(按客户统计)")
	@RequestMapping(value = "pcssView",method = RequestMethod.GET)
	public String pcssView() {
		return pcssView;
	}
	
	/**
	 * 产成品汇总（订单号、批次号、厂内名称）
	 * @return
	 */
	@Journal(name = "产成品汇总（订单号、批次号、厂内名称）")
	@RequestMapping(value = "produceSundrySummaryView",method = RequestMethod.GET)
	public String produceSundrySummaryView() {
		return produceSundrySummaryView;
	}
	/**
	 * 产成品汇总（根据厂内名称）
	 * @return
	 */
	@Journal(name = "产成品汇总（根据厂内名称）")
	@RequestMapping(value = "fgSummaryView",method = RequestMethod.GET)
	public String finishedGoodsSummaryView() {
		return fgSummaryView;
	}
	/**
	 * 产成品汇总(成品类别)
	 * @return
	 */
	@Journal(name = "产成品汇总(成品类别)")
	@RequestMapping(value = "productsSummaryView",method = RequestMethod.GET)
	public String productsSummaryView() {
		return productsSummaryView;
	}
	
	/**
	 * 产成品汇总(按客户统计)
	 * @param filter
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@NoAuth
	@ResponseBody
	@Journal(name="产成品汇总(按客户统计)")
	@RequestMapping("pcsslist")
	public String getProductStock(Filter filter, Page page) throws Exception{
		String end = filter.get("end");
		if(end == null){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			filter.set("end", sdf.format(new Date()));//设定结束默认时间
		}
		Map<String, Object> findPageInfo =finishProductService.productsCustomerStockSummary(filter, page);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> rows = (List<Map<String, Object>>) findPageInfo.get("rows");
		DecimalFormat df = new DecimalFormat("#.00");
		if(rows.size() != 0){
			Double d = 0.0;
			page.setAll(1);
			Map<String, Object> map2 =finishProductService.productsCustomerStockSummary(filter, page);
			List<Map<String, Object>> r = (List<Map<String, Object>>) map2.get("rows");
			for (int i = 0; i < r.size(); i++) {
				if(r.get(i).get("WEIGHT")!=null&&r.get(i).get("WEIGHT")!=""){
					d += (Double)r.get(i).get("WEIGHT");
				}
			}
			Object o=df.format(d);
			map.put("FACTORYPRODUCTNAME", "总计");
			map.put("WEIGHT", o);
			list.add(map);
		}
		else{
			map.put("FACTORYPRODUCTNAME", "总计");
			map.put("WEIGHT", 0);
			list.add(map);
		}
		findPageInfo.put("footer", list);
		return GsonTools.toJson(findPageInfo);
	}
	
	@SuppressWarnings("unchecked")
	@NoAuth
	@ResponseBody
	@Journal(name = "产成品汇总(订单号、批次号、厂内名称)列表")
	@RequestMapping("productsSundrSummarylist")
	public String productsSundrSummary(Filter filter, Page page) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			String start = filter.get("start").toString();
			String end = filter.get("end");
			if(end == null){
				filter.set("end", sdf.format(new Date()));
				filter.set("end1", sdf.format(new Date()));
				filter.set("end2", sdf.format(new Date()));
			}else{
				filter.set("end", end);
				filter.set("end1", end);
				filter.set("end2", end);
			}
			if(!"".equals(start)){
				filter.set("startTime", start);
				filter.set("start", start);
				filter.set("start1", start);
				filter.set("start2", start);
			}
			//总计
			Map<String, Object> findPageInfo = finishProductService.productsSundrySummary(filter, page);
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			Map<String, Object> map = new HashMap<String, Object>();
			List<Map<String, Object>> rows = (List<Map<String, Object>>) findPageInfo.get("rows");
			DecimalFormat df = new DecimalFormat("#.00");
			findPageInfo.put("footer", list);
			if(rows.size() != 0){
				page.setAll(1);
				Map<String, Object> map2 = finishProductService.productsSundrySummary(filter, page);
				List<Map<String, Object>> r = (List<Map<String, Object>>) map2.get("rows");
				Double startWight = 0.0;
				Double inWight = 0.0;
				Double useWight = 0.0;
				Double outtWight = 0.0;
				Double aum = 0.0;
				for(int i=0;i<r.size();i++){
					startWight += (Double)r.get(i).get("STARTWEIGHT");
					inWight += (Double)r.get(i).get("INWEIGHT");
					useWight += (Double)r.get(i).get("USENUM");
					outtWight += (Double)r.get(i).get("OUTWEIGHT");
					aum += (Double)r.get(i).get("ATUM");
				}
				
				Object s = df.format(startWight);
				Object i = df.format(inWight);
				Object u = df.format(useWight);
				Object o = df.format(outtWight);
				Object a = df.format(aum);
				map.put("SALESORDERCODE", "总计：");
				map.put("STARTWEIGHT", s+"  KG");
				map.put("INWEIGHT", i+"  KG");
				map.put("USENUM", u+"  KG");
				map.put("OUTWEIGHT", o+"  KG");
				map.put("ATUM", a+"  KG");
				list.add(map);
			}else{
				map.put("SALESORDERCODE", "总计：");
				map.put("STARTWEIGHT", 0+"  KG");
				map.put("INWEIGHT", 0+"  KG");
				map.put("USENUM", 0+"  KG");
				map.put("OUTWEIGHT", 0+"  KG");
				map.put("ATUM", 0+"  KG");
				list.add(map);
			}
			findPageInfo.put("footer", list);
			return GsonTools.toJson(findPageInfo);
		} catch (Exception e) {
		}
		return GsonTools.toJson("");
	}
	
	@NoAuth
	@ResponseBody
	@Journal(name = "产成品汇总(成品类别)列表")
	@RequestMapping("productsSummarylist")
	public String getFinishProduct(Filter filter, Page page) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			String start = filter.get("start").toString();
			String end = filter.get("end");
			if(end == null){
				filter.set("end", sdf.format(new Date()));
				filter.set("end1", sdf.format(new Date()));
				filter.set("end2", sdf.format(new Date()));
			}else{
				filter.set("end", end);
				filter.set("end1", end);
				filter.set("end2", end);
			}
			if(!"".equals(start)){
				filter.set("startTime", start);
				filter.set("start", start);
				filter.set("start1", start);
				filter.set("start2", start);
			}
			return GsonTools.toJson(finishProductService.productsSummary(filter, page));
		} catch (Exception e) {
		}
		return GsonTools.toJson("");
	}
	
	@SuppressWarnings("unchecked")
	@NoAuth
	@ResponseBody
	@Journal(name = "产成品汇总(厂内名称)列表")
	@RequestMapping("pfsummarylist")
	public String getFinishFactoryProduct(Filter filter, Page page) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			String start = filter.get("start");
			String end = filter.get("end");
			if(end == null){
				filter.set("end", sdf.format(new Date()));
				filter.set("end1", sdf.format(new Date()));
				filter.set("end2", sdf.format(new Date()));
			}else{
				filter.set("end", end);
				filter.set("end1", end);
				filter.set("end2", end);
			}
			if(start != null){
				filter.set("startTime", start);
				filter.set("start", start);
				filter.set("start1", start);
				filter.set("start2", start);
			}
			//总计
			Map<String, Object> findPageInfo = finishProductService.productsFactorySummary(filter, page);
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			Map<String, Object> map = new HashMap<String, Object>();
			List<Map<String, Object>> rows = (List<Map<String, Object>>) findPageInfo.get("rows");
			DecimalFormat df = new DecimalFormat("#.00");
			findPageInfo.put("footer", list);
			if(rows.size() != 0){
				page.setAll(1);
				Map<String, Object> map2 = finishProductService.productsFactorySummary(filter, page);
				List<Map<String, Object>> r = (List<Map<String, Object>>) map2.get("rows");
				Double startWight = 0.0;
				Double inWight = 0.0;
				Double useWight = 0.0;
				Double outtWight = 0.0;
				Double aum = 0.0;
				for(int i=0;i<r.size();i++){
					startWight += (Double)r.get(i).get("STARTWEIGHT");
					inWight += (Double)r.get(i).get("INWEIGHT");
					useWight += (Double)r.get(i).get("USENUM");
					outtWight += (Double)r.get(i).get("OUTWEIGHT");
					aum += (Double)r.get(i).get("ATUM");
				}
				
				Object s = df.format(startWight);
				Object i = df.format(inWight);
				Object u = df.format(useWight);
				Object o = df.format(outtWight);
				Object a = df.format(aum);
				map.put("CATEGORYNAME", "总计：");
				map.put("STARTWEIGHT", s+"  KG");
				map.put("INWEIGHT", i+"  KG");
				map.put("USENUM", u+"  KG");
				map.put("OUTWEIGHT", o+"  KG");
				map.put("ATUM", a+"  KG");
				list.add(map);
			}else{
				map.put("CATEGORYNAME", "总计：");
				map.put("STARTWEIGHT", 0+"  KG");
				map.put("INWEIGHT", 0+"  KG");
				map.put("USENUM", 0+"  KG");
				map.put("OUTWEIGHT", 0+"  KG");
				map.put("ATUM", 0+"  KG");
				list.add(map);
			}
			findPageInfo.put("footer", list);
			return GsonTools.toJson(findPageInfo);
		} catch (Exception e) {
		}
		return GsonTools.toJson("");
	}
	
	@SuppressWarnings("unchecked")
	@ResponseBody
	@Journal(name = "产成品汇总(成品类别)导出")
	@RequestMapping(value="export")
	public void export(Filter filter) throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Page page =new Page();
		page.setAll(1);
		//产品大类
		String name = filter.get("name");
		//类别代码
		String code = filter.get("code");
		//开始时间
		String start = filter.get("start");
		//结束时间
		String end = filter.get("end");
		
		if(!"".equals(name) && name != null){
			filter.getFilter().put("name", name);
		}
		if(!"".equals(code) && code != null){
			filter.getFilter().put("code", code);
		}
		if(!"".equals(start) && start != null){
			filter.getFilter().put("startTime", start);
			filter.getFilter().put("start", start);
			filter.getFilter().put("start1", start);
			filter.getFilter().put("start2", start);
		}
		if(end == ""){
			filter.getFilter().put("end", sdf.format(new Date()));
			filter.getFilter().put("end1", sdf.format(new Date()));
			filter.getFilter().put("end2", sdf.format(new Date()));
		}else{
			filter.getFilter().put("end", end);
			filter.getFilter().put("end1", end);
			filter.getFilter().put("end2", end);
		}
		
		Map<String, Object> map = finishProductService.productsSummary(filter, page);
		List<Map<String, Object>> list = (List<Map<String, Object>>) map.get("rows");
		InputStream is = new FileInputStream(new File(PathUtils.getClassPath() + "template/productCategorySummary.xlsx"));

		Workbook wb = new XSSFWorkbook(is);
		Sheet sheet = wb.getSheetAt(0);
		Row row = null;
		Cell cell = null;
		
		int i = 1;
		
		for (Map<String, Object> data : list) {
			row = sheet.createRow(i++);
			for (int j = 0; j < 10; j++) {
				cell = row.createCell(j);
				switch (j) {
				case 0:
					cell.setCellValue(data.get("CATEGORYNAME").toString());//产品大类
					break;
				case 1:
					cell.setCellValue(data.get("CATEGORYCODE").toString());//产品类别代码
					break;
				case 2:
					cell.setCellValue(data.get("STARTWEIGHT").toString());//月初库存数量
					break;
				case 3:
					cell.setCellValue(data.get("INWEIGHT").toString());//当月入库数量
					break;
				case 4:
					cell.setCellValue(data.get("OUTWEIGHTS").toString());//当月领用数量
					break;
				case 5:
					cell.setCellValue(data.get("USENUM").toString());//当月发出数量
					break;
				case 6:
					cell.setCellValue(data.get("ATUM").toString());//月末累计数量
					break;
				default:
					break;
				}
			}
		}
		HttpUtils.download(response,wb,"成品大类汇总(类别)");
		is.close();
	}
	@SuppressWarnings("unchecked")
	@ResponseBody
	@Journal(name = "产成品汇总（厂内名称）导出")
	@RequestMapping(value="export1")
	public void export1(Filter filter) throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Page page =new Page();
		page.setAll(1);
		//厂内名称
		String factorName = filter.get("factoryProductName");
		//客户产品名称
		String consumerProductName = filter.get("consumerProductName");
		//产品大类
		String name = filter.get("name");
		//类别代码
		String code = filter.get("code");
		//开始时间
		String start = filter.get("start");
		//结束时间
		String end = filter.get("end");
		
		if(!"".equals(name) && name != null){
			filter.getFilter().put("name", name);
		}
		if(!"".equals(code) && code != null){
			filter.getFilter().put("code", code);
		}
		
		if(!"".equals(factorName) && name != null){
			filter.getFilter().put("factoryProductName", factorName);
		}
		if(!"".equals(consumerProductName) && code != null){
			filter.getFilter().put("consumerProductName", consumerProductName);
		}
		
		if(!"".equals(start) && start != null){
			filter.getFilter().put("startTime", start);
			filter.getFilter().put("start", start);
			filter.getFilter().put("start1", start);
			filter.getFilter().put("start2", start);
		}
		if(end == ""){
			filter.getFilter().put("end", sdf.format(new Date()));
			filter.getFilter().put("end1", sdf.format(new Date()));
			filter.getFilter().put("end2", sdf.format(new Date()));
		}else{
			filter.getFilter().put("end", end);
			filter.getFilter().put("end1", end);
			filter.getFilter().put("end2", end);
		}
		
		Map<String, Object> map = finishProductService.productsFactorySummary(filter, page);
		List<Map<String, Object>> list = (List<Map<String, Object>>) map.get("rows");
		InputStream is = new FileInputStream(new File(PathUtils.getClassPath() + "template/productFactorySummary.xlsx"));

		Workbook wb = new XSSFWorkbook(is);
		Sheet sheet = wb.getSheetAt(0);
		Row row = null;
		Cell cell = null;
		
		int i = 1;
		
		for (Map<String, Object> data : list) {
			row = sheet.createRow(i++);
			for (int j = 0; j < 10; j++) {
				cell = row.createCell(j);
				switch (j) {
				case 0:
					cell.setCellValue(data.get("CATEGORYNAME").toString());//产品大类
					break;
				case 1:
					cell.setCellValue(data.get("CATEGORYCODE").toString());//产品类别代码
					break;
				case 2:
					
					cell.setCellValue(data.get("FACTORYPRODUCTNAME")==null ? "" : data.get("FACTORYPRODUCTNAME").toString());//厂内名称
					break;
				case 3:
					cell.setCellValue(data.get("CONSUMERPRODUCTNAME") == null ? "" : data.get("CONSUMERPRODUCTNAME").toString());//客户产品名称
					break;
				case 4:
					cell.setCellValue(data.get("STARTWEIGHT").toString());//月初库存数量
					break;
				case 5:
					cell.setCellValue(data.get("INWEIGHT").toString());//当月入库数量
					break;
				case 6:
					cell.setCellValue(data.get("USENUM").toString());//当月领用数量
					break;
				case 7:
					cell.setCellValue(data.get("OUTWEIGHTS") == null ? "" : data.get("OUTWEIGHTS").toString());//当月发出数量
					break;
				case 8:
					cell.setCellValue(data.get("ATUM").toString());//月末累计数量
					break;
				default:
					break;
				}
			}
		}
		HttpUtils.download(response,wb,"成品大类汇总(厂内名称)");
		is.close();
	}
	
	@SuppressWarnings("unchecked")
	@ResponseBody
	@Journal(name = "产成品汇总（订单号、批次号、厂内名称）导出")
	@RequestMapping(value="export2")
	public void export2(Filter filter) throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Page page =new Page();
		page.setAll(1);
		//开始时间
		String start = filter.get("start");
		//结束时间
		String end = filter.get("end");
		
		if(!"".equals(start) && start != null){
			filter.getFilter().put("startTime", start);
			filter.getFilter().put("start", start);
			filter.getFilter().put("start1", start);
			filter.getFilter().put("start2", start);
		}
		if(end == "" || end == null){
			filter.getFilter().put("end", sdf.format(new Date()));
			filter.getFilter().put("end1", sdf.format(new Date()));
			filter.getFilter().put("end2", sdf.format(new Date()));
		}else{
			filter.getFilter().put("end", end);
			filter.getFilter().put("end1", end);
			filter.getFilter().put("end2", end);
		}
		
		Map<String, Object> map = finishProductService.productsSundrySummary(filter, page);
		List<Map<String, Object>> list = (List<Map<String, Object>>) map.get("rows");
		InputStream is = new FileInputStream(new File(PathUtils.getClassPath() + "template/productSundrSummary.xlsx"));

		Workbook wb = new XSSFWorkbook(is);
		Sheet sheet = wb.getSheetAt(0);
		Row row = null;
		Cell cell = null;
		
		int i = 1;
		
		for (Map<String, Object> data : list) {
			row = sheet.createRow(i++);
			for (int j = 0; j < 12; j++) {
				cell = row.createCell(j);
				switch (j) {
				case 0:
					cell.setCellValue(data.get("SALESORDERCODE").toString());//订单号
					break;
				case 1:
					cell.setCellValue(data.get("BATCHCODE").toString());//批次号
					break;
				case 2:
					cell.setCellValue(data.get("FACTORYPRODUCTNAME").toString() == null ? "" : data.get("FACTORYPRODUCTNAME").toString());
					break;
				case 3://产品型号
					String model = data.get("PRODUCTISTC").toString();
					if(model == "1"){
						cell.setCellValue("套材");
					}else if(model == "2"){
						cell.setCellValue("非套材");
					}else if(model == "-1"){
						cell.setCellValue("胚布");
					}else{
						cell.setCellValue("");
					}
					break;
				case 4:
					cell.setCellValue(data.get("CATEGORYNAME") == null ? "" : data.get("CATEGORYNAME").toString());//产品大类
					break;
				case 5:
					cell.setCellValue(data.get("CATEGORYCODE") == null ? "" : data.get("CATEGORYCODE").toString());//产品类别代码
					break;
				case 6:
					cell.setCellValue(data.get("STARTWEIGHT") == null ? "" : data.get("STARTWEIGHT").toString());//月初库存数量
					break;
				case 7:
					cell.setCellValue(data.get("INWEIGHT") == null ? "" : data.get("INWEIGHT").toString());//当月入库数量
					break;
				case 8:
					cell.setCellValue(data.get("OUTWEIGHT") == null ? "" : data.get("OUTWEIGHT").toString());//当月领用数量
					break;
				case 9:
					cell.setCellValue(data.get("USENUM") == null ? "" : data.get("USENUM").toString());//当月发出数量
					break;
				case 10:
					cell.setCellValue(data.get("ATUM") == null ? "" : data.get("ATUM").toString());//月末累计数量
					break;
				default:
					break;
				}
			}
		}
		HttpUtils.download(response,wb,"产成品汇总（订单号、批次号、厂内名称）");
		is.close();
	}
	
	@SuppressWarnings("unchecked")
	@ResponseBody
	@Journal(name = "产成品汇总(按客户统计)导出")
	@RequestMapping(value="export3")
	public void export3(Filter filter) throws Exception{
		Page page =new Page();
		page.setAll(1);
		String end = filter.get("end");
		if(end == null){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			filter.set("end", sdf.format(new Date()));
		}
		
		Map<String, Object> map = finishProductService.productsCustomerStockSummary(filter, page);
		List<Map<String, Object>> list = (List<Map<String, Object>>) map.get("rows");
		InputStream is = new FileInputStream(PathUtils.getClassPath() + "template/productsCustomerStockSummary.xlsx");
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
				case 0://条码号
					cell.setCellValue(data.get("BARCODE").toString() == null ? "" : data.get("BARCODE").toString());
					break;
				case 1://订单号
					cell.setCellValue(data.get("SALESORDERCODE").toString() == null ? "" : data.get("SALESORDERCODE").toString());

					break;
				case 2://批次号
					cell.setCellValue(data.get("BATCHCODE").toString() == null ? "" : data.get("FACTORYPRODUCTNAME").toString());
					break;
				case 3://发货日期
					cell.setCellValue(data.get("PLANDELIVERYDATE") == null ? "" : data.get("PLANDELIVERYDATE").toString());
					break;
				case 4://厂内名称
					cell.setCellValue(data.get("FACTORYPRODUCTNAME") == null ? "" : data.get("FACTORYPRODUCTNAME").toString());
					break;
				case 5://客户产品名称
					cell.setCellValue(data.get("CONSUMERPRODUCTNAME") == null ? "" : data.get("CONSUMERPRODUCTNAME").toString());
					break;
				case 6://部件名称
					cell.setCellValue(data.get("TCPROCBOMVERSIONPARTSNAME") == null ? "" : data.get("TCPROCBOMVERSIONPARTSNAME").toString());
					break;
				case 7://产品规格
					cell.setCellValue(data.get("PRODUCTMODEL") == null ? "" : data.get("PRODUCTMODEL").toString());
					break;
				case 8://客户名称
					cell.setCellValue(data.get("CONSUMERNAME") == null ? "" : data.get("CONSUMERNAME").toString());
					break;
				case 9://重量
					cell.setCellValue(data.get("WEIGHT") == null ? "" : data.get("WEIGHT").toString());
					break;
				case 10://仓库
					cell.setCellValue(data.get("WAREHOUSECODE") == null ? "" : data.get("WAREHOUSECODE").toString());
					break;
				case 11://库位
					cell.setCellValue(data.get("WAREHOUSEPOSCODE") == null ? "" : data.get("WAREHOUSEPOSCODE").toString());
					break;
				case 12://入库时间
					cell.setCellValue(data.get("INTIME") == null ? "" : data.get("INTIME").toString());
					break;
				case 13://在库天数
					cell.setCellValue(data.get("DAYS") == null ? "" : data.get("DAYS").toString());
					break;
				case 14://保质期
					cell.setCellValue(data.get("PRODUCTSHELFLIFE") == null ? "" : data.get("PRODUCTSHELFLIFE").toString());
					break;
				case 15://质量状态
					String state = data.get("STOCKSTATE").toString();
					if(state == "1"){
						cell.setCellValue("在库");
					}else if(state == "-1"){
						cell.setCellValue("不在库");
					}else{
						cell.setCellValue("");
					}
					break;
				case 16://质量等级
					cell.setCellValue(data.get("ROLLQUALITYGRADECODE") == null ? "" : data.get("ROLLQUALITYGRADECODE").toString());
					break;
				default:
					break;
				}
			}
		}
		HttpUtils.download(response,wb,"产成品汇总(按客户统计)");
		is.close();
	}
}
