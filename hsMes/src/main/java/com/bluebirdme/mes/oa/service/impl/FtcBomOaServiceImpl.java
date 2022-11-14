package com.bluebirdme.mes.oa.service.impl;

import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.oa.dao.IFtcBomOaDao;
import com.bluebirdme.mes.oa.service.IFtcBomOaService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Service
public class FtcBomOaServiceImpl extends BaseServiceImpl implements IFtcBomOaService {
    @Resource
    public IFtcBomOaDao ftcBomOaDao;

    @Override
    protected IBaseDao getBaseDao() {
        return ftcBomOaDao;
    }

    @Override
    public Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
        return ftcBomOaDao.findPageInfo(filter, page);
    }
}
