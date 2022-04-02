/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.sales.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.sql.SQLTemplateException;
import com.bluebirdme.mes.produce.entity.FinishedProductMirror;
import com.bluebirdme.mes.sales.entity.SalesOrder;
import com.bluebirdme.mes.sales.entity.SalesOrderDetail;
/**
 * 
 * @author 高飞
 * @Date 2016-10-13 11:06:42
 */

public interface ISalesOrderDao extends IBaseDao {
	public void deleteDetails(Long salesId);
	public List<SalesOrderDetail> getDetails(Long salesOrderId);
	public List<SalesOrderDetail> findDetailByCode(String salesOrderCode)throws SQLTemplateException;
	public List<Map<String,Object>> findDetailByCode2(String orderCode) throws SQLTemplateException;
	public List<Map<String,Object>> getOrderPartCount(Long id);
	public List<SalesOrder> findUncreatePlans();
	public String getSerial(Integer export);
	public <T> Map<String, Object> findPageInfo2(Filter filter, Page page) throws Exception;
	public void update(String id);
	public String getCode(Integer salesOrderIsExport,Date salesOrderDate);
	public <T> Map<String, Object> findPageInfo1(Filter filter, Page page) throws Exception;
	public Map<String,Object> countRollsAndTrays(Long salesOrderDetailId);
	public <T> Map<String, Object> findPageOut(Filter filter, Page page) throws Exception;
	public <T> Map<String, Object> findPageQuantity(Filter filter, Page page) throws Exception;
	//月度订单产品汇总
	public <T> Map<String, Object> findPageSummaryMonthly(Filter filter, Page page) throws Exception;
	public String hasPackTask(Long salesOrderId);
	public void setAllocated(String ids);

	List<Map<String, Object>> findOrder();

	List<Map<String, Object>> findDetailByCodes(String salesOrderCode) throws Exception;

 List<Map<String, Object>> findSalesOrder(String data) throws Exception;

 List<Map<String, Object>> findD(String id);

 List<Map<String, Object>> findFV(String id);

	List<Map<String, Object>> findTV(String id);

	List<Map<String, Object>> findFBom(String id);

	List<Map<String, Object>> findTBom(String id);

	List<Map<String, Object>> findP(String id);

	List<Map<String,Object>> findListByMap1(Long id);

	List<Map<String, Object>> selectSalesOrder(String salesOrderSubCode, String factoryProductName);
}
