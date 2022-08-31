/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2018版权所有
 */
package com.bluebirdme.mes.task.annotation;

import com.bluebirdme.mes.utils.SystemInfo;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * @author Goofy
 * @Date 2018年4月10日 上午10:03:17
 */
//@Component
public class ServerMonitor {
    @Scheduled(fixedRate = 60000)
    public synchronized void info() throws Exception {
        SystemInfo info = SystemInfo.getInstance();
        info.log("D:\\");
    }
}
