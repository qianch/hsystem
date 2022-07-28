/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.planner.produce.service;

import com.bluebirdme.mes.core.base.service.IBaseService;
import com.bluebirdme.mes.planner.produce.entity.PackageTask;

import java.util.List;
import java.util.Map;

/**
 * @author 高飞
 * @Date 2017-5-26 10:04:05
 */
public interface IPackageTaskService extends IBaseService {

    List<Map<String, Object>> findTasks(Long planDetailId);

    List<Map<String, Object>> findPakcageInfo(Long bcBomId);

    void saveTask(List<PackageTask> list);

    void deleteTask(PackageTask task);

}
