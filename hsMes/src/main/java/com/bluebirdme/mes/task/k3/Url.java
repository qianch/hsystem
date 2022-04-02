/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2018版权所有
 */
package com.bluebirdme.mes.task.k3;

import com.bluebirdme.mes.core.base.schedule.BaseSchedule;
import com.bluebirdme.mes.core.properties.SystemProperties;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Goofy
 * @Date 2018年4月2日 下午1:36:56
 */
public abstract class Url extends BaseSchedule {
    private static Logger log = LoggerFactory.getLogger(Url.class);
    /**
     * 销售出库
     */
    final String P_OUT = "pOut";
    /**
     * 成品入库
     */
    final String P_IN = "pIn";
    /**
     * 原料入库
     */
    final String M_IN = "mIn";
    /**
     * 原料出库
     */
    final String M_OUT = "mOut";

    public boolean doSync(String action) {
        try {
            SystemProperties system = new SystemProperties();
            String url = system.getAsString("k3.sync." + action) + "/MES";
            log.info(Jsoup.connect(url).timeout(120000).get().toString());
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
            return false;
        }
        return true;
    }
}
