/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.siemens.barcode.controller;

import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.annotation.NoAuth;
import com.bluebirdme.mes.core.base.controller.BaseController;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.planner.cut.entity.CutPlan;
import com.bluebirdme.mes.printer.entity.Printer;
import com.bluebirdme.mes.siemens.barcode.entity.FragmentBarcode;
import com.bluebirdme.mes.siemens.barcode.service.IFragmentBarcodeService;
import com.bluebirdme.mes.siemens.order.entity.CutTask;
import com.bluebirdme.mes.siemens.order.entity.PartSuit;
import com.bluebirdme.mes.store.entity.PartBarcode;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.xdemo.superutil.j2se.ListUtils;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 裁片条码Controller
 * @author 高飞
 * @Date 2017-8-3 20:38:40
 */
@Controller
@RequestMapping("/siemens/fragmentBarcode")
@Journal(name="裁片条码")
public class FragmentBarcodeController extends BaseController {

	// 裁片条码页面
	final String index = "siemens/barcode/fragmentBarcode";
	final String addOrEdit="siemens/barcode/fragmentBarcodeAddOrEdit";

	@Resource IFragmentBarcodeService fragmentBarcodeService;

	
	@Journal(name = "条码管理")
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView index() {
		List<Printer> printerList = fragmentBarcodeService.findAll(Printer.class);
		model.addAttribute("printers", GsonTools.toJson(printerList));
		return new ModelAndView(index, model);
	}
	
	@Journal(name="部件条码查询")
	@RequestMapping(value="view",method = RequestMethod.GET)
	public String query(){
		return "siemens/barcode/view";
	}
	
	@Journal(name="部件条码查询")
	@ResponseBody
	@RequestMapping(value="view",method = RequestMethod.POST)
	public String doQuery(String code){
		code=code.toUpperCase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("barcode", code);
		Object obj;
		if(code.startsWith("P")){
			obj=fragmentBarcodeService.findUniqueByMap(PartBarcode.class, map);
		}else{
			obj=fragmentBarcodeService.findUniqueByMap(FragmentBarcode.class, map);
		}
		
		return GsonTools.toJson(obj);
	}
	
	@Journal(name="部件条码查询")
	@ResponseBody
	@RequestMapping(value="view/tree",method = RequestMethod.GET)
	public String queryTree(String code){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("barcode", code);
		boolean exist=false;
		String root=code;
		if(code.startsWith("P")){
			exist=fragmentBarcodeService.isExist(PartBarcode.class, map);
		}else{
			FragmentBarcode fb=fragmentBarcodeService.findUniqueByMap(FragmentBarcode.class, map);
			if(fb==null)
				exist=false;
			else{
				exist=true;
				if(fb.getIsPacked()==1){
					
					map.clear();
					map.put("fragmentBarcode", code);
					
					List<PartSuit> list=fragmentBarcodeService.findListByMap(PartSuit.class, map);
					
					if(list.size()==0){
						return ajaxError("条码已被打包，但无法找到被打包至那个部件条码中");
					}
					root=list.get(0).getPartBarcode();
				}else{
					root=code;
				}
			}
		}
		
		if(!exist)return ajaxError("条码不存在");
		
		List<Map<String,Object>> children=fragmentBarcodeService.getSuitInfo(code);
		
		List<Map<String, Object>> ret=new ArrayList<Map<String, Object>>();
		
		Map<String, Object> rootNode = new HashMap<String, Object>();
		
		rootNode.put("id", root);
		rootNode.put("text", root);
		
		Map<String, Object> child;
		
		List<Map<String, Object>> children_=new ArrayList<Map<String, Object>>();
		
		for(Map<String, Object> unit:children){
			child = new HashMap<String, Object>();
			child.put("id", unit.get("ID"));
			child.put("text", unit.get("TEXT"));
			child.put("fragmentName",unit.get("FRAGMENTNAME"));
			children_.add(child);
		}
		
		rootNode.put("children", children_);
		ret.add(rootNode);
		return GsonTools.toJson(ret);
	}
	
	
	@NoAuth
	@ResponseBody
	@Journal(name="获取裁片条码列表信息")
	@RequestMapping("list")
	public String getFragmentBarcode(Filter filter, Page page) throws Exception{
		return GsonTools.toJson(fragmentBarcodeService.findPageInfo(filter, page));
	}
	
	@ResponseBody
	@RequestMapping("extraPrint")
	public void extraPrint(String barcodes,String printer,String user,String reason) throws Exception{
		fragmentBarcodeService.extraPrint(barcodes, printer, user, reason);
	}
	
	@RequestMapping(value="review",method=RequestMethod.GET)
	public String review(){
		return "siemens/barcode/review";
	}
	
	@ResponseBody
	@RequestMapping(value="review",method=RequestMethod.POST)
	public String review(String barcode) throws Exception{
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		FragmentBarcode fragment=fragmentBarcodeService.findOne(FragmentBarcode.class, "barcode", barcode);
		
		if(fragment==null)throw new Exception("无效条码");
		
		map.put("device", fragment.getDeviceCode()==null?"":fragment.getDeviceCode());
		
		List<PartSuit> suitList=fragmentBarcodeService.find(PartSuit.class,"fragmentBarcode", barcode);
		
		if(ListUtils.isEmpty(suitList)){
			map.put("partBarcode", "未组套");
		}else{
			map.put("partBarcode", suitList.get(0).getPartBarcode());
		}
		
		map.put("suitTime",fragment.getPackedTime()==null?"":fragment.getPackedTime());
		map.put("suitUser",fragment.getPackUserName()==null?"":fragment.getPackUserName());
		
		CutTask ct=fragmentBarcodeService.findById(CutTask.class, fragment.getCtId());
		CutPlan cp=fragmentBarcodeService.findById(CutPlan.class, ct.getCutPlanId());
		List<String> feedingFarbic=fragmentBarcodeService.getFeedingFarbic(cp.getId());
		
		map.put("order", fragment.getOrderCode());
		
		map.put("batch", fragment.getBatchCode());
		
		map.put("code", fragment.getBarcode());
		
		map.put("farbic", feedingFarbic);
		
		return GsonTools.toJson(map);
	}

}