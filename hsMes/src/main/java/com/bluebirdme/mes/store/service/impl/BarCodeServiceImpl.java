/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.store.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import com.bluebirdme.mes.core.annotation.AnyExceptionRollback;

import org.springframework.stereotype.Service;
import org.xdemo.superutil.j2se.StringUtils;

import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.mobile.common.service.IMobileService;
import com.bluebirdme.mes.planner.cut.entity.CutPlan;
import com.bluebirdme.mes.planner.weave.entity.WeavePlan;
import com.bluebirdme.mes.sales.entity.SalesOrderDetail;
import com.bluebirdme.mes.store.dao.IBarCodeDao;
import com.bluebirdme.mes.store.entity.BarCodeRegType;
import com.bluebirdme.mes.store.entity.BarCodeType;
import com.bluebirdme.mes.store.entity.Box;
import com.bluebirdme.mes.store.entity.BoxBarcode;
import com.bluebirdme.mes.store.entity.BoxRoll;
import com.bluebirdme.mes.store.entity.IBarcode;
import com.bluebirdme.mes.store.entity.PartBarcode;
import com.bluebirdme.mes.store.entity.Roll;
import com.bluebirdme.mes.store.entity.RollBarcode;
import com.bluebirdme.mes.store.entity.Tray;
import com.bluebirdme.mes.store.entity.TrayBarCode;
import com.bluebirdme.mes.store.entity.TrayBoxRoll;
import com.bluebirdme.mes.store.service.IBarCodeService;

/**
 * 
 * @author 高飞
 * @Date 2017-3-20 20:34:16
 */
@Service
@AnyExceptionRollback
public class BarCodeServiceImpl extends BaseServiceImpl implements IBarCodeService {

	@Resource
	IBarCodeDao barCodeDao;

	@Resource
	IMobileService mService;

	@Override
	protected IBaseDao getBaseDao() {
		return barCodeDao;
	}

	@Override
	public <T> Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
		return null;
	}

	public SalesOrderDetail getSalesOrderDetail(String code) {
		IBarcode barcode = null;
		PartBarcode partBarcode = mService.findBarcodeInfo(BarCodeType.PART, code);
		if (code.indexOf("P") == 0 && partBarcode !=null) {
			barcode = partBarcode;
			if (barcode.getSalesOrderDetailId() != null)
				return findById(SalesOrderDetail.class, barcode.getSalesOrderDetailId());
			CutPlan cp = findById(CutPlan.class, barcode.getPlanId());
			return findById(SalesOrderDetail.class, cp.getFromSalesOrderDetailId());
		} else if (code.indexOf("R") == 0) {
			barcode = mService.findBarcodeInfo(BarCodeType.ROLL, code);
			if (barcode.getSalesOrderDetailId() != null)
				return findById(SalesOrderDetail.class, barcode.getSalesOrderDetailId());
			WeavePlan wp = findById(WeavePlan.class, barcode.getPlanId());
			return findById(SalesOrderDetail.class, wp.getFromSalesOrderDetailId());
		} else if (code.indexOf("B") == 0) {
			barcode = mService.findBarcodeInfo(BarCodeType.BOX, code);
			if (barcode.getSalesOrderDetailId() != null)
				return findById(SalesOrderDetail.class, barcode.getSalesOrderDetailId());
			WeavePlan wp = getWeavePlan(code);
			if (wp != null)
				return findById(SalesOrderDetail.class, wp.getFromSalesOrderDetailId());
			CutPlan cp = getCutPlan(code);
			if (cp != null)
				return findById(SalesOrderDetail.class, cp.getFromSalesOrderDetailId());
			return null;
		} else {
			barcode = mService.findBarcodeInfo(BarCodeType.TRAY, code);
			if (barcode.getSalesOrderDetailId() != null)
				return findById(SalesOrderDetail.class, barcode.getSalesOrderDetailId());
			WeavePlan wp = getWeavePlan(code);
			if (wp != null)
				return findById(SalesOrderDetail.class, wp.getFromSalesOrderDetailId());
			CutPlan cp = getCutPlan(code);
			if (cp != null)
				return findById(SalesOrderDetail.class, cp.getFromSalesOrderDetailId());
			return null;
		}
	}

	public WeavePlan getWeavePlan(String code) {
		IBarcode barcode = null;
		Map<String, Object> param = new HashMap<String, Object>();

		if (code.indexOf("R") == 0) {
			barcode = mService.findBarcodeInfo(BarCodeType.ROLL, code);
			WeavePlan wp = findById(WeavePlan.class, barcode.getPlanId());
			return wp;
		} else if (code.indexOf("B") == 0) {
			param.put("boxBarcode", code);
			List<BoxRoll> list = findListByMap(BoxRoll.class, param);
			if (list.get(0).getRollBarcode() != null) {
				return getWeavePlan(list.get(0).getRollBarcode());
			}
		} else if (code.indexOf("T") == 0 || code.indexOf("P") == 0) {
			param.put("trayBarcode", code);
			List<TrayBoxRoll> list = findListByMap(TrayBoxRoll.class, param);
			if(list.size() > 0){
				if (list.get(0).getBoxBarcode() != null) {
					return getWeavePlan(list.get(0).getBoxBarcode());
				}
				if (list.get(0).getRollBarcode() != null) {
					return getWeavePlan(list.get(0).getRollBarcode());
				}
			}
		}
		return null;
	}

	public CutPlan getCutPlan(String code) {
		IBarcode barcode = null;
		Map<String, Object> param = new HashMap<String, Object>();

		PartBarcode partBarCode = mService.findBarcodeInfo(BarCodeType.PART, code);
		if (code.indexOf("P") == 0 && barcode != null) {
			barcode = partBarCode;
			CutPlan cp = findById(CutPlan.class, barcode.getPlanId());
			return cp;
		} else if (code.indexOf("R") == 0) {
			return null;
		} else if (code.indexOf("B") == 0) {
			barcode = mService.findBarcodeInfo(BarCodeType.BOX, code);
			param.put("boxBarcode", code);
			List<BoxRoll> list = findListByMap(BoxRoll.class, param);
			if (list.get(0).getPartBarcode() != null) {
				return getCutPlan(list.get(0).getPartBarcode());
			}
		} else if (code.indexOf("T") == 0 || code.indexOf("P") == 0) {
			barcode = mService.findBarcodeInfo(BarCodeType.TRAY, code);
			param.put("trayBarcode", code);
			List<TrayBoxRoll> list = findListByMap(TrayBoxRoll.class, param);
			if(list.size() > 0){
				if (list.get(0).getBoxBarcode() != null) {
					return getCutPlan(list.get(0).getBoxBarcode());
				}
				if (list.get(0).getPartBarcode() != null) {
					return getCutPlan(list.get(0).getPartBarcode());
				}
			}
		}
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

	@Override
	public int countRollsInTray(String trayCode) {
		return barCodeDao.countRollsInTray(trayCode);
	}

	public boolean packed(String code) {
		char firstChar = code.charAt(0);
		Map<String, Object> param = new HashMap<String, Object>();
		switch (firstChar) {
		case 'T':
			return false;
		case 'B':
			param.clear();
			param.put("boxBarcode", code);
			return isExist(TrayBoxRoll.class, param, true);
		case 'P':
		case 'R':
			param.clear();
			param.put("rollBarcode", code);
			param.put("partBarcode", code);
			boolean packedToTray = isExist(TrayBoxRoll.class, param, false);
			boolean packageToBox=isExist(BoxRoll.class, param, false);;
			return packageToBox||packedToTray;
		default:
			return false;
		}
	}
}
