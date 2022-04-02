/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.stock.service;

import java.util.List;
import java.util.Map;

import com.bluebirdme.mes.baseInfo.entity.Material;
import com.bluebirdme.mes.core.base.service.IBaseService;
import com.bluebirdme.mes.core.sql.SQLTemplateException;
import com.bluebirdme.mes.stock.entity.MaterialForceOutRecord;
import com.bluebirdme.mes.stock.entity.MaterialInRecord;
import com.bluebirdme.mes.stock.entity.MaterialStockState;
import com.bluebirdme.mes.stock.entity.StockCheck;
import com.bluebirdme.mes.stock.entity.StockMove;
import com.bluebirdme.mes.stock.entity.MaterialStockOut;

/**
 * 
 * @author 徐波
 * @Date 2016-10-24 15:08:19
 */
public interface IMaterialStockService extends IBaseService {
	
	//根据大类和型号查询原料信息
	public Material findMaterial(String produceCategory,String materialModel);
	
	/**
	 * 出库之前生成出库单号
	 * @return
	 * @throws Exception
	 */
	public String getSerial() throws Exception;
	
	/**
	 * 原料入库
	 * @param mss
	 * @param mir
	 * @return
	 */
	public void mIn(MaterialStockState mss,MaterialInRecord mir);
	
	/**
	 * 原料退库
	 * @param mir
	 * @param palletCode
	 */
	public void back(MaterialInRecord mir,String palletCode);
	
	/**
	 * 原料出库 车间领料
	 * @param mso
	 * @param mssIds
	 */
	public void out(MaterialStockOut mso,Long[] mssIds) throws Exception;
	
	/**
	 * 验证原料在库状态，返回不在库的条码
	 * @param ids
	 * @return
	 */
	public String validMaterialStockState(Long []ids);
	
	/**
	 * 异常退库（退回巨石）
	 * @param out
	 * @throws Exception
	 */
	public void backToJuShi(MaterialForceOutRecord out) throws Exception;
	
	public void move(Long ids[],String warehouseCode,String warehousePosCode);
	
	public void checkResult(StockCheck sc);
	
	public String getOutWorkShop(String palletCode);
}
