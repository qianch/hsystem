/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.planner.delivery.service;

import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import com.bluebirdme.mes.audit.entity.AuditInstance;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.IBaseService;
import com.bluebirdme.mes.planner.delivery.entity.DeliveryPlan;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 
 * @author 徐波
 * @Date 2016-11-2 9:30:07
 */
public interface IDeliveryPlanService extends IBaseService {
	public void saveDatas(DeliveryPlan deliveryPlan)throws Exception;
	public void deleteDatas(List<DeliveryPlan> list)throws Exception;
	public void closeDeliveryPlan(Long did);

	/**
	 * 释放出库计划为为出库
	 */
	public String releaseDeliveryPlan(Long did);

	/**
	 * PDA成品出库加载出货单号
	 */
	public List<Map<String, Object>> findDeliveryCode();
	
	public List<Map<String,Object>> getDeliveryProducts(Long deliveryId);
	
	public String getSerial(String type);
	
	
	
	public int getxdl(String id, String pch);

	public int getOrderXdl(long salesOrderDetailId, String partName);

	public int getPlanXdl(long salesOrderDetailId, String batchCode,String  partName);
	
	public int getkcl(String id, String pch, String cnmc, String bjmc);
	
	public int getfhl(String id, String pch, String cnmc, String bjmc);

	public int getOrderFhl(String salesOrderCode);

	public int getDetailPlanOrderFhl(String salesOrderCode,String batchCode);
	
	public int getdjs(String id, String pch);
	
	public void deleteAll(String ids);
	public void cannel(String ids);
	
	public <T> Map<String, Object> findTcPageInfo(Filter filter, Page page) throws Exception;
	public <T> Map<String, Object> findPageInfoDelivery(Filter filter, Page page) throws Exception;
	public List<Map<String,Object>> getBatchCodeCountBySalesOrderCode(String salesOrderCode,Long productId,Long partId);
	public String copyBarcodeImgs(String ids, String uuid) throws FileNotFoundException;
	public List<Map<String,Object>> searchProduct(String salesOrderSubCode,String batchCode,Long productId,Long partId);
	/**
	 * 查找审核人员
	 * @param entityJavaClass java实体类名
	 * @param formId 表单Id
	 * @return
	 */
	public  List<Map<String,Object>> searchAuditer(String entityJavaClass,Long formId);
	public  List<Map<String,Object>> findProductOutRecordByPackingNumber(String packingNumber);
	public List<String> cars();
	public  void  copy(String id) throws Exception;

	public SXSSFWorkbook exportDeliveryExcel(String ids) throws Exception;

	void unbindingPDA(String id);
	public List<Map<String, Object>> findDeliverySlipMirror(Long id);

	List<Map<String, Object>> findDeliverySlip(long id);
}
