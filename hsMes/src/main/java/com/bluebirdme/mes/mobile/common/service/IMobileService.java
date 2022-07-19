/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.mobile.common.service;

import com.bluebirdme.mes.core.base.service.IBaseService;
import com.bluebirdme.mes.store.entity.BarCodeRegType;
import com.bluebirdme.mes.store.entity.BarCodeType;

import java.math.BigInteger;

/**
 * @author 高飞
 * @Date 2016-11-6 10:22:52
 */
public interface IMobileService extends IBaseService {
    <T> T findBarCodeReg(BarCodeRegType type, String barcode);

    <T> T findBarcodeInfo(BarCodeType type, String barcode);

    BigInteger isCount(String batchcode, Integer productWeigh);

    Double isAvg(String barcode, Integer productWeigh);

    BigInteger isCount1(String batchcode, String producePlanCode, String factoryproductname);

    String getOrderCnt(String salesOrderSubCode, String productBatchCode);

    String getStrockCnt(String salesOrderCode, String batchCode, String factoryProductName, String bjmc);

    String getPackageCnt(String salesOrderCode, String batchCode);
}
