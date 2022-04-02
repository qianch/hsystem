package com.bluebirdme.mes.platform.service.impl;

import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.platform.dao.ILogDao;
import com.bluebirdme.mes.platform.service.ILogService;
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
public class LogServceImpl extends BaseServiceImpl implements ILogService {
    @Resource
    ILogDao logDao;

    @Override
    protected IBaseDao getBaseDao() {
        return logDao;
    }

    @Override
    public <T> Map<String, Object> findPageInfo(final Filter filter, final Page page) throws Exception {
        return logDao.findPageInfo(filter, page);
    }

    @Override
    public void clearAll() {
        logDao.clearAll();
    }
}
