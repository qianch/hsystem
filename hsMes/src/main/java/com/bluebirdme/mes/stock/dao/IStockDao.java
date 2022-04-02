package com.bluebirdme.mes.stock.dao;

import java.util.List;
import java.util.Map;

import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.sql.SQLTemplateException;

/**
 * 
 * @author Goofy
 * @Date 2016年11月24日 下午3:04:46
 */
public interface IStockDao extends IBaseDao {
	public Map<String, Object> list(String type,String[] kuweis) throws SQLTemplateException;
}
