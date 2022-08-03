/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.sales.dao;

import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.sql.SQLTemplateException;
import com.bluebirdme.mes.sales.entity.SalesOrder;
import com.bluebirdme.mes.sales.entity.SalesOrderDetail;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author 高飞
 * @Date 2016-10-13 11:06:42
 */

public interface ISalesOrderDao extends IBaseDao {
    void deleteDetails(Long salesId);

    List<SalesOrderDetail> getDetails(Long salesOrderId);

    List<SalesOrderDetail> findDetailByCode(String salesOrderCode) throws SQLTemplateException;

    List<Map<String, Object>> findDetailByCode2(String orderCode) throws SQLTemplateException;

    List<Map<String, Object>> getOrderPartCount(Long id);

    List<SalesOrder> findUncreatePlans();

    String getSerial(Integer export);

    Map<String, Object> findPageInfo2(Filter filter, Page page) throws Exception;

    void update(String id);

    String getCode(Integer salesOrderIsExport, Date salesOrderDate);

    Map<String, Object> findPageInfo1(Filter filter, Page page) throws Exception;

    Map<String, Object> countRollsAndTrays(Long salesOrderDetailId);

    Map<String, Object> findPageOut(Filter filter, Page page) throws Exception;

    Map<String, Object> findPageQuantity(Filter filter, Page page) throws Exception;

    //月度订单产品汇总
    Map<String, Object> findPageSummaryMonthly(Filter filter, Page page) throws Exception;

    String hasPackTask(Long salesOrderId);

    void setAllocated(String ids);

    List<Map<String, Object>> findOrder();

    List<Map<String, Object>> findDetailByCodes(String salesOrderCode) throws Exception;

    List<Map<String, Object>> findSalesOrder(String data) throws Exception;

    List<Map<String, Object>> findD(String id);

    List<Map<String, Object>> findFV(String id);

    List<Map<String, Object>> findTV(String id);

    List<Map<String, Object>> findFBom(String id);

    List<Map<String, Object>> findTBom(String id);

    List<Map<String, Object>> findP(String id);

    List<Map<String, Object>> findListByMap1(Long id);

    List<Map<String, Object>> selectSalesOrder(String salesOrderSubCode, String factoryProductName);
}
