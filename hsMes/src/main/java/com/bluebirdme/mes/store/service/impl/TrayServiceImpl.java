package com.bluebirdme.mes.store.service.impl;

import com.bluebirdme.mes.core.annotation.AnyExceptionRollback;
import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.store.dao.ITrayDao;
import com.bluebirdme.mes.store.service.ITrayService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
@AnyExceptionRollback
public class TrayServiceImpl extends BaseServiceImpl implements ITrayService {
    @Resource
    ITrayDao trayDao;

    @Override
    protected IBaseDao getBaseDao() {
        return trayDao;
    }

    @Override
    public Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
        return trayDao.findPageInfo(filter, page);
    }

    @Override
    public List<Map<String, Object>> findRollBox(String code) {
        return trayDao.findBoxRoll(code);
    }
}
