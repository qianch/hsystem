package com.bluebirdme.mes.oa.service.impl;

import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.oa.dao.IFtcProcBomOaDao;
import com.bluebirdme.mes.oa.service.IFtcProcBomOaService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Service
public class FtcProcBomOaServiceImpl extends BaseServiceImpl implements IFtcProcBomOaService {
    @Resource
    public IFtcProcBomOaDao iFtcProcBomOaDao;

    @Override
    protected IBaseDao getBaseDao() {
        return iFtcProcBomOaDao;
    }

    @Override
    public <T> Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
        return iFtcProcBomOaDao.findPageInfo(filter, page);
    }
}
