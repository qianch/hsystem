package com.bluebirdme.mes.core.spring;

import com.bluebirdme.mes.platform.entity.ScheduleInstance;
import com.bluebirdme.mes.platform.service.IScheduleInstanceService;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Iterator;
import java.util.List;

/**
 * @author qianchen
 * @date 2020/05/21
 */
@Component
public class ScheduleInit implements ApplicationListener<ContextRefreshedEvent> {
    private static Logger log = LoggerFactory.getLogger(ScheduleInit.class);
    @Resource
    IScheduleInstanceService instService;

    public ScheduleInit() {
    }

    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() == null) {
            try {
                this.initSchedule();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void initSchedule() throws ClassNotFoundException, SchedulerException {
        log.debug("初始化调度任务");
        List<ScheduleInstance> list = this.instService.findAll(ScheduleInstance.class);
        Iterator iterator = list.iterator();

        while (iterator.hasNext()) {
            ScheduleInstance inst = (ScheduleInstance) iterator.next();
            if (!"STOP".equals(inst.getStatus())) {
                this.instService.start(inst);
            }
        }
    }
}
