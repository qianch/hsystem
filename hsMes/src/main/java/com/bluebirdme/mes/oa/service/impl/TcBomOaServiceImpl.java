package com.bluebirdme.mes.oa.service.impl;

import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.oa.dao.ITcBomOaDao;
import com.bluebirdme.mes.oa.service.ITcBomOaService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Service
public class TcBomOaServiceImpl extends BaseServiceImpl implements ITcBomOaService {
    @Resource
    public ITcBomOaDao tcBomOaDao;

    @Override
    protected IBaseDao getBaseDao() {
        return tcBomOaDao;
    }

    @Override
    public Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
        return tcBomOaDao.findPageInfo(filter, page);
    }
}
