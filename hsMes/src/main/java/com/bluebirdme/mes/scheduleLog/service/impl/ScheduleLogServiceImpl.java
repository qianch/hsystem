/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.scheduleLog.service.impl;

import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.scheduleLog.dao.IScheduleLogDao;
import com.bluebirdme.mes.scheduleLog.service.IScheduleLogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;

/**
 * @author 徐秦冬
 * @Date 2018-2-8 10:50:23
 */
@Service
@Transactional
public class ScheduleLogServiceImpl extends BaseServiceImpl implements IScheduleLogService {
    @Resource
    IScheduleLogDao scheduleLogDao;

    @Override
    protected IBaseDao getBaseDao() {
        return scheduleLogDao;
    }

    public void clearAll() {
        scheduleLogDao.clearAll();
    }
}
