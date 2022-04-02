package com.bluebirdme.mes.platform.service;

import com.bluebirdme.mes.core.base.service.IBaseService;
import com.bluebirdme.mes.platform.entity.Schedule;
import org.quartz.SchedulerException;

import java.util.List;

/**
 * @author qianchen
 * @date 2020/05/21
 */
@Deprecated
public interface IScheduleService extends IBaseService {
    Schedule getScheduleByClazz(final String p0);

    void addSchedule(final Schedule p0) throws ClassNotFoundException, SchedulerException;

    void deleteSchedule(final Schedule p0, final boolean p1) throws SchedulerException;

    void deleteSchedule(final Schedule p0) throws SchedulerException;

    void startSchedule(final Schedule p0) throws SchedulerException, ClassNotFoundException;

    void pauseSchedule(final Schedule p0) throws SchedulerException;

    List<Schedule> findUncompleteSchedule();
}
