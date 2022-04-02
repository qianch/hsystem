/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.stock.dao;

import java.util.List;
import java.util.Map;

import com.bluebirdme.mes.core.base.dao.IBaseDao;
/**
 * 
 * @author 宋黎明
 * @Date 2016-11-27 13:57:45
 */

public interface IProductDeliveryRecordDao extends IBaseDao {
	/**
	 * 根据出库单号查询出库记录
	 * @param deliveryCode
	 * @return
	 */
	public List<Map<String,Object>> findOutRecordByDeliveryCode(String deliveryCode);
}
