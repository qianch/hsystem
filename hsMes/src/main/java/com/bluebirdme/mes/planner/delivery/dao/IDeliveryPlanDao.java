/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.planner.delivery.dao;

import java.util.List;
import java.util.Map;

import com.bluebirdme.mes.audit.entity.AuditInstance;
import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;

import org.springframework.stereotype.Repository;
/**
 * 
 * @author 徐波
 * @Date 2016-11-2 9:30:07
 */

public interface IDeliveryPlanDao extends IBaseDao {
	/**
	 * PDA成品出库加载出货单号
	 */
	public List<Map<String, Object>> findDeliveryCode();
	public List<Map<String,Object>> getDeliveryProducts(Long deliveryId);
	public String getSerial(String type);
	public <T> Map<String, Object> findTcPageInfo(Filter filter, Page page)throws Exception;
	public <T> Map<String, Object> findPageInfoDelivery(Filter filter, Page page) throws Exception;
	public List<Map<String,Object>> getBatchCodeCountBySalesOrderCode(String salesOrderCode,Long productId,Long partId);
	public List<Map<String, Object>> searchProduct(String salesOrderSubCode, String batchCode, Long productId, Long partId);
	/**
	 * 查找审核人员
	 * @param entityJavaClass java实体类名
	 * @param formId 表单Id
	 * @return
	 */
	public  List<Map<String,Object>>  searchAuditer(String entityJavaClass,Long formId);
	public  List<Map<String, Object>> findProductOutRecordByPackingNumber(String packingNumber);
	public List<String> cars();
	
	
	
	
	public int getxdl(String id, String pch);
	public int getkcl(String id, String pch, String cnmc, String bjmc);
	public int getfhl(String id, String pch, String cnmc, String bjmc);
	public int getdjs(String id, String pch);
	public int getOrderFhl(String salesOrderCode);
	public int getDetailPlanOrderFhl(String salesOrderCode,String batchCode);

	//查询仓库信息
	public List<Map<String, Object>> QueryProductWareHouse(long deliveryId);
	//查询明阳风电导出数据
	public List<Map<String, Object>> findDeliverySlipMirror(Long id);

	List<Map<String, Object>> findDeliverySlip(long id);
}
