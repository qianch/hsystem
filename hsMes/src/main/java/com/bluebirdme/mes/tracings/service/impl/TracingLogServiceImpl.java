/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.tracings.service.impl;

import com.bluebirdme.mes.core.annotation.AnyExceptionRollback;
import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.core.sql.SQL;
import com.bluebirdme.mes.tracings.dao.ITracingLogDao;
import com.bluebirdme.mes.tracings.service.ITracingLogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author 徐波
 * @Date 2016-11-30 14:03:19
 */
@Service
@AnyExceptionRollback
public class TracingLogServiceImpl extends BaseServiceImpl implements ITracingLogService {
    @Resource
    ITracingLogDao tracingLogDao;

    @Override
    protected IBaseDao getBaseDao() {
        return tracingLogDao;
    }

    @Override
    public Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
        return tracingLogDao.findPageInfo(filter, page);
    }

    @Override
    public List<Map<String, Object>> findByCondition(Map<String, Object> map) {
        return tracingLogDao.findListMapByMap(SQL.get("tracingLog-list-sales"), map);
    }

    @Override
    public List<Map<String, Object>> findByTraceBackTo(String rollbarcode) {
        return tracingLogDao.findByTraceBackTo(rollbarcode);
    }
}
