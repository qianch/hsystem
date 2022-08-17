package com.bluebirdme.mes.stock.service;

import com.bluebirdme.mes.core.base.service.IBaseService;
import com.bluebirdme.mes.core.sql.SQLTemplateException;

import java.util.Map;

/**
 * @author Goofy
 * @Date 2016年11月24日 下午6:59:12
 */
public interface IStockService extends IBaseService {
    Map<String, Object> list(String type, String[] kuweis) throws SQLTemplateException;
}
