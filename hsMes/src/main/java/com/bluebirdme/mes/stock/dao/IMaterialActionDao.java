package com.bluebirdme.mes.stock.dao;

import java.util.Map;

import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;

public interface IMaterialActionDao extends IBaseDao {
	public <T> Map<String, Object> findPageOutInfo(Filter filter, Page page) throws Exception;
	public <T> Map<String, Object> findPageDetailInfo(Filter filter, Page page) throws Exception;
	public <T> Map<String, Object> findPageForceOutInfo(Filter filter, Page page) throws Exception;
	
	/**
	 * 根据ID 冻结\解冻|放行\取消放行
	 * @param mssId
	 * @param action
	 * @param actionValue
	 */
	public void action(Long mssId[],String action,String actionValue);
}
