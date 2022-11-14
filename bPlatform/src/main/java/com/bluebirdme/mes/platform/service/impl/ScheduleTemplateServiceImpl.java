package com.bluebirdme.mes.platform.service.impl;

import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.platform.dao.IScheduleTemplateDao;
import com.bluebirdme.mes.platform.service.IScheduleTemplateService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Map;

/**
 * @author qianchen
 * @date 2020/05/21
 */

@Service
@Transactional
public class ScheduleTemplateServiceImpl extends BaseServiceImpl implements IScheduleTemplateService {
    @Resource
    IScheduleTemplateDao scheduleTemplateDao;

    @Override
    protected IBaseDao getBaseDao() {
        return this.scheduleTemplateDao;
    }

    @Override
    public Map<String, Object> findPageInfo(final Filter filter, final Page page) throws Exception {
        return this.scheduleTemplateDao.findPageInfo(filter, page);
    }
}
