/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.mobile.common.service.impl;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.bluebirdme.mes.baseInfo.entity.FtcBom;
import com.bluebirdme.mes.baseInfo.entityMirror.FtcBomMirror;
import org.springframework.stereotype.Service;

import com.bluebirdme.mes.core.annotation.AnyExceptionRollback;
import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.mobile.common.dao.IMobileDao;
import com.bluebirdme.mes.mobile.common.service.IMobileService;
import com.bluebirdme.mes.store.entity.BarCodeRegType;
import com.bluebirdme.mes.store.entity.BarCodeType;
import com.bluebirdme.mes.store.entity.Box;
import com.bluebirdme.mes.store.entity.BoxBarcode;
import com.bluebirdme.mes.store.entity.PartBarcode;
import com.bluebirdme.mes.store.entity.Roll;
import com.bluebirdme.mes.store.entity.RollBarcode;
import com.bluebirdme.mes.store.entity.Tray;
import com.bluebirdme.mes.store.entity.TrayBarCode;

/**
 * 
 * @author 高飞
 * @Date 2016-11-6 10:22:52
 */
@Service
@AnyExceptionRollback
public class MobileServiceImpl extends BaseServiceImpl implements IMobileService {

	@Resource
	IMobileDao mobileDao;

	@Override
	protected IBaseDao getBaseDao() {
		return mobileDao;
	}

	@Override
	public <T> Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T findBarCodeReg(BarCodeRegType type, String barcode) {

		Map<String, Object> param = new HashMap<String, Object>();

		switch (type) {
		case TRAY:
			param.put("trayBarcode", barcode);
			return (T) findUniqueByMap(Tray.class, param);
		case BOX:
			param.put("boxBarcode", barcode);
			return (T) findUniqueByMap(Box.class, param);
		case ROLL:
			if (barcode.startsWith("P")) {
				param.put("partBarcode", barcode);
				return (T) findUniqueByMap(Roll.class, param);
			} else {
				param.put("rollBarcode", barcode);
				return (T) findUniqueByMap(Roll.class, param);
			}
		default:
			break;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T findBarcodeInfo(BarCodeType type, String barcode) {
		Map<String, Object> param = new HashMap<String, Object>();

		switch (type) {
		case TRAY:
			param.put("barcode", barcode);
			return (T) findUniqueByMap(TrayBarCode.class, param);
		case BOX:
			param.put("barcode", barcode);
			return (T) findUniqueByMap(BoxBarcode.class, param);
		case ROLL:
			param.put("barcode", barcode);
			return (T) findUniqueByMap(RollBarcode.class, param);
		case PART:
			param.put("barcode", barcode);
			return (T) findUniqueByMap(PartBarcode.class, param);
		default:
			break;
		}
		return null;
	}

	public Double isAvg(String barcode, Integer productWeigh) {
		return mobileDao.isAvg(barcode, productWeigh);
	}

	public BigInteger isCount(String barcode, Integer productWeigh) {
		return mobileDao.isCount(barcode, productWeigh);
	}

	public BigInteger isCount1(String batchcode, String producePlanCode, String factoryproductname) {
		return mobileDao.isCount1(batchcode, producePlanCode, factoryproductname);
	}

	public String getOrderCnt(String salesOrderSubCode, String productBatchCode) {
		return mobileDao.getOrderCnt(salesOrderSubCode, productBatchCode);
	}

	public String getStrockCnt(String salesOrderCode, String batchCode, String factoryProductName, String bjmc) {
		return mobileDao.getStrockCnt(salesOrderCode, batchCode, factoryProductName, bjmc);
	}

	@Override
	public String getPackageCnt(String salesOrderCode, String batchCode) {
		return mobileDao.getPackageCnt(salesOrderCode, batchCode);
	}
}
