package com.bluebirdme.mes.task.annotation;

import com.bluebirdme.mes.audit.entity.AuditConstant;
import com.bluebirdme.mes.core.properties.SystemProperties;
import com.bluebirdme.mes.planner.cut.entity.CutPlan;
import com.bluebirdme.mes.planner.produce.entity.ProducePlan;
import com.bluebirdme.mes.planner.produce.service.IProducePlanService;
import com.bluebirdme.mes.planner.weave.entity.WeavePlan;
import org.quartz.DisallowConcurrentExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 将审核过的生产计划转化为编织和裁剪计划
 *
 * @author Goofy
 * @Date 2016年11月16日 上午10:12:32
 */
@Component
@DisallowConcurrentExecution
public class ProducePlanCreator {
    private static final Logger logger = LoggerFactory.getLogger(ProducePlanCreator.class);
    @Resource
    IProducePlanService planService;

    @Scheduled(fixedRate = 300000)
    public synchronized void creator() throws IOException {
        SystemProperties properties = new SystemProperties();
        if (!properties.getAsBoolean("Task")) {
            logger.info("编织和裁剪计划为关闭状态，如要开启请配置system.properties中Task=true");
            return;
        }

        Map<String, Object> param = new HashMap<>();
        param.put("auditState", AuditConstant.RS.PASS);
        param.put("hasCreatedCutAndWeavePlan", 0);
        List<ProducePlan> plans = planService.findListByMap(ProducePlan.class, param);
        param.put("hasCreatedCutAndWeavePlan", null);
        plans.addAll(planService.findListByMap(ProducePlan.class, param));
        for (ProducePlan pp : plans) {
            try {
                logger.info("正在生成任务单" + pp.getProducePlanCode() + "创建编织和裁剪计划");
                if (null != pp.getCreateFeedback()) {
                    logger.info("单号:" + pp.getProducePlanCode() + ":{" + pp.getCreateFeedback() + "};");
                    continue;
                }
                param.clear();
                param.put("planCode", pp.getProducePlanCode());
                // 检查是否有创建过，如果创建过，那么则不再创建新的计划
                if (planService.isExist(WeavePlan.class, param, true)) {
                    logger.info("生产任务单已创建过编织计划，单号:" + pp.getProducePlanCode());
                    pp.setHasCreatedCutAndWeavePlan(3);
                    planService.update(pp);
                    continue;
                }

                if (planService.isExist(CutPlan.class, param, true)) {
                    logger.info("生产任务单已创建过裁剪计划，单号:" + pp.getProducePlanCode());
                    pp.setHasCreatedCutAndWeavePlan(4);
                    planService.update(pp);
                    continue;
                }
                planService.createCutAndWeavePlans(pp);
                logger.info("任务单编织和裁剪计划创建完毕");
            } catch (Exception e) {
                pp.setHasCreatedCutAndWeavePlan(2);
                planService.update(pp);
                logger.error(e.getLocalizedMessage(), e);
            }
        }
    }
}
