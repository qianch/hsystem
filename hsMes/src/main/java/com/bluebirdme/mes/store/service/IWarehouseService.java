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
import com.bluebirdme.mes.core.sql.SQLTemplateException;

/**
 * 
 * @author 肖文彬
 * @Date 2016-9-29 15:45:32
 */
public interface IWarehouseService extends IBaseService {
	public void delete(String ids);
	public void updateS(String ids);
	public List<Map<String, Object>> warehouse();
	public List<Map<String, Object>> queryWarehousebyType(String waretype)throws SQLTemplateException;
}
