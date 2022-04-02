/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.mobile.produce.dao;

import java.util.List;
import java.util.Map;

import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
/**
 * 
 * @author 徐波
 * @Date 2016-11-8 14:03:33
 */

public interface IFeedingRecordDao extends IBaseDao {
	public <T> Map<String, Object> findPageInfo2(Filter filter, Page page) throws Exception ;
	
	public List<Map<String,Object>> querylist(String deviceCode);
	
	public List<Map<String,Object>> querylist2(String deviceCode);
	
	public void editInfo(String weaveid, String userid, String tlids);
	
	public List<Map<String,Object>> queryYlbh(String ylbh, String oldweaveId, String deviceCode);
	public void deleteYlbh(String ylbh, String oldweaveId, String deviceCode);
}
