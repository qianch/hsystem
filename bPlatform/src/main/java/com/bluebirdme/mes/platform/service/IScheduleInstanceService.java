package com.bluebirdme.mes.platform.service;

import com.bluebirdme.mes.core.base.service.IBaseService;
import com.bluebirdme.mes.platform.entity.ScheduleInstance;
import org.quartz.SchedulerException;

/**
 * @author qianchen
 * @date 2020/05/21
 */
public interface IScheduleInstanceService extends IBaseService {
    void start(final ScheduleInstance p0) throws SchedulerException, ClassNotFoundException;

    void stop(final ScheduleInstance p0) throws SchedulerException, ClassNotFoundException;
}
