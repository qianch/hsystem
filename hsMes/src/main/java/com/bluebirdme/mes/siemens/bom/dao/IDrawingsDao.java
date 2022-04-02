/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.siemens.bom.dao;

import java.util.List;
import java.util.Map;

import com.bluebirdme.mes.core.base.dao.IBaseDao;
/**
 * 
 * @author 高飞
 * @Date 2017-7-18 14:06:57
 */

public interface IDrawingsDao extends IBaseDao {
	public List<Map<String, Object>> drawingsList(Long partId);
	public int[] getSuitCountPerDrawings(Long partId);
}
