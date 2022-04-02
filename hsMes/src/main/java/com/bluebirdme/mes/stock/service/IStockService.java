package com.bluebirdme.mes.stock.service;

import java.util.Map;

import com.bluebirdme.mes.core.base.service.IBaseService;
import com.bluebirdme.mes.core.sql.SQLTemplateException;

/**
 * 
 * @author Goofy
 * @Date 2016年11月24日 下午6:59:12
 */
public interface IStockService extends IBaseService {
	public Map<String, Object> list(String type,String[] kuweis) throws SQLTemplateException;
}
