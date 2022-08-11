/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.siemens.order.service.impl;

import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.siemens.order.dao.ICutTaskOrderPrintTaskDao;
import com.bluebirdme.mes.siemens.order.service.ICutTaskOrderPrintTaskService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Map;

/**
 * @author 高飞
 * @Date 2019-4-15 18:44:42
 */
@Service
@Transactional
public class CutTaskOrderPrintTaskServiceImpl extends BaseServiceImpl implements ICutTaskOrderPrintTaskService {
    @Resource
    ICutTaskOrderPrintTaskDao cutTaskOrderPrintTaskDao;

    @Override
    protected IBaseDao getBaseDao() {
        return cutTaskOrderPrintTaskDao;
    }

    @Override
    public Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
        return cutTaskOrderPrintTaskDao.findPageInfo(filter, page);
    }
}
