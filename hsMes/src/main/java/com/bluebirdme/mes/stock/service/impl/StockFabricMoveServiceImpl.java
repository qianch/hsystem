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
import com.bluebirdme.mes.stock.dao.IStockFabricMoveDao;
import com.bluebirdme.mes.stock.service.IStockFabricMoveService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author 徐波
 * @Date 2017-2-11 8:53:07
 */
@Service
@AnyExceptionRollback
public class StockFabricMoveServiceImpl extends BaseServiceImpl implements IStockFabricMoveService {
    @Resource
    IStockFabricMoveDao stockFabricMoveDao;

    @Override
    protected IBaseDao getBaseDao() {
        return stockFabricMoveDao;
    }

    @Override
    public Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
        return stockFabricMoveDao.findPageInfo(filter, page);
    }
}
