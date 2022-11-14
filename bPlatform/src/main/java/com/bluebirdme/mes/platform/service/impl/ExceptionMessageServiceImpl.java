package com.bluebirdme.mes.platform.service.impl;

import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.platform.dao.IExceptionMessageDao;
import com.bluebirdme.mes.platform.service.IExceptionMessageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author qianchen
 * @date 2020/05/21
 */

@Service
@Transactional
public class ExceptionMessageServiceImpl extends BaseServiceImpl implements IExceptionMessageService {
    @Resource
    IExceptionMessageDao exceptionMessageDao;

    @Override
    protected IBaseDao getBaseDao() {
        return exceptionMessageDao;
    }

    @Override
    public Map<String, Object> findPageInfo(final Filter filter, final Page page) throws Exception {
        return this.exceptionMessageDao.findPageInfo(filter, page);
    }

    @Override
    public void clearAll() {
        exceptionMessageDao.clearAll();
    }
}
