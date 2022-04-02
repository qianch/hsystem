/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.store.service;

import com.bluebirdme.mes.core.base.service.IBaseService;
import com.bluebirdme.mes.planner.cut.entity.CutPlan;
import com.bluebirdme.mes.planner.weave.entity.WeavePlan;
import com.bluebirdme.mes.sales.entity.SalesOrderDetail;
import com.bluebirdme.mes.store.entity.BarCodeRegType;
import com.bluebirdme.mes.store.entity.BarCodeType;

/**
 * 
 * @author 高飞
 * @Date 2017-3-20 20:34:16
 */
public interface IBarCodeService extends IBaseService {
	/**
	 * 通过条码获取订单明细
	 * @param code
	 * @return
	 */
	public SalesOrderDetail getSalesOrderDetail(String code);
	/**
	 * 通过条码获取裁剪计划
	 * @param code
	 * @return
	 */
	public CutPlan getCutPlan(String code);
	/**
	 * 通过条码获取编织计划
	 * @param code
	 * @return
	 */
	public WeavePlan getWeavePlan(String code);
	/**
	 * 通过条码获取条码登记信息
	 * @param type
	 * @param barcode
	 * @return
	 */
	public <T> T findBarCodeReg(BarCodeRegType type,String barcode);
	/**
	 * 通过条码获取条码信息
	 * @param type
	 * @param barcode
	 * @return
	 */
	public <T> T findBarcodeInfo(BarCodeType type,String barcode);
	
	public int countRollsInTray(String trayCode);
	
	/**
	 * 校验条码是否被打包
	 * @param code
	 * @return
	 */
	public boolean packed(String code);
	
}
