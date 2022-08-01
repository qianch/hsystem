package com.bluebirdme.mes.produce.service.impl;

import com.bluebirdme.mes.core.annotation.AnyExceptionRollback;
import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.produce.dao.IFinishedProductCategoryDao;
import com.bluebirdme.mes.produce.service.IFinishedProductCategoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author king
 */
@Service
@AnyExceptionRollback
public class FinishedProductCategoryServiceImpl extends BaseServiceImpl implements IFinishedProductCategoryService {
    @Resource
    IFinishedProductCategoryDao productCategoryDao;

    @Override
    protected IBaseDao getBaseDao() {
        return productCategoryDao;
    }

    @Override
    public Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
        return productCategoryDao.findPageInfo(filter, page);
    }
}
