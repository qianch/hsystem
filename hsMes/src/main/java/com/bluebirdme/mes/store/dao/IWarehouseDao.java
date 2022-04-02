/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.store.dao;

import java.util.List;
import java.util.Map;

import com.bluebirdme.mes.core.base.dao.IBaseDao;

import com.bluebirdme.mes.core.sql.SQLTemplateException;
import org.springframework.stereotype.Repository;
/**
 * 
 * @author 肖文彬
 * @Date 2016-9-29 15:45:32
 */

public interface IWarehouseDao extends IBaseDao {
	public void delete(String ids);
	public void updateS(String ids);
	public List<Map<String, Object>> combobox();

	public List<Map<String, Object>> queryWarehousebyType(String waretype) throws SQLTemplateException;
}
