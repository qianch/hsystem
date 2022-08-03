/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.sales.service.impl;

import com.bluebirdme.mes.core.annotation.AnyExceptionRollback;
import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.sales.dao.ISalesOrderStockDao;
import com.bluebirdme.mes.sales.service.ISalesOrderStockService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author 高飞
 * @Date 2016-12-15 18:28:22
 */
@Service
@AnyExceptionRollback
public class SalesOrderStockServiceImpl extends BaseServiceImpl implements ISalesOrderStockService {
    @Resource
    ISalesOrderStockDao salesOrderStockDao;

    @Override
    protected IBaseDao getBaseDao() {
        return salesOrderStockDao;
    }

    @Override
    public Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
        return salesOrderStockDao.findPageInfo(filter, page);
    }
}
