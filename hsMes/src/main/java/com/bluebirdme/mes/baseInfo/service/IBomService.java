package com.bluebirdme.mes.baseInfo.service;

import java.util.List;
import java.util.Map;

import com.bluebirdme.mes.baseInfo.entity.BCBomVersion;
import com.bluebirdme.mes.baseInfo.entity.FtcBomDetail;
import com.bluebirdme.mes.baseInfo.entity.FtcBomVersion;
import com.bluebirdme.mes.baseInfo.entity.TcBomVersion;
import com.bluebirdme.mes.core.base.service.IBaseService;
import com.bluebirdme.mes.produce.entity.FinishedProduct;
import com.bluebirdme.mes.sales.entity.SalesOrderDetail;

/**
 * BOM Service
 * @author Goofy
 * @Date 2016年10月19日 下午2:35:37
 */
public interface IBomService extends IBaseService {
	/**
	 * 根据包材code获取包材BOM的版本信息
	 * @param bcBomCode
	 * @return
	 */
	public List<BCBomVersion> getBcVersions(String bcBomCode);
	/**
	 * 根据套材code获取套材BOM的版本信息
	 * @param tcBomCode
	 * @return
	 */
	public List<TcBomVersion> getTcVersions(String tcBomCode);
	/**
	 * 根据非套材code获取非套材BOM的版本信息
	 * @param ftcBomCode
	 * @return
	 */
	public List<FtcBomVersion> getFtcVersions(String ftcBomCode);
	
	public <T> List<T> getBomDetails(Class<T> clazz,String bomCode,String bomVersion);
	public List<FtcBomDetail> getBomDetails(FinishedProduct product);
	public void setDefult(String type,int defultType,Long id);

	List<Map<String,Object>> findSalesOrderDetail(Long id,String c) throws Exception;

	List<Map<String, Object>> findSalesOrderDetail1(Long id) throws Exception;

}
