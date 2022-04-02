package com.bluebirdme.mes.planner.deviceScheduling;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import com.bluebirdme.mes.baseInfo.entity.FtcBcBom;
import com.bluebirdme.mes.baseInfo.entity.FtcBcBomVersion;
import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.annotation.NoAuth;
import com.bluebirdme.mes.core.annotation.support.LogType;
import com.bluebirdme.mes.core.base.controller.BaseController;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.sql.SQL;
import com.bluebirdme.mes.core.sql.SQLTemplateException;
import com.bluebirdme.mes.device.service.IDeviceService;
import com.bluebirdme.mes.planner.pack.entity.PackTask;
import com.bluebirdme.mes.planner.pack.service.IPackTaskService;
import com.bluebirdme.mes.sales.entity.Consumer;

/**
 * 机台排产
 * @author Goofy
 * @Date 2017年3月9日 上午10:57:59
 */
@Controller
@RequestMapping("/device/scheduling")
public class DeviceSchedulingController extends BaseController {
	private final String SCHEDULING="planner/scheduling/deviceScheduling";
	private final String SCHEDULING_VIEW="planner/scheduling/view";
	private final String SCHEDULING_VIEWC="planner/scheduling/viewC";
	private final String FTCBCBOM="planner/scheduling/checkFtcBcBomVersionDetail";
	private final String CHECKFTCPACKTASK="planner/scheduling/checkPackTask_produce";
	
	
	@Resource IDeviceService deviceService;
	@Resource IPackTaskService packTaskService;
	
	@RequestMapping(method=RequestMethod.GET)
	public String index(){
		return SCHEDULING;
	}
	
	@RequestMapping(value="view",method=RequestMethod.GET)
	public String view(){
		return SCHEDULING_VIEW;
	}
	@RequestMapping(value="viewC",method=RequestMethod.GET)
	public String viewC(){
		return SCHEDULING_VIEWC;
	}
	
	@ResponseBody
	@RequestMapping(value="view/list",method=RequestMethod.POST)
	public String viewList(Filter filter,Page page) throws Exception{
		Map<String, Object> map=deviceService.findAllDevicePlans(filter, page);
		return GsonTools.toJson(map);
	}

	
	@ResponseBody
	@Journal(name="获取设备列表以及订单的最早交货日期")
	@RequestMapping("list")
	public String getDevice(Filter filter, Page page) throws Exception{
		return GsonTools.toJson(deviceService.getDeliveryDate(filter, page));
	}
	
	@ResponseBody
	@Journal(name="查询设备的所有部门")
	@RequestMapping("department")
	public String getDeviceDepartment() throws SQLTemplateException{
		return GsonTools.toJson(deviceService.getDeviceDepartment());
	}
	@ResponseBody
	@Journal(name="查询裁剪车间")
	@RequestMapping("cutworkshop")
	public String getCutWorkShop() throws SQLTemplateException{
		return GsonTools.toJson(deviceService.getCutWorkShop());
	}
	
	@ResponseBody
	@Journal(name="保存设备和计划",logType=LogType.DB)
	@RequestMapping("save")
	public String saveDeviceAndOrder(String dids,String wids){
		deviceService.saveDeviceAndOrder(dids, wids);
		return ajaxSuccess();
	}
	
	
	
	@ResponseBody
	@Journal(name="保存设备和计划",logType=LogType.DB)
	@RequestMapping("savetwo")
	public String saveDeviceAndOrder2(String dids,String wids){
		deviceService.saveDeviceAndOrder2(dids, wids);
		return ajaxSuccess();
	}
	
	
	@ResponseBody
	@Journal(name="获取机台分配的计划")
	@RequestMapping("plans")
	public String getDevicePlans(String dids){
		return GsonTools.toJson(deviceService.findDevicePlans(dids));
	}
	
	
	
	@ResponseBody
	@Journal(name="获取机台分配的计划")
	@RequestMapping("bjdetails")
	public String getBjDetails(String devcode, String yx, String partname){
		return GsonTools.toJson(deviceService.getBjDetails(devcode, yx, partname));
	}

	@ResponseBody
	@Journal(name="删除机台分配的计划",logType=LogType.DB)
	@RequestMapping("delete")
	public String deleteDevicePlans(String dids,String wids){
		deviceService.deleteDevicePlans(dids,wids);
		return ajaxSuccess();
	}
	/**
	 * 查看非套材包材BOM页面
	 * @return
	 */
	@RequestMapping(value="checkFtcBcBomVersionDetail",method=RequestMethod.GET)
	public ModelAndView checkFtcBcBomVersionDetail(Integer packVersionId){
		FtcBcBomVersion ftcBcBomVersion=deviceService.findById(FtcBcBomVersion.class, Long.valueOf(packVersionId.toString()));
		FtcBcBom ftcBcBom=null;
		Consumer consumer=null;
		if(ftcBcBomVersion!=null){
		     ftcBcBom=deviceService.findById(FtcBcBom.class, Long.valueOf(ftcBcBomVersion.getBid()));
			 consumer=deviceService.findById(Consumer.class, Long.valueOf(ftcBcBomVersion.getConsumerId()));
		}
		
		return  new ModelAndView(FTCBCBOM, model.addAttribute("packVersionId",packVersionId).addAttribute("ftcBcBomVersion",ftcBcBomVersion).addAttribute("ftcBcBom",ftcBcBom).addAttribute("consumer",consumer));
	}
	
	@ResponseBody
	@Journal(name="根据packBomId得到非套材包装BOM列表")
	@RequestMapping("packBomList")
	public String getPackBomList(Integer packVersionId){
		List<Map<String,Object>> ftcBcBomVersionDetail=deviceService.getFtcBcBomVersionDetail(packVersionId);
		return GsonTools.toJson(ftcBcBomVersionDetail);
	}
	
	/**
	 * 查看非套材包装任务页面
	 * @return
	 */
	@RequestMapping(value="checkFtcPackTask",method=RequestMethod.GET)
	public ModelAndView  checkFtcPackTask(Long ppdId){
		return new ModelAndView(CHECKFTCPACKTASK, model.addAttribute("ppdId",ppdId));
	}
	@ResponseBody
	@Journal(name="根据生产计划明细ID获取包装任务列表")
	@RequestMapping("packTaskList")
	public String getPackTaskList(Long ppdId){
		List<PackTask> packTask=packTaskService.findProduceTask(ppdId);
		return GsonTools.toJson(packTask);
	}
	
	


	
}
