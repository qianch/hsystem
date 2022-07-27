/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.planner.produce.dao;

import com.bluebirdme.mes.core.base.dao.IBaseDao;

import java.util.List;
import java.util.Map;

/**
 * @author 高飞
 * @Date 2017-5-26 10:04:05
 */

public interface IPackageTaskDao extends IBaseDao {
    List<Map<String, Object>> findTasks(Long producePlanDetailId);

    List<Map<String, Object>> findPakcageInfo(Long bcBomId);
}
