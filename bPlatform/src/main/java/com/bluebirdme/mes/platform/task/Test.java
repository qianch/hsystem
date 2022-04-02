package com.bluebirdme.mes.platform.task;

import com.bluebirdme.mes.core.base.schedule.BaseSchedule;
import com.bluebirdme.mes.core.schedule.Task;
import com.bluebirdme.mes.core.spring.SpringCtx;
import com.bluebirdme.mes.platform.entity.User;
import com.bluebirdme.mes.platform.service.IUserService;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author qianchen
 * @date 2020/05/21
 */

@Task("测试任务调度")
public class Test extends BaseSchedule {
    private static Logger log = LoggerFactory.getLogger(Test.class);

    @Override
    public boolean doJob(final JobExecutionContext ctx) {
        try {
            log.debug("KEY:" + ctx.getJobDetail().getKey());
            final IUserService service = (IUserService) SpringCtx.getBean("userServiceImpl");
            final List<User> list = service.findAll(User.class);
            for (final User user : list) {
                log.debug(user.getLoginName());
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
