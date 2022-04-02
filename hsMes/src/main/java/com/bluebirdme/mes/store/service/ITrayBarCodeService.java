/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.store.service;

import java.util.List;
import java.util.Map;

import com.bluebirdme.mes.core.base.service.IBaseService;
import com.bluebirdme.mes.core.sql.SQL;
import com.bluebirdme.mes.produce.entity.FinishedProduct;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;

/**
 *
 * @author 宋黎明
 * @Date 2016-11-8 14:59:26
 */
public interface ITrayBarCodeService extends IBaseService {
	/**
	 * 根据卷条码查询销售订单信息
	 * @param code 卷条码
	 * @return
	 */
	public List<Map<String, Object>> findSalesOrderByRollcode(String code);

	/**
	 * 根据箱条码查询销售订单信息
	 * @param code 箱条码
	 * @return
	 */
	public List<Map<String, Object>> findSalesOrderByBoxcode(String code);

	/**
	 * 根据托条码查询销售订单信息
	 * @param code 托条码
	 * @return
	 */
	public List<Map<String, Object>> findSalesOrderByTraycode(String code);

	/**
	 * 根据卷条码查询产品信息
	 * @param code 卷条码
	 * @return
	 */
	public List<Map<String, Object>> findProductByRollcode(String code);

	/**
	 * 根据箱条码查询产品信息
	 * @param code 箱条码
	 * @return
	 */
	public List<Map<String, Object>> findProductByBoxcode(String code);

	/**
	 * 根据托条码查询产品信息
	 */
	public FinishedProduct findProductByTraycode(String productIsTc,String trayCode);

	/**
	 * 清空个性化条码编号
	 */
	public String clearTray(String ids) throws Exception;

	public List<Map<String, Object>> findMaxTrayBarCodeCount();


	public List<Map<String, Object>> findMaxTrayPartBarCodeCount();
}
