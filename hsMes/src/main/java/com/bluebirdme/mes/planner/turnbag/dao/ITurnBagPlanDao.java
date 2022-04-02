/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.planner.turnbag.dao;

import java.util.List;
import java.util.Map;

import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;

import org.springframework.stereotype.Repository;
/**
 * 
 * @author 高飞
 * @Date 2017-2-9 11:28:32
 */

public interface ITurnBagPlanDao extends IBaseDao {
	public <T> Map<String, Object> findOrderPageInfo(Filter filter, Page page) throws Exception;
	public List<Map<String,Object>> getBatchCodeCountBySalesOrderCode(Long orderId,Long partId);
	public String getSerial();
	public List<Map<String,Object>> getDetails(Long turnbagPlanId);
	public List<Map<String,Object>> getGoodsPosition(List<Long> ids,List<String> batchCodes);
	public List<Map<String, Object>> getPackChildren(String code);
	public String trayDeviceCode(String trayCode);
	public Map<String,Object> getBatchInfo(Long targetProducePlanDetailId,Long fromProducePlanDetailId);
	/**
	 * 查询翻包领出记录
	 * @param filter
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public <T> Map<String, Object> findTurnBagOutPageInfo(Filter filter, Page page) throws Exception;
}
