/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.siemens.barcode.service.impl;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.xdemo.superutil.j2se.PathUtils;
import org.xdemo.superutil.j2se.StringUtils;

import com.bluebirdme.mes.core.annotation.AnyExceptionRollback;
import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.siemens.barcode.dao.IFragmentBarcodeDao;
import com.bluebirdme.mes.siemens.barcode.entity.FragmentBarcode;
import com.bluebirdme.mes.siemens.barcode.service.IFragmentBarcodeService;
import com.bluebirdme.mes.siemens.order.entity.CutTask;
import com.bluebirdme.mes.siemens.order.entity.CutTaskDrawings;
import com.bluebirdme.mes.siemens.order.entity.CutTaskOrder;
import com.bluebirdme.mes.siemens.order.entity.CutTaskOrderDrawings;
import com.bluebirdme.mes.siemens.order.entity.PartSuit;
import com.bluebirdme.mes.utils.PrintUtils;

/**
 * 
 * @author 高飞
 * @Date 2017-8-3 20:38:40
 */
@Service
@AnyExceptionRollback
public class FragmentBarcodeServiceImpl extends BaseServiceImpl implements IFragmentBarcodeService {
	
	@Resource IFragmentBarcodeDao fragmentBarcodeDao;
	
	@Override
	protected IBaseDao getBaseDao() {
		return fragmentBarcodeDao;
	}

	@Override
	public <T> Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
		return fragmentBarcodeDao.findPageInfo(filter,page);
	}
	
	public void extraPrint(String barcodes,String printer,String user,String reason) throws Exception{
		FragmentBarcode fb=null;
		CutTaskDrawings ctd=null;
		CutTaskOrderDrawings ctod=null;
		
		List<String> content=new ArrayList<String>();
		
		for(String barcode:barcodes.split(",")){
			fb=findOne(FragmentBarcode.class, "barcode", barcode);
			fb.setExtraPrintReason(reason);
			fb.setExtraPrintTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			fb.setExtraPrintUser(user);
			fb.setPrintType("补打");
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("ctId", fb.getCtId());
			map.put("dwId", fb.getDwId());
			
			ctd=findUniqueByMap(CutTaskDrawings.class, map);
			ctd.setExtraPrintCount(ctd.getExtraPrintCount()+1);
			
			map.put("ctoId", fb.getCtoId());
			map.remove("ctId");
			
			ctod=findUniqueByMap(CutTaskOrderDrawings.class, map);
			ctod.setExtraPrintCount(ctod.getExtraPrintCount()+1);
			
			update(ctd);
			update(ctod);
			content.add(StringUtils.trimAll(fb.getPartName())+" \t "+fb.getFarbicModel()+" \t"+fb.getFragmentName()+" \tL:"+fb.getFragmentLength()+"M \tW:"+fb.getFragmentWidth()+"MM \t"+fb.getFragmentMemo()+" \t"+fb.getGroupName()+" \t"+fb.getBarcode()+"\t"+fb.getFragmentDrawingNo());
		}
		
		//String file="D:\\muban\\恒石条码(裁片).txt";
		String btw=  new File(PathUtils.getClassPath()) + File.separator +"BtwFiles" + File.separator + "恒石条码(裁片).btw";
		PrintUtils.print(content,btw, printer);
	}
	
	/**
	 * 组套
	 * 1.保存部件条码和小部件条码的对应关系
	 * 2.更新小部件条码的组套时间，操作人，对应机台
	 * 3.更新派工单组套套数
	 * 4.更新任务单组套套数
	 */
	public void suit(String ctoCode,String part,String fragments,String user,String device) throws Exception{
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("partBarcode",part);
		if(has(PartSuit.class, map)){
			throw new Exception("部件已组套，无法再次操作");
		}
		
		String _codes=fragmentBarcodeDao.fragmentCheckPacked(fragments.split(","));
		System.out.println(_codes);
		if(!StringUtils.isBlank(_codes)){
			throw new Exception("["+_codes+"]\n已被组套，无法再次操作");
		}
		
		List<PartSuit> list=new ArrayList<PartSuit>();
		
		PartSuit suit=null;
		
		StringBuffer sb=new StringBuffer();
		
		for(String fg:fragments.split(",")){
			suit=new PartSuit();
			suit.setFragmentBarcode(fg);
			suit.setPartBarcode(part);
			list.add(suit);
			sb.append((sb.length()==0?"":",")+"'"+fg+"'");
		}
		
		list.add(suit);
		
		saveList(list);
		
		String date=new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		
		fragmentBarcodeDao.updateFragmentBarcodeInfo(sb.toString(), user, date, device);
		
		CutTaskOrder cto=findOne(CutTaskOrder.class, "ctoCode", ctoCode);
		
		cto.setPackedSuitCount(cto.getPackedSuitCount()+1);
		if(cto.getPackedSuitCount()>cto.getAssignSuitCount()){
			throw new Exception("打包数量超过派工数量");
		}
		
		if(cto.getAssignSuitCount()==cto.getPackedSuitCount()){
			cto.setIsComplete(1);
		}
		
		CutTask ct=findById(CutTask.class, cto.getCtId());
		ct.setPackedSuitCount(ct.getPackedSuitCount()+1);
		
		if(ct.getPackedSuitCount()>ct.getSuitCount()){
			throw new Exception("打包数量超过任务单数量");
		}
		
		if(ct.getSuitCount()==ct.getPackedSuitCount()){
			ct.setIsComplete(1);
		}
		
		update(cto);
		update(ct);
				
	}
	
	public List<String> getFeedingFarbic(Long cutId){
		return fragmentBarcodeDao.getFeedingFarbic(cutId);
	}
	
	public List<Map<String,Object>> getSuitInfo(String code){
		return fragmentBarcodeDao.getSuitInfo(code);
	}

}
