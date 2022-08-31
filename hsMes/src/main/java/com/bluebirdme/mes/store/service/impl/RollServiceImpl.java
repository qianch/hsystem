/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.store.service.impl;

import com.bluebirdme.mes.core.annotation.AnyExceptionRollback;
import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.store.dao.IRollDao;
import com.bluebirdme.mes.store.service.IRollService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author 徐波
 * @Date 2016-11-9 15:32:13
 */
@Service
@AnyExceptionRollback
public class RollServiceImpl extends BaseServiceImpl implements IRollService {
    @Resource
    IRollDao rollDao;

    @Override
    protected IBaseDao getBaseDao() {
        return rollDao;
    }

    @Override
    public Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
        return rollDao.findPageInfo(filter, page);
    }
}
