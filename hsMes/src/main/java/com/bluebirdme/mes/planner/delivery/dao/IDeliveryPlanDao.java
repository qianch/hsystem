/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.planner.delivery.dao;

import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;

import java.util.List;
import java.util.Map;

/**
 * @author 徐波
 * @Date 2016-11-2 9:30:07
 */

public interface IDeliveryPlanDao extends IBaseDao {
    /**
     * PDA成品出库加载出货单号
     */
    List<Map<String, Object>> findDeliveryCode();

    List<Map<String, Object>> getDeliveryProducts(Long deliveryId);

    String getSerial(String type);

    <T> Map<String, Object> findTcPageInfo(Filter filter, Page page) throws Exception;

    <T> Map<String, Object> findPageInfoDelivery(Filter filter, Page page) throws Exception;

    List<Map<String, Object>> getBatchCodeCountBySalesOrderCode(String salesOrderCode, Long productId, Long partId);

    List<Map<String, Object>> searchProduct(String salesOrderSubCode, String batchCode, Long productId, Long partId);

    /**
     * 查找审核人员
     *
     * @param entityJavaClass java实体类名
     * @param formId          表单Id
     * @return
     */
    List<Map<String, Object>> searchAuditer(String entityJavaClass, Long formId);

    List<Map<String, Object>> findProductOutRecordByPackingNumber(String packingNumber);

    List<String> cars();


    int getxdl(String id, String pch);

    int getkcl(String id, String pch, String cnmc, String bjmc);

    int getfhl(String id, String pch, String cnmc, String bjmc);

    int getdjs(String id, String pch);

    int getOrderFhl(String salesOrderCode);

    int getDetailPlanOrderFhl(String salesOrderCode, String batchCode);

    //查询仓库信息
    List<Map<String, Object>> QueryProductWareHouse(long deliveryId);

    //查询明阳风电导出数据
    List<Map<String, Object>> findDeliverySlipMirror(Long id);

    List<Map<String, Object>> findDeliverySlip(long id);
}
