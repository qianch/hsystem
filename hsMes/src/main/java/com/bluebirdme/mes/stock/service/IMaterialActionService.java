package com.bluebirdme.mes.stock.service;

import java.util.Map;

import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.IBaseService;

public interface IMaterialActionService extends IBaseService{
	/**
	 * 原料出库(明细)
	 * @param filter
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public <T> Map<String, Object> findPageOutInfo(Filter filter, Page page) throws Exception;
	/**
	 * 原料库存
	 * @param filter
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public <T> Map<String, Object> findPageDetailInfo(Filter filter, Page page) throws Exception;
	/**
	 * 异常退回巨石
	 * @param filter
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public <T> Map<String, Object> findPageForceOutInfo(Filter filter, Page page) throws Exception;
	
	/**
	 * 根据ID 冻结\解冻|放行\取消放行
	 * @param mssId
	 * @param action
	 * @param actionValue
	 */
	public void action(Long mssId[],String action,String actionValue) throws Exception;
}
