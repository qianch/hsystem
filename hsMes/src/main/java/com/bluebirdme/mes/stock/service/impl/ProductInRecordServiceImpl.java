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
import com.bluebirdme.mes.stock.dao.IProductInRecordDao;
import com.bluebirdme.mes.stock.entity.ProductInRecord;
import com.bluebirdme.mes.stock.service.IProductInRecordService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author 宋黎明
 * @Date 2016-11-14 11:42:22
 */
@Service
@AnyExceptionRollback
public class ProductInRecordServiceImpl extends BaseServiceImpl implements IProductInRecordService {
    @Resource
    IProductInRecordDao productInRecordDao;

    @Override
    protected IBaseDao getBaseDao() {
        return productInRecordDao;
    }

    @Override
    public Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
        return productInRecordDao.findPageInfo(filter, page);
    }

    @Override
    public Map<String, Object> productsShopStatistics(Filter filter, Page page) throws Exception {
        return productInRecordDao.productsShopStatistics(filter, page);
    }

    @Override
    public Map<String, Object> shopStorageCategoryStatistics(Filter filter, Page page) throws Exception {
        return productInRecordDao.shopStorageCategoryStatistics(filter, page);
    }

    @Override
    public Map<String, Object> findPageInfoDRK(Filter filter, Page page) {
        return productInRecordDao.findPageInfoDRK(filter, page);
    }

    @Override
    public ProductInRecord findfirstProductInRecord(String column, Object value) {
        return productInRecordDao.findfirstProductInRecord(column, value);
    }
}
