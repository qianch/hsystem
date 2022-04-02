/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.sales.service;

import java.util.List;
import java.util.Map;

import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.IBaseService;
import com.bluebirdme.mes.core.sql.SQLTemplateException;
import com.bluebirdme.mes.produce.entity.FinishedProductMirror;
import com.bluebirdme.mes.sales.entity.SalesOrder;
import com.bluebirdme.mes.sales.entity.SalesOrderDetail;
import com.bluebirdme.mes.store.entity.TrayBarCode;

/**
 * 订单Service
 * @author 高飞
 * @Date 2016-10-13 11:06:42
 */
public interface ISalesOrderService extends IBaseService {
	public void save(SalesOrder order) throws Exception;
	public void edit(SalesOrder order) throws Exception;
	public void forceEdit(List<SalesOrderDetail> details,Long userId) throws Exception;
	public List<SalesOrderDetail> getDetails(Long salesOrderId);
	public List<SalesOrderDetail> getDetails(String salesOrderCode)throws SQLTemplateException;
	public List<SalesOrder> findUncreatePlans();
	public String getSerial(Integer export);
	public void deleteSalesOrder(String ids);
	public <T> Map<String, Object> findPageInfo2(Filter filter, Page page) throws Exception;
	public List<Map<String,Object>> getDetails2(String salesOrderCode) throws SQLTemplateException;
	public void update(String id);
	public void copy(String id)throws Exception;
	public <T> Map<String, Object> findPageInfo1(Filter filter, Page page) throws Exception;
	public List<Map<String,Object>> getOrderPartCount(Long id);
	public Map<String,Object> countRollsAndTrays(Long salesOrderDetailId);
	public <T> Map<String, Object> findPageOut(Filter filter, Page page) throws Exception;
	public <T> Map<String, Object> findPageQuantity(Filter filter, Page page) throws Exception;
	//月度订单产品汇总
	public <T> Map<String, Object> findPageSummaryMonthly(Filter filter, Page page) throws Exception;
	//public void fix(int type);
	public String hasPackTask(Long salesOrderId);
	public void setAllocated(String ids);
	/**
	 * 根据订单号查询包含的所有托
	 */
	public  List<TrayBarCode>  findTrayBySalesOrdercode(String salesOrderCode);

	List<Map<String, Object>> findOrder();

	List<Map<String, Object>> findDetailByCodes(String salesOrderCode) throws Exception;

 List<Map<String, Object>> findSalesOrder(String data) throws Exception;

	public List<Map<String, Object>> findD(String id);

	public List<Map<String, Object>> findV(String id, String vId);

	public List<Map<String, Object>> findBom(String id);

	public List<Map<String, Object>> findP(String id, String vId);

	List<Map<String,Object>> findListByMap1(Long formId);

    List<Map<String, Object>> selectSalesOrder(String salesOrderSubCode, String factoryProductName);
}
