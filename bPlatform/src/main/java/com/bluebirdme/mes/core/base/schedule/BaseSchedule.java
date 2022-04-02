package com.bluebirdme.mes.core.base.schedule;

import com.bluebirdme.mes.core.schedule.Task;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author qianchen
 * @date 2020/05/21
 */
public abstract class BaseSchedule implements Job {
    private static final Logger logger = LoggerFactory.getLogger(BaseSchedule.class);

    public BaseSchedule() {
    }

    @Override
    public void execute(JobExecutionContext ctx) {
        Long start = System.currentTimeMillis();
        String executeResult = this.doJob(ctx) ? "成功" : "失败";
        Long end = System.currentTimeMillis();
        Class<? extends Object> clazz = this.getClass();
        Task task = clazz.getAnnotation(Task.class);
        logger.debug("[" + task.value() + "::" + clazz.getName() + "]执行" + executeResult + ",耗时：" + (end - start) + "ms");
    }

    public abstract boolean doJob(JobExecutionContext var1);
}
