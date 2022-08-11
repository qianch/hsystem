/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.siemens.bom.service.impl;

import com.bluebirdme.mes.core.annotation.AnyExceptionRollback;
import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.siemens.bom.dao.ICutGroupDao;
import com.bluebirdme.mes.siemens.bom.service.ICutGroupService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author 高飞
 * @Date 2017-7-25 10:39:09
 */
@Service
@AnyExceptionRollback
public class CutGroupServiceImpl extends BaseServiceImpl implements ICutGroupService {
    @Resource
    ICutGroupDao cutGroupDao;

    @Override
    protected IBaseDao getBaseDao() {
        return cutGroupDao;
    }

    @Override
    public Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
        return cutGroupDao.findPageInfo(filter, page);
    }

    public List<Map<String, Object>> findCutWorkshopUsers() {
        return cutGroupDao.findCutWorkshopUsers();
    }
}
