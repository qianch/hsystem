/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2018版权所有
 */
package com.bluebirdme.mes.task.k3;

import org.quartz.JobExecutionContext;

import com.bluebirdme.mes.core.schedule.Task;

/**
 * @author Goofy
 * @Date 2018年4月2日 下午1:34:42
 */
@Task("销售出库单同步")
public class ProductOutSync extends Url {
	
	@Override
	public boolean doJob(JobExecutionContext ctx) {
		return doSync(P_OUT);
	}

}
