/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.produce.service;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.IBaseService;
import com.bluebirdme.mes.core.excel.ExcelContent;
import com.bluebirdme.mes.core.excel.ExcelImportMessage;
import com.bluebirdme.mes.core.sql.SQLTemplateException;

/**
 *
 * @author 宋黎明
 * @Date 2016-9-30 10:49:34
 */
public interface IFinishedProductService extends IBaseService {
	public abstract <T> Map<String, Object> findPageInfo2(Filter filter, Page page) throws Exception;
	public void delete(String ids);
	//查找tcBom的信息
	public List<Map<String, Object>> tcTree(String data)  throws SQLTemplateException;
	//查找ftcBom的信息
	public List<Map<String, Object>> ftcTree(String data) throws SQLTemplateException;
	//查找bcBom的信息
	public List<Map<String, Object>> bcTree(String data) throws SQLTemplateException;

	public int querySlBycode(String wlbh) throws SQLTemplateException;

	public int queryCpggInfo(String wlbh) throws SQLTemplateException;


	public int queryMfnewcode(String wlbh) throws SQLTemplateException;

	public int queryJznewcode(String wlbh) throws SQLTemplateException;

	public int queryJcnewcode(String wlbh) throws SQLTemplateException;


	public int queryJzcnewcode(String wlbh) throws SQLTemplateException;

	public int querySlinfo(String wlbh) throws SQLTemplateException;

	public String getzgmcbycode(String code) throws SQLTemplateException;



	public void completeBomId() throws SQLTemplateException, IOException;
	//废弃状态
	public void updates(Long id);
	public <T> Map<String, Object> findPageInfoDelivery(Filter filter, Page page) throws Exception;

	/**
	 * 产成品汇总（按成品类别统计）
	 * @param filter
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> productsSummary(Filter filter, Page page)throws Exception;

	/**
	 * 产成品汇总（按厂内名称）
	 * @param filter
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> productsFactorySummary(Filter filter, Page page)throws Exception;

	/**
	 * 产成品汇总（订单号、批次号、厂内名称）
	 * @param filter
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> productsSundrySummary(Filter filter, Page page)throws Exception;

	/**
	 * 产成品汇总(按客户统计)
	 * @param filter
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> productsCustomerStockSummary(Filter filter, Page page)throws Exception;

	/**
	 * Excel导入保存
	 * @param content
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public ExcelImportMessage saveFinishedProductFromExcel(ExcelContent content) throws NoSuchAlgorithmException;
	/**
	 * 查询所有未作废的成品
	 */

	public List<Map<String, Object>> findAllFinishProduct()  throws Exception;


	public List<Map<String,Object>> checkYxInfo(String planCode)throws SQLTemplateException;

	public List<Map<String,Object>> queryMfinfo(String wlbh, String mf)throws SQLTemplateException;

	public List<Map<String,Object>> queryJZinfo(String wlbh, String jz)throws SQLTemplateException;

	public List<Map<String,Object>> queryJCinfo(String wlbh, String jc)throws SQLTemplateException;

	public List<Map<String,Object>> queryJZCinfo(String wlbh, String jz, String jc)throws SQLTemplateException;


	public List<Map<String,Object>> queryGGinfo(String wlbh, String gg)throws SQLTemplateException;

	public List<Map<String,Object>> queryBcinfoByBcbm(String bcmb)throws SQLTemplateException;





	public void resumeFinishProduct(Long id);


    Map<String, Object> findPageInfo1(Filter filter, Page page);

	public List<Map<String, Object>> queryYxInfo(long finishedProductId)throws SQLTemplateException;
	
	
	  //产品的总克重
	public int queryProcBomDetail(String ftcBomVersionId) throws SQLTemplateException;
}
