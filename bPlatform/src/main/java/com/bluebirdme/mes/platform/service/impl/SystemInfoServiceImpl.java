package com.bluebirdme.mes.platform.service.impl;

import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.platform.dao.ISystemInfoDao;
import com.bluebirdme.mes.platform.service.ISystemInfoService;
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
public class SystemInfoServiceImpl extends BaseServiceImpl implements ISystemInfoService {
    @Resource
    ISystemInfoDao sysInfoDao;

    @Override
    protected IBaseDao getBaseDao() {
        return this.sysInfoDao;
    }

    @Override
    public Object findUserCount() {
        return this.sysInfoDao.findUserCount();
    }

    @Override
    public Map<String, Object> findPageInfo(final Filter filter, final Page page) throws Exception {
        return null;
    }
}
