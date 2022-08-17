/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.stock.service;

import com.bluebirdme.mes.baseInfo.entity.Material;
import com.bluebirdme.mes.core.base.service.IBaseService;
import com.bluebirdme.mes.stock.entity.*;

/**
 * @author 徐波
 * @Date 2016-10-24 15:08:19
 */
public interface IMaterialStockService extends IBaseService {
    /**
     * 根据大类和型号查询原料信息
     */
    public Material findMaterial(String produceCategory, String materialModel);

    /**
     * 出库之前生成出库单号
     */
    String getSerial() throws Exception;

    /**
     * 原料入库
     *
     * @param mss
     * @param mir
     * @return
     */
    void mIn(MaterialStockState mss, MaterialInRecord mir);

    /**
     * 原料退库
     */
    void back(MaterialInRecord mir, String palletCode);

    /**
     * 原料出库 车间领料
     */
    void out(MaterialStockOut mso, Long[] mssIds) throws Exception;

    /**
     * 验证原料在库状态，返回不在库的条码
     */
    String validMaterialStockState(Long[] ids);

    /**
     * 异常退库（退回巨石）
     */
    void backToJuShi(MaterialForceOutRecord out) throws Exception;

    void move(Long[] ids, String warehouseCode, String warehousePosCode);

    void checkResult(StockCheck sc);

    String getOutWorkShop(String palletCode);
}
