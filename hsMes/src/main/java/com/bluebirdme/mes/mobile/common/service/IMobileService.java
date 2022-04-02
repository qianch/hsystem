/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.mobile.common.service;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import com.bluebirdme.mes.baseInfo.entity.FtcBom;
import com.bluebirdme.mes.baseInfo.entityMirror.FtcBomMirror;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.IBaseService;
import com.bluebirdme.mes.store.entity.BarCodeRegType;
import com.bluebirdme.mes.store.entity.BarCodeType;

/**
 * @author 高飞
 * @Date 2016-11-6 10:22:52
 */
public interface IMobileService extends IBaseService {
	public <T> T findBarCodeReg(BarCodeRegType type,String barcode);
	public <T> T findBarcodeInfo(BarCodeType type,String barcode);
	public BigInteger isCount(String batchcode,Integer productWeigh);
	public Double isAvg(String barcode,Integer productWeigh);
	public BigInteger isCount1(String batchcode,String producePlanCode,String factoryproductname);
	public String getOrderCnt(String salesOrderSubCode,String productBatchCode);
	public String getStrockCnt(String salesOrderCode, String batchCode, String factoryProductName, String bjmc);
	public String getPackageCnt(String salesOrderCode,String batchCode);
}
