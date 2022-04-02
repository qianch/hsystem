package com.bluebirdme.mes.core.schedule;

import org.quartz.Scheduler;

/**
 * @author qianchen
 * @date 2020/05/21
 */
public class QuartzSchedule {
    private Scheduler scheduler;

    public QuartzSchedule() {
    }

    public Scheduler getScheduler() {
        return this.scheduler;
    }

    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }
}
