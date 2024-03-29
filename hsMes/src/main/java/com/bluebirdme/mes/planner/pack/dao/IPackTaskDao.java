/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2017版权所有
 */
package com.bluebirdme.mes.planner.pack.dao;

import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.planner.pack.entity.PackTask;

import java.util.List;

/**
 * @author Goofy
 * @Date 2017年12月7日 上午10:25:01
 */
public interface IPackTaskDao extends IBaseDao {
    List<PackTask> findProductTask(Long productId);

    List<PackTask> findProduceTask(Long ppdId);

    void updateLeftCount(List<PackTask> list);

}
