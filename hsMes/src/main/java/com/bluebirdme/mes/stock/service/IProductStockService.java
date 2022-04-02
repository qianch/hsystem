/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.stock.service;

import java.util.List;
import java.util.Map;

import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.IBaseService;
import com.bluebirdme.mes.core.sql.SQLTemplateException;
import com.bluebirdme.mes.stock.entity.*;

/**
 * @author 宋黎明
 * @Date 2016-10-24 15:08:20
 */
public interface IProductStockService extends IBaseService {

    /**
     * 在途库状态更新
     * 保存库存信息
     */
    public String POnTheWay(String code, String warehousecode, String logisticscompany, String plate, long loginid) throws Exception;

    /**
     * 老入库
     * 保存库存信息，入库记录
     */
    public String saveInRecordAndStock(ProductInRecord productInRecord, Long overTime) throws Exception;

    /**
     * 新入库
     * 保存库存信息，入库记录
     */
    String pIn(ProductInRecord productInRecord, Long overTime) throws Exception;

    /**
     * 老出库
     * 更改库存信息，保存出库记录
     */
    public String saveOutRecordAndUpdateStock(String codes, Long UserId, String packingNum, String plate, String boxNumber, Double count) throws Exception;

    /**
     * 新出库
     * 更改库存信息，保存出库记录
     */
    public String pOut(String codes, Long UserId, String packingNum, String plate, String boxNumber, Double count, int isfinished) throws Exception;

    /**
     * 移库
     * 保存移库信息并更新库存信息
     */
    public String saveAndUpdate(StockMove stockMove, String code) throws Exception;

    public String pMove(StockMove stockMove, String code) throws Exception;

    /**
     * 通过仓库代码和库位代码查询产品库存信息
     *
     * @param warehouseCode    仓库代码
     * @param warehousePosCode 库位代码
     * @return
     * @throws SQLTemplateException
     */
    public List<Map<String, Object>> findProductStockInfo(String warehouseCode, String warehousePosCode) throws SQLTemplateException;

    public List<Map<String, Object>> getMoveInfoBybarcode(String barcode) throws SQLTemplateException;

    //获取成品状态
    public String GetStockState(int stockState);

    /**
     * 查询条码的库存状态
     *
     * @param trayCode     托条码
     * @param boxCode      箱条码
     * @param rollCode     卷条码
     * @param materialCode 物料条码
     * @return IN:在库  OUT:不在库
     * @throws Exception
     */
    public Map<String, Object> findStateByCode(String barCode, String materialCode) throws Exception;

    /**
     * 条码号冻结
     *
     * @param codes
     * @return
     * @throws Exception
     */
    public void doFreeze(String codes) throws Exception;

    public List<Map<String, Object>> findRoll(String code);

    public List<Map<String, Object>> findTray(String code);

    public List<Map<String, Object>> findMaterial(String code);

    /**
     * 通过条码查询成品信息
     *
     * @param trayCode
     * @param boxCode
     * @param rollCode
     * @return
     */
    public List<Map<String, Object>> findProductInfo(String trayCode, String boxCode, String rollCode);

    public String saveAndUpdate1(StockFabricMove stockMove, String code) throws Exception;

    public void abandon(String code) throws Exception;

    public void abandon(String code, String userId) throws Exception;

    /**
     * 查询同一批次的其他货物所在的位置
     *
     * @param salesOrderCode
     * @param batchCode
     * @param productModel
     * @return
     */
    public String pwarhourse(String salesOrderCode, String batchCode, String productModel) throws Exception;

    /**
     * 库龄明细表
     *
     * @param filter
     * @param page
     * @return
     */
    public Map<String, Object> warehouseDetail(Filter filter, Page page);

    /**
     * 库龄汇总表
     *
     * @param filter
     * @param page
     * @return
     */
    public Map<String, Object> summaryDetail(Filter filter, Page page);

    /**
     * 库龄数量对比表
     *
     * @param filter
     * @param page
     * @return
     */
    public Map<String, Object> comparisonDetail(Filter filter, Page page);

    /**
     * 胚布仓库
     *
     * @param filter
     * @param page
     * @return
     */
    public Map<String, Object> getGreigeStockInfo(Filter filter, Page page);

    public <T> Map<String, Object> stockView(Filter filter, Page page) throws Exception;

    public <T> Map<String, Object> stockViewNew(Filter filter, Page page) throws Exception;

    public <T> Map<String, Object> stockViewNewPcj(Filter filter, Page page) throws Exception;
    /**
     * 待入库
     * 保存库存信息，入库记录
     */
    public String saveStockPending(ProductInRecord productInRecord, Long overTime) throws Exception;

    public String pBack(ProductStockTran productStockTran, String code) throws Exception;

    public String pbPendingIn(PendingInRecord pendingInRecord, Long overTime) throws Exception;

    public String pbIn(ProductInRecord productInRecord) throws Exception;

    public String savePbIn(ProductInRecord productInRecord, Long overTime) throws Exception;

    public <T> Map<String, Object> findPageInfoMoveList(Filter filter, Page page);

    public String pwarhourseName(String warehouseCode);

    /**
     * 查询同一批次待入库的其他货物所在的位置
     *
     * @param salesOrderCode
     * @param batchCode
     * @param productModel
     * @return
     */
    public String pendingWarhourse(String salesOrderCode, String batchCode, String productModel) throws Exception;

    public String pIns(List<ProductInRecord> productInRecordlist) throws Exception;

    public String pbIns(List<ProductInRecord> productInRecordlist) throws Exception;

    /**
     * 胚布批量待出库
     *
     * @param fabricPickRecordlist
     * @return
     */
    public String pbPicks(List<FabricPickRecord> fabricPickRecordlist) throws Exception;


    /**
     * 移库记录
     * @param filter
     * @param page
     * @param <T>
     * @return
     */
    public <T> Map<String, Object> moveInfolist(Filter filter, Page page);
}
