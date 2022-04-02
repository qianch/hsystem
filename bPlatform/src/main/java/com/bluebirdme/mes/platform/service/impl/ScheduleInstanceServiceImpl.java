package com.bluebirdme.mes.platform.service.impl;

import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.core.schedule.QuartzSchedule;
import com.bluebirdme.mes.platform.dao.IScheduleInstanceDao;
import com.bluebirdme.mes.platform.entity.ScheduleInstance;
import com.bluebirdme.mes.platform.entity.ScheduleTemplate;
import com.bluebirdme.mes.platform.service.IScheduleInstanceService;
import org.quartz.*;
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
public class ScheduleInstanceServiceImpl extends BaseServiceImpl implements IScheduleInstanceService {
    @Resource
    IScheduleInstanceDao scheduleInstanceDao;
    @Resource
    QuartzSchedule schedule;

    @Override
    protected IBaseDao getBaseDao() {
        return this.scheduleInstanceDao;
    }

    @Override
    public <T> Map<String, Object> findPageInfo(final Filter filter, final Page page) throws Exception {
        return scheduleInstanceDao.findPageInfo(filter, page);
    }

    @Override
    public void start(ScheduleInstance instance) throws SchedulerException, ClassNotFoundException {
        instance = this.findById(ScheduleInstance.class, instance.getId());
        instance.setStatus("RUN");
        update(instance);
        final ScheduleTemplate tpl = this.findById(ScheduleTemplate.class, instance.getTemplateId());
        final Class clazz = Class.forName(tpl.getClazz());
        final JobDetail job = JobBuilder.newJob(clazz).withIdentity(JobKey.jobKey(instance.getId() + "")).build();
        final Trigger trigger = TriggerBuilder.newTrigger().withSchedule((ScheduleBuilder) CronScheduleBuilder.cronSchedule(instance.getCron())).build();
        schedule.getScheduler().scheduleJob(job, trigger);
        schedule.getScheduler().start();
    }

    @Override
    public void stop(ScheduleInstance instance) throws SchedulerException {
        instance = this.findById(ScheduleInstance.class, instance.getId());
        instance.setStatus("STOP");
        update(instance);
        schedule.getScheduler().deleteJob(JobKey.jobKey(instance.getId() + ""));
    }
}
