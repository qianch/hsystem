/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.printer.dao;

import com.bluebirdme.mes.core.base.dao.IBaseDao;

import java.util.List;
import java.util.Map;

/**
 * @author 徐波
 * @Date 2016-11-14 15:40:51
 */

public interface IPrinterDao extends IBaseDao {
    List<Map<String, Object>> getDaiyRollCount(String barc);

    List<Map<String, Object>> getDaiyTrayCount(String barc);

    public List<Map<String, Object>> getDaiyBoxCount(String barc);

    List<Map<String, Object>> getDaiyPartCount(String barc);

    void insert(Object... object) throws Exception;
}
