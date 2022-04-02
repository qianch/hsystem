/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.produce.dao;

import java.util.List;
import java.util.Map;

import com.bluebirdme.mes.baseInfo.entity.BCBomVersion;
import com.bluebirdme.mes.baseInfo.entity.FtcBomVersion;
import com.bluebirdme.mes.baseInfo.entity.TcBomVersion;
import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.sql.SQLTemplateException;
/**
 *
 * @author 宋黎明
 * @Date 2016-9-30 10:49:34
 */

public interface IFinishedProductDao extends IBaseDao {
	public <T> Map<String, Object> findPageInfo2(Filter filter, Page page) throws Exception;
	public void delete(String ids);
	//查询tcBom的信息
	public List<Map<String,Object>> findTcBom(String data) throws SQLTemplateException;
	//查询ftcBom的信息
	public List<Map<String,Object>> findFtcBom(String data) throws SQLTemplateException;
	//查询bcBom的信息
	public List<Map<String, Object>> findBcBom(String data) throws SQLTemplateException;


	public int querySlBycode(String wlbh) throws SQLTemplateException;
	public int querySlinfo(String wlbh) throws SQLTemplateException;

	public int queryMfnewcode(String wlbh) throws SQLTemplateException;

	public int queryJznewcode(String wlbh) throws SQLTemplateException;

	public int queryJcnewcode(String wlbh) throws SQLTemplateException;

	public int queryJzcnewcode(String wlbh) throws SQLTemplateException;

	public String getzgmcbycode(String code) throws SQLTemplateException;




	//根据ftcbom查询下面的版本
	public List<FtcBomVersion> findFtcV(Long id);
	//根据tcbom查询下面的版本
	public List<TcBomVersion> findTcV(Long id);
	//根据bcbom查询下面的版本
	public List<BCBomVersion> findBcV(Long id);
	//更新成品废弃状态
	public void updates(Long id);
	public <T> Map<String, Object> findPageInfoDelivery(Filter filter, Page page) throws Exception;

	/**
	 * 产成品汇总（按成品类别）
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
	 * 查询所有的成品
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> findAllFinishProduct()  throws Exception;


	  public List<Map<String,Object>> checkYxInfo(String planCode)throws SQLTemplateException;

	  public List<Map<String,Object>> queryMfinfo(String wlbh, String mf)throws SQLTemplateException;


	  public List<Map<String, Object>> queryJZinfo(String wlbh, String jz) throws SQLTemplateException;

	  public List<Map<String, Object>> queryJCinfo(String wlbh, String jc) throws SQLTemplateException;

	  public List<Map<String, Object>> queryJZCinfo(String wlbh, String jz, String jc) throws SQLTemplateException;

	  public List<Map<String, Object>> queryGGinfo(String wlbh, String gg) throws SQLTemplateException;


	  public int queryCpggInfo(String wlbh) throws SQLTemplateException;


	  public List<Map<String, Object>> queryBcinfoByBcbm(String bcmb) throws SQLTemplateException;


      Map<String, Object> findPageInfo1(Filter filter, Page page);

	  public List<Map<String, Object>> queryYxInfo(long finishedProductId)throws SQLTemplateException;
	  //产品的总克重
	  public int queryProcBomDetail(String ftcBomVersionId) throws SQLTemplateException;
}
