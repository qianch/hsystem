/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.stock.dao;

import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.sql.SQLTemplateException;

import java.util.List;
import java.util.Map;

/**
 * @author 徐波
 * @Date 2016-10-24 15:08:20
 */

public interface IProductStockDao extends IBaseDao {
    /**
     * 通过仓库代码和库位代码查询产品库存信息
     *
     * @param warehouseCode    仓库代码
     * @param warehousePosCode 库位代码
     * @return
     * @throws SQLTemplateException
     */
    List<Map<String, Object>> findProductStockInfo(String warehouseCode, String warehousePosCode) throws SQLTemplateException;

    List<Map<String, Object>> getMoveInfoBybarcode(String barcode) throws SQLTemplateException;

    List<Map<String, Object>> findRoll(String code);

    List<Map<String, Object>> findTray(String code);

    List<Map<String, Object>> findMaterial(String code);

    /**
     * 查询同一批次的其他货物所在的位置
     */
    List<Map<String, Object>> findWarhourse(String salesOrderCode, String batchCode, String productModel) throws Exception;

    /**
     * 通过条码查询成品信息
     *
     * @param trayCode
     * @param boxCode
     * @param rollCode
     * @return
     */
    List<Map<String, Object>> findProductInfo(String trayCode, String boxCode, String rollCode);

    /**
     * 库龄明细
     *
     * @param filter
     * @param page
     * @return
     */

    Map<String, Object> warehouseDetail(Filter filter, Page page);

    /**
     * 库龄汇总表
     *
     * @param filter
     * @param page
     * @return
     */
    Map<String, Object> summaryDetail(Filter filter, Page page);

    /**
     * 库龄数量对比表
     *
     * @param filter
     * @param page
     * @return
     */
    Map<String, Object> comparisonDetail(Filter filter, Page page);

    /**
     * 胚布仓库
     *
     * @param filter
     * @param page
     * @return
     */
    Map<String, Object> getGreigeStockInfo(Filter filter, Page page);

    Map<String, Object> stockView(Filter filter, Page page) throws Exception;

    Map<String, Object> stockViewNew(Filter filter, Page page) throws Exception;

    Map<String, Object> stockViewNewPcj(Filter filter, Page page) throws Exception;

    Map<String, Object> findPageInfoMoveList(Filter filter, Page page);

    List<Map<String, Object>> findPendingWarhourse(String salesOrderCode, String batchCode, String productModel);

    Map<String, Object> moveInfolist(Filter filter, Page page);
}
