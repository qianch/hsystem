/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.baseInfo.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import com.bluebirdme.mes.baseInfo.entity.*;
import com.bluebirdme.mes.siemens.bom.entity.Drawings;
import com.bluebirdme.mes.siemens.bom.entity.Fragment;
import com.bluebirdme.mes.siemens.bom.entity.Suit;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import com.bluebirdme.mes.audit.entity.AuditConstant;
import com.bluebirdme.mes.audit.service.IAuditInstanceService;
import com.bluebirdme.mes.baseInfo.service.ITcBomService;
import com.bluebirdme.mes.baseInfo.service.ITcBomVersionService;
import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.annotation.NoAuth;
import com.bluebirdme.mes.core.annotation.support.LogType;
import com.bluebirdme.mes.core.base.controller.BaseController;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.constant.Constant;
import com.bluebirdme.mes.core.excel.ExcelImportMessage;
import com.bluebirdme.mes.core.valid.annotations.Valid;

/**
 * 套材Bom版本Controller
 * @author 肖文彬
 * @Date 2016-10-9 16:10:05
 */
@Controller
@RequestMapping("/bom/tcBomVersion")
@Journal(name="套材Bom版本")
public class TcBomVersionController extends BaseController {

	// 套材Bom版本页面
	final String index = "baseInfo/tcBomVersion";
	final String addOrEdit="baseInfo/tcBom/tcBomVersionAddOrEdit";
	final String commitAudit="selector/auditEdit";
	
	@Resource
	ITcBomService tcBomService;
	@Resource ITcBomVersionService tcBomVersionService;
	@Resource IAuditInstanceService auditInstanceService;
	
	@Journal(name = "首页")
	@RequestMapping(method = RequestMethod.GET)
	public String index() {
		return index;
	}
	
	@NoAuth
	@ResponseBody
	@Journal(name="获取套材Bom版本列表信息")
	@RequestMapping("list")
	public String getTcBomVersion(Filter filter, Page page) throws Exception{
		return GsonTools.toJson(tcBomVersionService.findPageInfo(filter, page));
	}
	

	@Journal(name="添加套材Bom版本页面")
	@RequestMapping(value="add",method=RequestMethod.GET)
	public ModelAndView _add(Long id){
		TcBom tcBom = tcBomVersionService.findById(TcBom.class, id);
		return new ModelAndView(addOrEdit,model.addAttribute("tcBom", tcBom));
	}
	
	@ResponseBody
	@Journal(name="保存套材Bom版本",logType=LogType.DB)
	@RequestMapping(value="add",method=RequestMethod.POST)
	@Valid
	public String add(TcBomVersion tcBomVersion) throws Exception{
		HashMap<String, Object> map=new HashMap<String, Object>();
		List<TcBomVersion> saveList = new ArrayList<TcBomVersion>();
		map.put("tcProcBomId", tcBomVersion.getTcProcBomId());
		if(tcBomVersion.getTcProcBomVersionDefault()==null){
			tcBomVersion.setTcProcBomVersionDefault(1);
		}
		if(tcBomVersion.getTcProcBomVersionEnabled()==null){
			tcBomVersion.setTcProcBomVersionEnabled(1);
		}
		if(tcBomVersion.getAuditState()==null){
			tcBomVersion.setAuditState(0);
		}
		List<TcBomVersion> li=tcBomVersionService.findListByMap(TcBomVersion.class, map);
		for(TcBomVersion bv:li){
			if(bv.getTcProcBomVersionCode().equals(tcBomVersion.getTcProcBomVersionCode())){
				return ajaxError("已经有相同名字的版本");
			}
			HashMap<String, Object> map1 = new HashMap<String, Object>();
			map1.put("tcProcBomId", bv.getTcProcBomId());

			if (bv.getTcProcBomVersionDefault() == 1) {
				List<TcBomVersion> versionList = tcBomVersionService.findListByMap(
						TcBomVersion.class, map1);
				for (TcBomVersion v : versionList) {
					if (v.getTcProcBomVersionDefault() == 1) {
						v.setTcProcBomVersionDefault(-1);
						saveList.add(v);
					}
				}
			}
		}
		tcBomVersionService.save(tcBomVersion);
		return GsonTools.toJson(tcBomVersion);
	}
	
	@Journal(name="编辑套材Bom版本页面")
	@RequestMapping(value="edit",method=RequestMethod.GET)
	public ModelAndView _edit(Long id){
		TcBomVersion tcBomVersion=tcBomVersionService.findById(TcBomVersion.class, id);
		TcBom tcBom = tcBomVersionService.findById(TcBom.class,Long.valueOf(tcBomVersion.getTcProcBomId()));
		String filePath=UUID.randomUUID().toString();
		return new ModelAndView(addOrEdit, model.addAttribute("tcBomVersion", tcBomVersion).addAttribute("tcBom", tcBom).addAttribute("filePath", filePath));
	}
	
	@ResponseBody
	@Journal(name="编辑套材Bom版本",logType=LogType.DB)
	@RequestMapping(value="edit",method=RequestMethod.POST)
	@Valid
	public String edit(TcBomVersion tcBomVersion,Long fileId) throws Exception{
		HashMap<String, Object> map= new HashMap();
		map.put("tcProcBomId", tcBomVersion.getTcProcBomId());
		
		List<TcBomVersion> li=tcBomVersionService.findListByMap(TcBomVersion.class, map);
		for(TcBomVersion bv:li){
			if(bv.getTcProcBomVersionCode().equals(tcBomVersion.getTcProcBomVersionCode())&&!bv.getId().equals(tcBomVersion.getId())){
				return ajaxError("已经有相同名字的版本");
			}
		}
		ExcelImportMessage eim=tcBomService.doUpdateTcBomVersion(tcBomVersion, fileId);
		TcBom tcBom=tcBomService.findById(TcBom.class,tcBomVersion.getTcProcBomId());
		tcBomService.savePdfFile(tcBom,tcBomVersion,fileId,eim);
		if(eim != null && eim.hasError()){
			Map<String,String> excelErrorMsg=new HashMap<>();
			excelErrorMsg.put("excelErrorMsg",eim.getMessage());
			return GsonTools.toJson(excelErrorMsg);
		}
		return GsonTools.toJson(tcBomVersion);
	}
	
	@Journal(name="提交审核套材BOM版本页面")
	@RequestMapping(value="commitAudit",method=RequestMethod.GET)
	public ModelAndView commitAudit(String id){
		return new ModelAndView(commitAudit, model.addAttribute("id", id));
	}
	
	@ResponseBody
	@Journal(name="提交审核套材BOM版本",logType=LogType.DB)
	@RequestMapping(value="commitAudit",method=RequestMethod.POST)
	public String _commitAudit(Long id,String name) throws Exception{
		TcBomVersion tcBomVersion = tcBomVersionService.findById(TcBomVersion.class, id);
		TcBom tcBom = tcBomVersionService.findById(TcBom.class, tcBomVersion.getTcProcBomId());
		/*Map<String,Object> map = new HashMap<>();
		map.put("tcBomId",tcBom.getId());
		List<Fragment> fragments = tcBomVersionService.findListByMap(Fragment.class, map);
		if (fragments.size()==0){
			return ajaxError("裁片信息尚未建立，请检查");
		}
		Map<String,Object> map2 = new HashMap<>();
		map2.put("tcProcBomVersoinId",tcBomVersion.getId());
		List<TcBomVersionParts> tcBomVersionPartsList = tcBomVersionService.findListByMap(TcBomVersionParts.class, map2);
		String msg="";
		String msg1="";
		if (tcBomVersionPartsList.size()==0){
			return ajaxError("未新建部件信息");
		}
		for (TcBomVersionParts parts :tcBomVersionPartsList) {
			if (parts.getTcProcBomVersionPartsType().equals("成品胚布")){
				continue;
			}
			Map<String,Object> map3 = new HashMap<>();
			map3.put("partId",parts.getId());
			List<Drawings> baseDrawingsLIst = tcBomVersionService.findListByMap(Drawings.class, map3);
			if (baseDrawingsLIst.size()==0){
				msg +="部件名称："+parts.getTcProcBomVersionPartsName()+"的图纸bom信息未建立；";
			}
			if (parts.getDrawingsComplete()== null || parts.getDrawingsComplete()==0){
				msg +="部件名称："+parts.getTcProcBomVersionPartsName()+"的图纸bom信息尚未启用；";
			}
			List<Suit> suits = tcBomVersionService.findListByMap(Suit.class, map3);
			if (suits.size()==0){
				msg1 +="部件名称："+parts.getTcProcBomVersionPartsName()+"的组套bom信息未建立；";
			}
			if(parts.getSuitComplete() == null || parts.getSuitComplete()==0){
				msg1 +="部件名称："+parts.getTcProcBomVersionPartsName()+"的组套bom信息尚未启用；";
			}
		}
		if(!msg.equals("")){
			return ajaxError(msg);
		}
		if(!msg1.equals("")){
			return ajaxError(msg1);
		}*/
		if(tcBom.getIsTestPro()==0){
			auditInstanceService.submitAudit(name, AuditConstant.CODE.TC, (Long)session.getAttribute(Constant.CURRENT_USER_ID), "bom/tc/find/?id="+id, id, TcBomVersion.class);
		}else if(tcBom.getIsTestPro()==1){
			auditInstanceService.submitAudit(name, AuditConstant.CODE.TC1, (Long)session.getAttribute(Constant.CURRENT_USER_ID), "bom/tc/find/?id="+id, id, TcBomVersion.class);
		}else{
			auditInstanceService.submitAudit(name, AuditConstant.CODE.TC2, (Long)session.getAttribute(Constant.CURRENT_USER_ID), "bom/tc/find/?id="+id, id, TcBomVersion.class);
		}
		return Constant.AJAX_SUCCESS;
	}
	
	@RequestMapping(value = "rebuildBcAudit", method = RequestMethod.GET)
	public void rebuildBcAudit() throws Exception {
		List<TcBomVersion> versionList=tcBomVersionService.findAll(TcBomVersion.class);
		for(TcBomVersion tv:versionList){
			if (tv.getAuditState() == null) {
				continue;
			}
			if (tv.getAuditState() < 2 && tv.getAuditState() > 0) {
				TcBom bom = tcBomVersionService.findById(TcBom.class, tv.getTcProcBomId());
				_commitAudit(tv.getId(),"套材工艺审核：工艺名称："+bom.getTcProcBomName()+"/"+bom.getTcProcBomCode()+"版本号："+tv.getTcProcBomVersionCode());
			}
			
		}
	}
	

}