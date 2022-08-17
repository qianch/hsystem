/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.stock.service;

import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.IBaseService;
import com.bluebirdme.mes.core.sql.SQLTemplateException;
import com.bluebirdme.mes.stock.entity.*;

import java.util.List;
import java.util.Map;

/**
 * @author 宋黎明
 * @Date 2016-10-24 15:08:20
 */
public interface IProductStockService extends IBaseService {
    /**
     * 在途库状态更新
     * 保存库存信息
     */
    String POnTheWay(String code, String warehousecode, String logisticscompany, String plate, long loginid) throws Exception;

    /**
     * 老入库
     * 保存库存信息，入库记录
     */
    String saveInRecordAndStock(ProductInRecord productInRecord, Long overTime) throws Exception;

    /**
     * 新入库
     * 保存库存信息，入库记录
     */
    String pIn(ProductInRecord productInRecord, Long overTime) throws Exception;

    /**
     * 老出库
     * 更改库存信息，保存出库记录
     */
    String saveOutRecordAndUpdateStock(String codes, Long UserId, String packingNum, String plate, String boxNumber, Double count) throws Exception;

    /**
     * 新出库
     * 更改库存信息，保存出库记录
     */
    String pOut(String codes, Long UserId, String packingNum, String plate, String boxNumber, Double count, int isfinished) throws Exception;

    /**
     * 移库
     * 保存移库信息并更新库存信息
     */
    String saveAndUpdate(StockMove stockMove, String code) throws Exception;

    String pMove(StockMove stockMove, String code) throws Exception;

    /**
     * 通过仓库代码和库位代码查询产品库存信息
     */
    List<Map<String, Object>> findProductStockInfo(String warehouseCode, String warehousePosCode) throws SQLTemplateException;

    List<Map<String, Object>> getMoveInfoBybarcode(String barcode) throws SQLTemplateException;

    //获取成品状态
    String GetStockState(int stockState);

    /**
     * 查询条码的库存状态
     */
    Map<String, Object> findStateByCode(String barCode, String materialCode) throws Exception;

    /**
     * 条码号冻结
     *
     * @param codes
     * @return
     * @throws Exception
     */
    void doFreeze(String codes) throws Exception;

    List<Map<String, Object>> findRoll(String code);

    List<Map<String, Object>> findTray(String code);

    List<Map<String, Object>> findMaterial(String code);

    /**
     * 通过条码查询成品信息
     */
    List<Map<String, Object>> findProductInfo(String trayCode, String boxCode, String rollCode);

    String saveAndUpdate1(StockFabricMove stockMove, String code) throws Exception;

    void abandon(String code) throws Exception;

    void abandon(String code, String userId) throws Exception;

    /**
     * 查询同一批次的其他货物所在的位置
     */
    String pwarhourse(String salesOrderCode, String batchCode, String productModel) throws Exception;

    /**
     * 库龄明细表
     */
    Map<String, Object> warehouseDetail(Filter filter, Page page);

    /**
     * 库龄汇总表
     */
    Map<String, Object> summaryDetail(Filter filter, Page page);

    /**
     * 库龄数量对比表
     */
    Map<String, Object> comparisonDetail(Filter filter, Page page);

    /**
     * 胚布仓库
     */
    Map<String, Object> getGreigeStockInfo(Filter filter, Page page);

    Map<String, Object> stockView(Filter filter, Page page) throws Exception;

    Map<String, Object> stockViewNew(Filter filter, Page page) throws Exception;

    Map<String, Object> stockViewNewPcj(Filter filter, Page page) throws Exception;

    /**
     * 待入库
     * 保存库存信息，入库记录
     */
    String saveStockPending(ProductInRecord productInRecord, Long overTime) throws Exception;

    String pBack(ProductStockTran productStockTran, String code) throws Exception;

    String pbPendingIn(PendingInRecord pendingInRecord, Long overTime) throws Exception;

    String pbIn(ProductInRecord productInRecord) throws Exception;

    String savePbIn(ProductInRecord productInRecord, Long overTime) throws Exception;

    Map<String, Object> findPageInfoMoveList(Filter filter, Page page);

    String pwarhourseName(String warehouseCode);

    /**
     * 查询同一批次待入库的其他货物所在的位置
     */
    String pendingWarhourse(String salesOrderCode, String batchCode, String productModel) throws Exception;

    String pIns(List<ProductInRecord> productInRecordlist) throws Exception;

    String pbIns(List<ProductInRecord> productInRecordlist) throws Exception;

    /**
     * 胚布批量待出库
     */
    String pbPicks(List<FabricPickRecord> fabricPickRecordlist) throws Exception;


    /**
     * 移库记录
     */
    Map<String, Object> moveInfolist(Filter filter, Page page);
}
