/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2018版权所有
 */
package com.bluebirdme.mes.task.k3;

import com.bluebirdme.mes.core.schedule.Task;
import org.quartz.JobExecutionContext;

/**
 * @author Goofy
 * @Date 2018年4月2日 下午1:34:42
 */
@Task("成品入库同步")
public class ProductInSync extends Url {
    @Override
    public boolean doJob(JobExecutionContext ctx) {
        return doSync(P_IN);
    }
}
