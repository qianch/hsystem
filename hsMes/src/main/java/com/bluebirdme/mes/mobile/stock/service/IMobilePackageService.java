package com.bluebirdme.mes.mobile.stock.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.PathVariable;

import com.bluebirdme.mes.core.base.service.IBaseService;
import com.bluebirdme.mes.store.entity.Box;
import com.bluebirdme.mes.store.entity.BoxRoll;
import com.bluebirdme.mes.store.entity.Tray;
import com.bluebirdme.mes.store.entity.TrayBoxRoll;

/**
 * @author Goofy
 * @Date 2016年11月18日 上午9:39:00
 */
public interface IMobilePackageService extends IBaseService {
	public void saveBoxRoll(Box box,BoxRoll[] boxRolls,Long planId,String partName);
	public void saveTrayBoxRoll(Tray tray,TrayBoxRoll[] trayBoxRolls,Long planId,String partName);
	public void updateProgress(Tray tray);
	/**
	 * 盒打包判断卷是否已被打包
	 */
	public List<Map<String,Object>> isPackedRoll(String rollCodes);
	/**
	 * 托打包判断盒或卷是否已被打包
	 * @param rollCodes
	 * @param BoxCodes
	 * @return
	 */
	public List<Map<String,Object>> isPackedBoxRoll(String rollCodes,String boxCodes);
	public String tray(String puname, Long packagingStaff, String trayCode,
			String boxCodes, String rollCodes, Long planId, String partName,
			String model,Long partId) throws Exception;
	public String box(String puname, String boxCode, Long packagingStaff,
			String rollCodes, Long planId, String partName,Long partId) throws Exception ;
	public void updateTrayInfo(String trayCode,String rollCode) throws Exception;
	public String endPack(String code) throws IOException;
	public void open(String code);
	/**
	 * 拆包
	 * @param code 条码
	 * @param operateUserId 拆包人
	 * @return
	 */
	public void openPackBarCode(String code,Long operateUserId);
	public void deleteInner(String type,Long id) throws Exception;
}
