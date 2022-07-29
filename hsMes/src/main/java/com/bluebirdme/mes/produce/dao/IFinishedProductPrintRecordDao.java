/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.produce.dao;

import com.bluebirdme.mes.core.base.dao.IBaseDao;

import java.util.List;
import java.util.Map;

/**
 * @author 徐波
 * @Date 2016-11-26 23:01:35
 */

public interface IFinishedProductPrintRecordDao extends IBaseDao {
    List<Map<String, Object>> findFinishedProductPrintRecords(Long productId) throws Exception;
}
