/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.planner.weave.controller;

import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.annotation.NoAuth;
import com.bluebirdme.mes.core.annotation.support.LogType;
import com.bluebirdme.mes.core.base.controller.BaseController;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.constant.Constant;
import com.bluebirdme.mes.core.valid.annotations.Valid;
import com.bluebirdme.mes.planner.weave.entity.WeavePlan;
import com.bluebirdme.mes.planner.weave.entity.WeavePlanDevices;
import com.bluebirdme.mes.planner.weave.service.IWeavePlanService;
import com.bluebirdme.mes.utils.HttpUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 编织计划Controller
 * @author 肖文彬
 * @Date 2016-10-18 13:37:59
 */
@Controller
@RequestMapping("planner/weavePlan")
@Journal(name="编织计划")
public class WeavePlanController extends BaseController {

	// 编织计划页面
	final String index = "planner/weave/weavePlan";
	final String addOrEdit="planner/weave/weavePlanAddOrEdit";
	final String addDevice="planner/weave/addDevice";
	final String choose_Device = "planner/weave/device";
	final String select="planner/weave/weaveSelect";
	final String selecttwo="planner/weave/weaveSelect_two";
	final String finishProduce="planner/weave/finishProducePage";
	final String viewBjInfo = "planner/scheduling/checkBjInfo";
	final String bjhzview = "planner/scheduling/checkBjHzInfo";

	@Resource IWeavePlanService weavePlanService;
	

	@Journal(name = "首页")
	@RequestMapping(method = RequestMethod.GET)
	public String index() {
		return index;
	}
	
	@NoAuth
	@ResponseBody
	@Journal(name="获取编织计划列表信息")
	@RequestMapping("list")
	public String getWeavePlan(String planCode) throws Exception{
		return GsonTools.toJson(weavePlanService.findWeavePlan(planCode));
	}
	
	@NoAuth
	@ResponseBody
	@Journal(name="获取编织计划列表信息")
	@RequestMapping("weaveList")
	public String getWeavePlans(Filter filter, Page page) throws Exception{
		if (filter.get("isFinish")==null) {
			filter.set("isFinish", "-1");
		}
		if (filter.get("closed")==null) {
			filter.set("closed", "0");
		}
		if(filter.get("isTurnBagPlan")==null){
			filter.set("isTurnBagPlan", "生产");
		}
		Map<String, Object> findPageInfo = weavePlanService.findWeavePageInfo(filter, page);
		
		return GsonTools.toJson(findPageInfo);
	}

	@NoAuth
	@ResponseBody
	@Journal(name="获取机台裁剪列表信息")
	@RequestMapping("cutList1")
	public String getDeviceCut(Filter filter, Page page) throws Exception{

		Map<String, Object> findPageInfo2 = weavePlanService.findWeavePageInfo2(filter, page);

		return GsonTools.toJson(findPageInfo2);
	}

	@Journal(name="选择计划页面")
	@RequestMapping(value="select",method=RequestMethod.GET)
	public ModelAndView select(String workShop){
		System.out.println(workShop);
		return new ModelAndView(select,model.addAttribute("workShop", workShop));
	}
	
	
	@Journal(name="选择套材计划页面")
	@RequestMapping(value="selecttwo",method=RequestMethod.GET)
	public ModelAndView selecttwo(String workShop){
		System.out.println(workShop);
		return new ModelAndView(selecttwo,model.addAttribute("workShop", workShop));
	}
	
	
	@Journal(name="生产完成确定页面")
	@RequestMapping(value="finishProduce",method=RequestMethod.GET)
	public ModelAndView finishProduce(String id){
		return new ModelAndView(finishProduce,model.addAttribute("id", id));
	}
	@ResponseBody
	@Journal(name="生产订单完成获取详情")
	@RequestMapping(value="findfinished",method=RequestMethod.POST)
	public String findfinished(String id) throws Exception{
		Page page=new Page();
		page.setAll(1);
		Filter filter = new Filter();
		HashMap<String, String> hm = new HashMap<>();
		hm.put("id", id);
		filter.setFilter(hm);
		Map<String,Object> map=weavePlanService.findfinished(filter, page);
		String json = GsonTools.toJson(map);
		return json;
	}
	@ResponseBody
	@Journal(name="查询未完成的编织计划")
	@RequestMapping(value="findUnCompletedWeavePlan",method=RequestMethod.POST)
	public String findUnCompletedWeavePlan(Filter filter,Page page) throws Exception{
		Map<String, Object> map=weavePlanService.findNofinish(filter, page);
		return GsonTools.toJson(map);
	}
	
	

	@Journal(name="添加编织计划页面",logType=LogType.DB)
	@RequestMapping(value="add",method=RequestMethod.GET)
	public ModelAndView _add(WeavePlan weavePlan){
		return new ModelAndView(addOrEdit,model.addAttribute("weavePlan", weavePlan));
	}
	
	@ResponseBody
	@Journal(name="加载编织计划")
	@RequestMapping(value="findWeavePlan",method=RequestMethod.POST)
	@Valid
	public String add() throws Exception{
		return GsonTools.toJson(weavePlanService.PlanCodeCombobox());
	}
	
	@Journal(name="编辑编织计划页面",logType=LogType.DB)
	@RequestMapping(value="edit",method=RequestMethod.GET)
	public ModelAndView _edit(Long id){/*
		WeavePlan weavePlan = weavePlanService.findById(WeavePlan.class, id);
		Map<String, Object> map= new HashMap<String, Object>();
		map.put("producePlanId", weavePlan.getProducePlanDetailId());
		List<WeavePlanDevices> list=weavePlanService.findListByMap(WeavePlanDevices.class,map);
		if(list.size()==0){
			return new ModelAndView(addOrEdit, model.addAttribute("weavePlan", weavePlan));
		}else{
			StringBuffer sb=new StringBuffer();
			StringBuffer sb1=new StringBuffer();
			for (int i = 0; i < list.size(); i++) {
				sb1.append((i==0?"":",")+list.get(i).getDeviceId());
				Map<String, Object> map1= new HashMap<String, Object>();
				map1.put("id", list.get(i).getDeviceId());
				List<Device> userList=weavePlanService.findListByMap(Device.class, map1);
				for(int j=0;j<userList.size();j++){
					sb.append((i==0?"":",")+userList.get(j).getDeviceName());
				}
			}
			weavePlan=weavePlanService.findById(WeavePlan.class, weavePlan.getId());
			return new ModelAndView(addOrEdit, model.addAttribute("weavePlan", weavePlan).addAttribute("deviceName", sb.toString()).addAttribute("deviceId",sb1.toString()));
		}
		
	*/
		return null;	
	}
	
	@Deprecated
	@ResponseBody
	@Journal(name="编辑编织计划",logType=LogType.DB)
	@RequestMapping(value="edit",method=RequestMethod.POST)
	public String edit(WeavePlan weavePlan,Long[] did,Integer[] dCount,String time) throws Exception{
		/*if(weavePlan.getId()==null){
			weavePlanService.save(weavePlan, did, dCount,time);
		}else{
			weavePlanService.updateWeave(weavePlan, did, dCount,time);
		}*/
		return ajaxSuccess();
	}

	@ResponseBody
	@Journal(name="查询编织计划下的机台信息")
	@RequestMapping(value="device",method=RequestMethod.POST)
	public String find(Long wid,String date,String workshop,Long id) throws Exception{
		List<Map<String,Object>> li=weavePlanService.findDevice(wid,date,workshop,id);
		return GsonTools.toJson(li);
	}
	
//	@ResponseBody
//	@Journal(name="更新index",logType=LogType.DB)
//	@RequestMapping(value="update",method=RequestMethod.POST)
//	@Valid
//	public String edit(Long id, Long index) throws Exception{
//		WeavePlan weavePlan = weavePlanService.findById(WeavePlan.class, id);
//		weavePlan.setSort(index);
//		weavePlanService.update(weavePlan);
//		return Constant.AJAX_SUCCESS;
//	}
	
	/**
	 * EASYUI组件DataList
	 * @param filter
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@NoAuth
	@ResponseBody
	@Journal(name = "获取生产计划列表信息")
	@RequestMapping("datalist")
	public String datalist(Filter filter, Page page) throws Exception {
		return GsonTools.toJson(weavePlanService.dataList(filter, page));
	}
	
	@ResponseBody
	@Journal(name="编织计划标记已完成",logType=LogType.DB)
	@RequestMapping(value="isFinish",method=RequestMethod.POST)
	@Valid
	public String finish(String ids) throws Exception{
		weavePlanService.updateState(ids);
		return Constant.AJAX_SUCCESS;
	}
	
	@ResponseBody
	@Journal(name="编辑计划取消完成",logType=LogType.DB)
	@RequestMapping(value="iscloseFinish",method=RequestMethod.POST)
	@Valid
	public String iscloseFinish(String ids) throws Exception{
		if(ids!=null){
			for(String id:ids.split(",")){
				WeavePlan w=weavePlanService.findById(WeavePlan.class, Long.parseLong(id));
				w.setIsFinished(-1);
				weavePlanService.update2(w);
			}
		}
		return Constant.AJAX_SUCCESS;
	}
	
	@ResponseBody
	@Journal(name="编辑计划取消关闭",logType=LogType.DB)
	@RequestMapping(value="isCancelClose",method=RequestMethod.POST)
	public String isCancelClose(String ids) throws Exception{
		for(String id:ids.split(",")){
			WeavePlan w=weavePlanService.findById(WeavePlan.class, Long.parseLong(id));
			w.setIsFinished(-1);
			w.setClosed(0);
			weavePlanService.update2(w);
		}
		return Constant.AJAX_SUCCESS;
	}
	
	@ResponseBody
	@Journal(name="优先排序",logType=LogType.DB)
	@RequestMapping(value="sort",method=RequestMethod.POST)
	@Valid
	public String sort(String id) throws Exception{
		WeavePlan weavePlan=weavePlanService.findById(WeavePlan.class, Long.valueOf(id));
		/*Map<String, Object> map= new HashMap<String, Object>();
		map.put("weavePlanId", weavePlan.getId());*/
/*		List<Devices> list=weavePlanService.findListByMap(Devices.class, map);
		if(list.size()==0){
			return ajaxError("此编织计划下没有分配机台！");
		}*/
		Long time = System.currentTimeMillis();
		weavePlanService.updateSort(id,time);
		return Constant.AJAX_SUCCESS;
	}

	@ResponseBody
	@Journal(name="取消优先排序",logType=LogType.DB)
	@RequestMapping(value="cancelSort",method=RequestMethod.POST)
	@Valid
	public String cancelSort(String id) throws Exception{
		WeavePlan weavePlan=weavePlanService.findById(WeavePlan.class, Long.valueOf(id));
		Long time = null;
		weavePlanService.updateSort(id,time);
		return Constant.AJAX_SUCCESS;
	}
	
	@RequestMapping("device")
	public String chooseDevice(String singleSelect, String workShop) {
		model.addAttribute("singleSelect", singleSelect).addAttribute("workShop", workShop);
		return choose_Device;
	}
	
	@Journal(name="打开分配机台页面")
	@RequestMapping(value="addDevice",method=RequestMethod.GET)
	public ModelAndView devices(Long weavePlanId,String date,String workshop){/*
		if(weavePlanId==null){
			return new ModelAndView(addDevice);
		}
		WeavePlan weavePlan = weaveDailyPlanService.findById(WeavePlan.class, weavePlanId);
		Map<String, Object> map= new HashMap<String, Object>();
		map.put("weavePlanId", weavePlan.getId());
		map.put("produceDate", date);
		map.put("workshop",workshop);
		List<WeavePlanDevices> list=weaveDailyPlanService.findListByMap(WeavePlanDevices.class,map);
		List<Map<String,Object>> deviceDg=new ArrayList<Map<String,Object>>();
		Device _device=null;
		Map<String,Object> device;
		for(WeavePlanDevices d:list){
			_device=weavePlanService.findById(Device.class, d.getDeviceId());
			if(_device!=null){
				device=new HashMap<String,Object>();
				device.put("DEVICEID", _device.getId());
				device.put("DEVICENAME", _device.getDeviceName());
				device.put("DEVICECODE", _device.getDeviceCode());
				device.put("PRODUCECOUNT", d.getProduceCount());
				deviceDg.add(device);
			}
		}
		return new ModelAndView(addDevice, model.addAttribute("weavePlan", weavePlan).addAttribute("deviceDg", GsonTools.toJson(deviceDg)));
	*/
		return null;
	}
	
	@Journal(name="机台排产")
	@RequestMapping(value="addDevice",method=RequestMethod.POST)
	public String savePlanDevices(@RequestBody List<WeavePlanDevices> devices){
		weavePlanService.saveDevices(devices);
		return ajaxSuccess();
	}


	@NoAuth
	@ResponseBody
	@Journal(name = "导出编织计划信息")
	@RequestMapping(value="export")
	public void getProductStockExport1(Filter filter) throws Exception{
		Page page=new Page();
		page.setRows(99999);
		if (filter.get("isFinish")==null) {
			filter.set("isFinish", "-1");
		}
		if (filter.get("closed")==null) {
			filter.set("closed", "0");
		}
		if(filter.get("isTurnBagPlan")==null){
			filter.set("isTurnBagPlan", "生产");
		}
		Map<String, Object> weavePlan=weavePlanService.findWeavePageInfo(filter, page);
		//System.out.println("记录数="+list.size());
		List<Map<String, Object>> list = (List) weavePlan.get("rows");
		SimpleDateFormat sf = new SimpleDateFormat("yyyy年MM月dd日");
		String templateName = "编织计划记录单";
		SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日");
		Workbook wb = new SXSSFWorkbook();
		CellStyle cellStyle = wb.createCellStyle();
		cellStyle.setAlignment(HorizontalAlignment.CENTER); // 水平布局：居中
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		cellStyle.setBorderBottom(BorderStyle.THIN);
		cellStyle.setBorderTop(BorderStyle.THIN);
		cellStyle.setBorderRight(BorderStyle.THIN);
		cellStyle.setBorderLeft(BorderStyle.THIN);
		cellStyle.setWrapText(true);

		Sheet sheet = wb.createSheet();
		// sheet.setDisplayGridlines(true);
		Row row = null;
		Cell cell = null;
		String columnName[] = new String[] {"完成状态","关闭状态","生产计划单号","分配状态","销售订单号",
				"批次号", "工艺名称","厂内产品名称","客户产品名称","门幅(mm)","卷长(m)","预留长度(m)","卷重(kg)","部件名称",
				"图号","卷号","层号","计划数量（卷）","生产进度","打包托数/总托数","客户简称","产品属性","出货日期","工艺代码","工艺版本","包装代码","包装版本","包装要求","备注"};
		int r = 0;// 从第1行开始写数据
		row = sheet.createRow(r);
		cell = row.createCell(0);
		cell.setCellValue("浙江恒石纤维基业有限公司编织计划表");
		cell.setCellStyle(cellStyle);
		for (int i = 1; i < 14; i++) {
			cell = row.createCell(i);
			cell.setCellStyle(cellStyle);
		}
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 28));
		r++;
		
		row = sheet.createRow(r);
		sheet.setColumnWidth(0, 10 * 256);// 设置列宽
		sheet.setColumnWidth(1, 10 * 256);
		sheet.setColumnWidth(2, 25 * 256);
		sheet.setColumnWidth(3, 18 * 256);
		sheet.setColumnWidth(4, 18 * 256);
		sheet.setColumnWidth(5, 24 * 256);
		sheet.setColumnWidth(6, 20 * 256);
		sheet.setColumnWidth(7, 20 * 256);
		sheet.setColumnWidth(8,20 * 256);
		sheet.setColumnWidth(9, 15 * 256);
		sheet.setColumnWidth(10, 15 * 256);
		sheet.setColumnWidth(11, 15 * 256);
		sheet.setColumnWidth(12, 15 * 256);
		sheet.setColumnWidth(13, 15 * 256);
		sheet.setColumnWidth(14, 15 * 256);
		sheet.setColumnWidth(15, 15 * 256);
		sheet.setColumnWidth(16, 15 * 256);
		sheet.setColumnWidth(17, 15 * 256);
		sheet.setColumnWidth(18, 15 * 256);
		sheet.setColumnWidth(19, 15 * 256);
		sheet.setColumnWidth(20, 15 * 256);
		sheet.setColumnWidth(21, 15 * 256);
		sheet.setColumnWidth(22, 15 * 256);
		sheet.setColumnWidth(23, 15 * 256);
		sheet.setColumnWidth(24, 15 * 256);
		sheet.setColumnWidth(25, 40* 256);
		sheet.setColumnWidth(26, 15 * 256);
		sheet.setColumnWidth(27, 15 * 256);
		sheet.setColumnWidth(28, 25 * 256);
		for (int a = 0; a < columnName.length; a++) {
			cell = row.createCell(a);
			cell.setCellValue(columnName[a]);
			cell.setCellStyle(cellStyle);
		}
		r++;

		for (Map<String, Object> data : list) {
			row = sheet.createRow(r);
			for (int j = 0; j < columnName.length; j++) {
				cell = row.createCell(j);
				switch (j) {
				case 0:
					if (data.get("ISFINISHED") != null) {
						if(Integer.valueOf(data.get("ISFINISHED").toString())==1){
							
							cell.setCellValue("已完成");
						}else{
							cell.setCellValue("未完成");
						}
					}
					break;
				case 1:
					if (data.get("CLOSED") == null ||Integer.valueOf(data.get("CLOSED").toString())==0){
						
							cell.setCellValue("正常");
					}else if(Integer.valueOf(data.get("CLOSED").toString())==1){
						cell.setCellValue("已关闭");
					}else{
						cell.setCellValue("");
					}
					break;
				case 2:
					if (data.get("PLANCODE") != null) {
						cell.setCellValue(data.get("PLANCODE").toString());
					}
					break;
				case 3:
					if (data.get("ISPLANED") == null ||Integer.valueOf(data.get("ISPLANED").toString())==0){
						
						cell.setCellValue("未分配");
					}else if(Integer.valueOf(data.get("ISPLANED").toString())==1){
					cell.setCellValue("已分配");
						}else{
							cell.setCellValue("");
						}
					break;
				case 4:
					if (data.get("SALESORDERSUBCODE") != null) {
						cell.setCellValue(data.get("SALESORDERSUBCODE").toString());
					}
					break;
				case 5:
					if (data.get("BATCHCODE") != null) {
						cell.setCellValue(data.get("BATCHCODE").toString());
					}
					break;
				case 6:
					if (data.get("PRODUCTMODEL") != null) {
						cell.setCellValue(data.get("PRODUCTMODEL").toString());
					}
					break;
				case 7:
					if (data.get("FACTORYPRODUCTNAME") != null) {
						cell.setCellValue(data.get("FACTORYPRODUCTNAME").toString());
					}
					break;
				case 8:
					if (data.get("CONSUMERPRODUCTNAME") != null) {
						cell.setCellValue(data.get("CONSUMERPRODUCTNAME").toString());
					}
					break;

				case 9:
					if (data.get("PRODUCTWIDTH") != null) {
						cell.setCellValue(data.get("PRODUCTWIDTH").toString());
					}
					break;
				case 10:
					if (data.get("PRODUCTLENGTH") != null) {
						cell.setCellValue(data.get("PRODUCTLENGTH").toString());
					}
					break;
				case 11:
					if (data.get("RESERVELENGTH") != null) {
						cell.setCellValue(data.get("RESERVELENGTH").toString());
					}
					break;
				case 12:
					if (data.get("PRODUCTWEIGHT") != null) {
						cell.setCellValue(data.get("PRODUCTWEIGHT").toString());
					}
					break;

				case 13:
					if (data.get("PARTNAME") != null) {
						cell.setCellValue(data.get("PARTNAME").toString());
					}
					break;
				case 14:
					if (data.get("DRAWNO") != null) {
						cell.setCellValue(data.get("DRAWNO").toString());
					}
					break;
				case 15:
					if (data.get("ROLLNO") != null) {
						cell.setCellValue(data.get("ROLLNO").toString());
					}
					break;
				case 16:
					if (data.get("LEVELNO") != null) {
						cell.setCellValue(data.get("LEVELNO").toString());
					}
					break;
				case 17:
					if (data.get("REQUIREMENTCOUNT") != null) {
						String REQUIREMENTCOUNT=data.get("REQUIREMENTCOUNT").toString();
						cell.setCellValue(REQUIREMENTCOUNT.substring(0,REQUIREMENTCOUNT.indexOf(".")));
					}
					break;
				case 18:
					String rc=data.get("RC").toString();
					//String ts=data.get("TS").toString();
					String REQUIREMENTCOUNT=data.get("REQUIREMENTCOUNT").toString();
					/*if(Integer.parseInt(data.get("PRODUCTISTC").toString())==1){
						if("0.0".equals(data.get("TS").toString())||(data.get("TS").toString()==null)){
							
							cell.setCellValue("-"+"/"+REQUIREMENTCOUNT.substring(0,REQUIREMENTCOUNT.indexOf("."))+"套");
						}else{
							cell.setCellValue(data.get("TS").toString()+"/"+REQUIREMENTCOUNT.substring(0,REQUIREMENTCOUNT.indexOf("."))+"套");
							
						}
					}else{*/
						if("0".equals(rc)){
							
							cell.setCellValue("-"+"/"+REQUIREMENTCOUNT.substring(0,REQUIREMENTCOUNT.indexOf("."))+"卷");
						}else{
							cell.setCellValue(rc+"/"+REQUIREMENTCOUNT.substring(0,REQUIREMENTCOUNT.indexOf("."))+"卷");
							
						}
					/*}*/
					break;
				case 19:
					String tc=data.get("TC").toString();
					if(data.get("TOTALTRAYCOUNT")!=null){
						if("0".equals(tc) || tc==null){
							cell.setCellValue("-"+"/"+data.get("TOTALTRAYCOUNT").toString()+"托");
						}else{
							cell.setCellValue(tc+"/"+data.get("TOTALTRAYCOUNT").toString()+"托");
							break;
						}
					}else{
						cell.setCellValue("-"+"/"+"-"+"托");
					}
					break;
				case 20:
					if (data.get("CONSUMERSIMPLENAME") != null) {
						cell.setCellValue(data.get("CONSUMERSIMPLENAME").toString());
					}
					break;
				case 21:
					if (data.get("PRODUCTTYPE") != null) {
						String PRODUCTTYPE=data.get("PRODUCTTYPE").toString();
						if(PRODUCTTYPE.equals("1")){
							cell.setCellValue("大卷产品");
						}else if("2".equals(PRODUCTTYPE)){
							cell.setCellValue("中卷产品");
						}else if("3".equals(PRODUCTTYPE)){
							cell.setCellValue("小卷产品");
						}else{
							cell.setCellValue("其他产品");
						}
					}
					break;
				case 22:
					if (data.get("DELEVERYDATE") != null) {
						cell.setCellValue(data.get("DELEVERYDATE").toString());
					}
					break;
				case 23:
					if (data.get("PROCESSBOMCODE") != null) {
						cell.setCellValue(data.get("PROCESSBOMCODE").toString());
					}
					break;
				case 24:
					if (data.get("PROCESSBOMVERSION") != null) {
						cell.setCellValue(data.get("PROCESSBOMVERSION").toString());
					}
					break;
				case 25:
					if (data.get("BCBOMCODE") != null) {
						cell.setCellValue(data.get("BCBOMCODE").toString());
					}
					break;
				case 26:
					if (data.get("BCBOMVERSION") != null) {
						cell.setCellValue(data.get("BCBOMVERSION").toString());
					}
					break;
				case 27:
					if (data.get("PREQ") != null) {
						cell.setCellValue(data.get("PREQ").toString());
					}
					break;
				case 28:
					if (data.get("COM") != null) {
						cell.setCellValue(data.get("COM").toString());
					}
					break;
					
				}
			}
			r++;
		}
		HttpUtils.download(response, wb, templateName);
	}

	@ResponseBody
	@Journal(name="获取编织计划包装信息")
	@RequestMapping("getWeavePlanPackTask")
	public String getWeavePlanPackTask(Long wid){
		return GsonTools.toJson(weavePlanService.getWeavePlanPackTask(wid));
	}
	
	@Journal(name="选择计划页面")
	@RequestMapping(value="checkbjinfo",method=RequestMethod.GET)
	public ModelAndView checkbjinfo(String id,String yx, String partname){
		System.out.println(id);
		return new ModelAndView(viewBjInfo,model.addAttribute("id",id).addAttribute("yx",yx).addAttribute("partname",partname));
		
	}
	
	
	
	@Journal(name="选择部件汇总信息页面")
	@RequestMapping(value="checkbjhzinfo",method=RequestMethod.GET)
	public ModelAndView checkbjinfo(String id){
		System.out.println(id);
		return new ModelAndView(bjhzview,model.addAttribute("id",id));
		
	}
	
	
	
	
	
	
	
	
	@NoAuth
	@ResponseBody
	@Journal(name="查看部件信息")
	@RequestMapping("viewBjInfo")
	public String getYxInfo(String id, String yx, String partname) throws Exception{ 
		//String a = GsonTools.toJson(finishProductService.checkYxInfo(id));
		
		Map<String,Object> bjinfos = weavePlanService.getBjDetails(id, yx, partname);
		
		
		//return new ModelAndView(viewBjInfo, model.addAttribute("yxdates", GsonTools.toJson(bjinfos)));
		return GsonTools.toJson(bjinfos);
		//return GsonTools.toJson(finishProductService.checkYxInfo(id));
	}
	
	
	
	
	

	@NoAuth
	@ResponseBody
	@Journal(name="查看部件汇总信息")
	@RequestMapping("viewBjhzInfo")
	public String getBjHzInfo(String id) throws Exception{ 
		return GsonTools.toJson(weavePlanService.findDevicePlans(id));
	}
	
	
	
	
	
	
}