/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.stock.dao;

import com.bluebirdme.mes.baseInfo.entity.Material;
import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.stock.entity.StockCheck;
/**
 * 
 * @author 肖文彬
 * @Date 2016-11-16 11:15:02
 */

public interface IMaterialStockDao extends IBaseDao {
	
	//根据大类和型号查询原料信息
	public Material findMaterial(String produceCategory,String materialModel);
	
	/**
	 * 出库之前生成出库单号
	 * @return
	 * @throws Exception
	 */
	public String getSerial() throws Exception;
	
	public String validMaterialStockState(Long []ids);
	
	public void move(Long ids[],String warehouseCode,String warehousePosCode);
	
	public void checkResult(StockCheck sc);
	public String getOutWorkShop(String palletCode);
	
}
