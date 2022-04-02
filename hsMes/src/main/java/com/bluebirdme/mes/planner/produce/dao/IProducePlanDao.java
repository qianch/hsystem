/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.planner.produce.dao;

import java.util.List;
import java.util.Map;

import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;

import com.bluebirdme.mes.planner.produce.entity.ProducePlan;
import com.bluebirdme.mes.planner.produce.entity.ProducePlanDetail;
import com.bluebirdme.mes.produce.entity.FinishedProduct;
import org.springframework.stereotype.Repository;
/**
 * 
 * @author 高飞
 * @Date 2016-11-28 21:25:48
 */

public interface IProducePlanDao extends IBaseDao {
	public <T> Map<String, Object> findOrderPageInfo(Filter filter, Page page) throws Exception;
	public <T> Map<String, Object> listOrders(Filter filter, Page page) throws Exception;
	public String getSerial(String workShopCode) throws Exception;
	public String getSdeviceCode(Long id);//得到机台实际机台编码
	
	public Double getFbslByDdh(String ddh) throws Exception;

	public Double getFbslByDdhs(Map<String, Object> ddhs) throws Exception;
	
	/**
	 * @param filter
	 * @param page
	 * @return
	 */
	public Map<String, Object> dataList(Filter filter, Page page) throws Exception;
	
	public void setIsSettled(Long weaveDailyId,Long cutDailyId);
	
	/**
	 * 检查批次号 是否存在
	 * @param batchCode
	 * @param producePlanId
	 * @return
	 */
	public List<Map<String,Object>> checkBatchCode(String batchCode,Long producePlanId);
	
	public List<Map<String,Object>> details(Long planId);
	public List<Map<String,Object>> detailsMirror(Long planId);
	public <T> Map<String, Object> findOrderPageInfo2(Filter filter, Page page) throws Exception;
	//未分配的订单
	public List<Map<String,Object>> searchbox(String searchbox);
	public Map<String, Object> findfinished(Filter filter,Page page);
	public <T> Map<String, Object> findSchedule(Filter filter, Page page) throws Exception;
	public <T> Map<String, Object> findScheduleWeight(Filter filter, Page page) throws Exception;
	public String hasPackTask(Long id);
	
	public <T> Map<String, Object> findTBPageInfo(Filter filter, Page page) throws Exception;
	
	public <T> Map<String, Object> findTBPageInfo2(Filter filter, Page page) throws Exception;

	List<Map<String,Object>> findProductListByMap(Long id);

    List<Map<String, Object>> selectByFormId(Long formId);

	List<ProducePlanDetail> findListGroupByMap(String batchcode, String productmodel);
}
