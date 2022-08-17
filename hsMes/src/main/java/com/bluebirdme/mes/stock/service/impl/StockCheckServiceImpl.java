/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.stock.service.impl;

import com.bluebirdme.mes.core.annotation.AnyExceptionRollback;
import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.stock.dao.IStockCheckDao;
import com.bluebirdme.mes.stock.service.IStockCheckService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author 肖文彬
 * @Date 2016-11-8 15:25:19
 */
@Service
@AnyExceptionRollback
public class StockCheckServiceImpl extends BaseServiceImpl implements IStockCheckService {
    @Resource
    IStockCheckDao stockCheckDao;

    @Override
    protected IBaseDao getBaseDao() {
        return stockCheckDao;
    }

    @Override
    public Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
        return stockCheckDao.findPageInfo(filter, page);
    }

    @Override
    public List<Map<String, Object>> findR(String id) {
        return stockCheckDao.findR(id);
    }
}
