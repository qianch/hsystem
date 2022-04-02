package com.bluebirdme.mes.platform.service.impl;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.platform.dao.IScheduleDao;
import com.bluebirdme.mes.platform.entity.Schedule;
import com.bluebirdme.mes.platform.service.IScheduleService;
import org.quartz.*;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

/**
 * @author qianchen
 * @date 2020/05/21
 */
@Deprecated
@Desc("任务调度Service")
@Service
@Transactional
public class ScheduleServiceImpl extends BaseServiceImpl implements IScheduleService {
    @Resource
    IScheduleDao scheduleDao;
    @Resource
    SchedulerFactoryBean schedulerFactoryBean;

    @Override
    protected IBaseDao getBaseDao() {
        return scheduleDao;
    }

    @Override
    public Schedule getScheduleByClazz(final String clazz) {
        return this.scheduleDao.getScheduleByClazz(clazz);
    }

    @Override
    public void addSchedule(final Schedule schedule) throws ClassNotFoundException, SchedulerException {
        super.save(schedule);
        final Class clazz = Class.forName(schedule.getClazz());
        final JobDetail job = JobBuilder.newJob(clazz).withIdentity(JobKey.jobKey(schedule.getClazz())).build();
        final Trigger trigger;
        if (schedule.getType() == 1) {
            trigger = TriggerBuilder.newTrigger().withSchedule((ScheduleBuilder) SimpleScheduleBuilder.simpleSchedule().withRepeatCount(schedule.getTimes() - 1).withIntervalInSeconds((int) schedule.getIntervals())).startAt(schedule.getStartTime()).build();
        } else {
            trigger = TriggerBuilder.newTrigger().withSchedule((ScheduleBuilder) CronScheduleBuilder.cronSchedule(schedule.getCron())).build();
        }
        schedulerFactoryBean.getScheduler().scheduleJob(job, trigger);
        schedulerFactoryBean.getScheduler().start();
    }

    @Override
    public void deleteSchedule(final Schedule schedule, final boolean deleteFromDB) throws SchedulerException {
        if (deleteFromDB) {
            scheduleDao.delete(schedule);
        } else {
            deleteSchedule(schedule);
            schedule.setStatus(3);
            schedule.setExecuteTimes(0);
            scheduleDao.update(schedule);
        }
    }

    @Override
    public void deleteSchedule(final Schedule schedule) throws SchedulerException {
        schedulerFactoryBean.getScheduler().deleteJob(JobKey.jobKey(schedule.getClazz()));
    }

    @Override
    public void startSchedule(final Schedule schedule) throws SchedulerException, ClassNotFoundException {
        final int status = schedule.getStatus();
        schedule.setStatus(0);
        scheduleDao.update(schedule);
        if (status == 3 || status == 1) {
            final Class clazz = Class.forName(schedule.getClazz());
            final JobDetail job = JobBuilder.newJob(clazz)
                    .withIdentity(JobKey.jobKey(schedule.getClazz()))
                    .build();
            final Trigger trigger;
            if (schedule.getType() == 1) {
                trigger = TriggerBuilder.newTrigger()
                        .withSchedule((ScheduleBuilder) SimpleScheduleBuilder.simpleSchedule()
                                .withRepeatCount(schedule.getTimes() - 1)
                                .withIntervalInSeconds(schedule.getIntervals()))
                        .startAt(schedule.getStartTime()).build();
            } else {
                trigger = TriggerBuilder.newTrigger()
                        .withSchedule((ScheduleBuilder) CronScheduleBuilder.cronSchedule(schedule.getCron()))
                        .build();
            }
            schedulerFactoryBean.getScheduler().scheduleJob(job, trigger);
            schedulerFactoryBean.getScheduler().start();
        } else if (status == 2) {
            schedulerFactoryBean.getScheduler().resumeJob(JobKey.jobKey(schedule.getClazz()));
        }
    }

    @Override
    public void pauseSchedule(final Schedule schedule) throws SchedulerException {
        schedulerFactoryBean.getScheduler().pauseJob(JobKey.jobKey(schedule.getClazz()));
        schedule.setStatus(2);
        scheduleDao.update(schedule);
    }

    @Override
    public <T> Map<String, Object> findPageInfo(final Filter filter, final Page page) throws Exception {
        return scheduleDao.findPageInfo(filter, page);
    }

    @Override
    public List<Schedule> findUncompleteSchedule() {
        return scheduleDao.findUncompleteSchedule();
    }
}
