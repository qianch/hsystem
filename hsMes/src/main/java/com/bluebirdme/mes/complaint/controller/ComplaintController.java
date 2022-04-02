/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.complaint.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import com.bluebirdme.mes.complaint.entity.Complaint;
import com.bluebirdme.mes.complaint.service.IComplaintService;
import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.annotation.NoAuth;
import com.bluebirdme.mes.core.annotation.support.LogType;
import com.bluebirdme.mes.core.base.controller.BaseController;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.constant.Constant;
import com.bluebirdme.mes.core.valid.annotations.Valid;

/**
 * 投诉Controller
 * @author 高飞
 * @Date 2016-11-25 15:40:05
 */
@Controller
@RequestMapping("/complaint")
@Journal(name="投诉")
public class ComplaintController extends BaseController {

	// 投诉页面
	final String index = "complaint/complaint";
	final String addOrEdit="complaint/complaintAddOrEdit";

	@Resource IComplaintService complaintService;

	@Journal(name = "首页")
	@RequestMapping(method = RequestMethod.GET)
	public String index() {
		return index;
	}
	
	@NoAuth
	@ResponseBody
	@Journal(name="获取投诉列表信息")
	@RequestMapping("list")
	public String getComplaint(Filter filter, Page page) throws Exception{
		return GsonTools.toJson(complaintService.findPageInfo(filter, page));
	}
	

	@Journal(name="添加投诉页面")
	@RequestMapping(value="add",method=RequestMethod.GET)
	public ModelAndView _add(Complaint complaint){
		complaint.setFilePath(UUID.randomUUID().toString());
		return new ModelAndView(addOrEdit,model.addAttribute("complaint", complaint));
	}
	
	@ResponseBody
	@Journal(name="保存投诉",logType=LogType.DB)
	@RequestMapping(value="add",method=RequestMethod.POST)
	@Valid
	public String add(Complaint complaint,String code) throws Exception{
		String serial=getSerial("",code);
		complaint.setComplaintCode(code+serial);
		complaint.setLastUpdateDate(new Date());
		complaint.setLastUpdateUser((String)session.getAttribute(Constant.CURRENT_USER_NAME));
		complaintService.save(complaint);
		return GsonTools.toJson(complaint);
	}
	
	@Journal(name="编辑投诉页面")
	@RequestMapping(value="edit",method=RequestMethod.GET)
	public ModelAndView _edit(Complaint complaint){
		complaint=complaintService.findById(Complaint.class, complaint.getId());
		System.out.println(complaint.getLastUpdateDate());
		return new ModelAndView(addOrEdit, model.addAttribute("complaint", complaint));
	}
	
	@ResponseBody
	@Journal(name="编辑投诉",logType=LogType.DB)
	@RequestMapping(value="edit",method=RequestMethod.POST)
	@Valid
	public String edit(Complaint complaint) throws Exception{
		complaint.setLastUpdateDate(new Date());
		complaint.setLastUpdateUser((String)session.getAttribute(Constant.CURRENT_USER_NAME));
		complaintService.update(complaint);
		return GsonTools.toJson(complaint);
	}

	@ResponseBody
	@Journal(name="删除投诉",logType=LogType.DB)
	@RequestMapping(value="delete",method=RequestMethod.POST)
	public String edit(String ids) throws Exception{
		complaintService.delete(Complaint.class,ids);
		return Constant.AJAX_SUCCESS;
	}
	
	@ResponseBody
	@Journal(name="获取序列号")
	@RequestMapping(value="serial",method=RequestMethod.GET)
	public String getSerial(String code,String flag){
		
		Date now=new Date();
		
		Map<String,String> map=new HashMap<String,String>();
		int serial=complaintService.getSerial(code,new SimpleDateFormat("yyyy").format(now))+1;

		String ret="";
		if((serial+"").length()==3)
			ret=serial+"";
		if((serial+"").length()==2)
			ret="0"+serial;
		if((serial+"").length()==1)
			ret="00"+serial;
		
		String _serial=new SimpleDateFormat("yyyyMMdd").format(now)+"-"+ret;
		
		map.put("serial", _serial);
		if(flag.equals("1")){
			return GsonTools.toJson(map);
		}else{
			return _serial;
		}
	}

}