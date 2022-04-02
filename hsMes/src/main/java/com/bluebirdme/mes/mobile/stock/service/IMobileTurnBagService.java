package com.bluebirdme.mes.mobile.stock.service;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.core.base.service.IBaseService;
import com.bluebirdme.mes.planner.turnbag.entity.TurnBagPlan;

/**
 * 类注释
 * @author Goofy
 * @Date 2017年2月11日 下午3:49:43
 */
public interface IMobileTurnBagService extends IBaseService {
	public void turnbag(Long tid,Long[] dids,String[] codes) throws Exception;
}
