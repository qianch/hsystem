package com.bluebirdme.mes.stock.service.impl;

import com.bluebirdme.mes.core.annotation.AnyExceptionRollback;
import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.core.sql.SQLTemplateException;
import com.bluebirdme.mes.stock.dao.IStockDao;
import com.bluebirdme.mes.stock.service.IStockService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 库存Service
 *
 * @author Goofy
 * @Date 2016年11月24日 下午6:59:31
 */
@Service
@AnyExceptionRollback
public class StockServiceImpl extends BaseServiceImpl implements IStockService {
    @Resource
    IStockDao dao;

    @Override
    protected IBaseDao getBaseDao() {
        return dao;
    }

    @Override
    public Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
        return null;
    }

    public Map<String, Object> list(String type, String[] kuweis) throws SQLTemplateException {
        return dao.list(type, kuweis);
    }
}
