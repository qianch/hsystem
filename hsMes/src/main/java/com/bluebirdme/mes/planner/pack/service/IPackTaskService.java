/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2017版权所有
 */
package com.bluebirdme.mes.planner.pack.service;

import com.bluebirdme.mes.core.base.service.IBaseService;
import com.bluebirdme.mes.planner.pack.entity.PackTask;

import java.util.List;

/**
 * @author Goofy
 * @Date 2017年12月7日 下午2:40:33
 */
public interface IPackTaskService extends IBaseService {
    void saveOrUpdate(List<PackTask> list);

    List<PackTask> findProductTask(Long productId);

    List<PackTask> findProduceTask(Long ppdId);

    void updateLeftCount(List<PackTask> list);
}
