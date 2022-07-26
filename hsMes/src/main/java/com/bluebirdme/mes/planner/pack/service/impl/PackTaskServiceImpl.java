/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2017版权所有
 */
package com.bluebirdme.mes.planner.pack.service.impl;

import com.bluebirdme.mes.core.annotation.AnyExceptionRollback;
import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.planner.pack.dao.IPackTaskDao;
import com.bluebirdme.mes.planner.pack.entity.PackTask;
import com.bluebirdme.mes.planner.pack.service.IPackTaskService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author Goofy
 * @Date 2017年12月7日 下午2:42:07
 */
@Service
@AnyExceptionRollback
public class PackTaskServiceImpl extends BaseServiceImpl implements IPackTaskService {
    @Resource
    IPackTaskDao ptDao;

    @Override
    protected IBaseDao getBaseDao() {
        return ptDao;
    }

    @Override
    public <T> Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
        return null;
    }

    @Override
    public void saveOrUpdate(List<PackTask> list) {
        Map<String, Object> condition = new HashMap<String, Object>();
        //先删除相应的任务
        if (list.get(0).getPpdId() != null) {
            condition.put("ppdId", list.get(0).getPpdId());
        } else {
            condition.put("sodId", list.get(0).getSodId());
        }
        //先删除相应的任务
        this.delete(PackTask.class, condition);
        saveList(list);
        if (list.get(0).getPpdId() != null) {
            ptDao.updateLeftCount(list);
        }
    }

    public List<PackTask> findProductTask(Long productId) {
        return ptDao.findProductTask(productId);
    }

    public List<PackTask> findProduceTask(Long ppdId) {
        return ptDao.findProduceTask(ppdId);
    }

    public void updateLeftCount(List<PackTask> list) {
        ptDao.updateLeftCount(list);
    }
}
