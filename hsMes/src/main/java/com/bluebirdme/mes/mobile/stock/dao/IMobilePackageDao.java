package com.bluebirdme.mes.mobile.stock.dao;

import java.util.List;
import java.util.Map;

import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.service.IBaseService;
import com.bluebirdme.mes.store.entity.Box;
import com.bluebirdme.mes.store.entity.BoxRoll;
import com.bluebirdme.mes.store.entity.Tray;
import com.bluebirdme.mes.store.entity.TrayBoxRoll;

/**
 * 
 * @author Goofy
 * @Date 2016年11月18日 上午9:34:47
 */
public interface IMobilePackageDao extends IBaseDao {
	public void saveBoxRoll(Box box,BoxRoll[] boxRolls,Long planId,String partName);
	public void saveTrayBoxRoll(Tray tray,TrayBoxRoll[] trayBoxRolls,Long planId,String partName);
	/**
	 * 盒打包判断卷是否已被打包
	 */
	public List<Map<String,Object>> isPackedRoll(String[] rolls);
	/**
	 * 托打包判断盒或卷是否已被打包
	 * @param rolls
	 * @param boxs
	 * @return
	 */
	public List<Map<String,Object>> isPackedBoxRoll(String[] rolls,String[] boxs);
}
