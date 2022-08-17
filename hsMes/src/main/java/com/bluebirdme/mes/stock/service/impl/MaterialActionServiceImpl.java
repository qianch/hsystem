package com.bluebirdme.mes.stock.service.impl;

import com.bluebirdme.mes.core.annotation.AnyExceptionRollback;
import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.stock.dao.IMaterialActionDao;
import com.bluebirdme.mes.stock.service.IMaterialActionService;
import com.bluebirdme.mes.stock.service.IMaterialStockService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Service
@AnyExceptionRollback
public class MaterialActionServiceImpl extends BaseServiceImpl implements IMaterialActionService {
    @Resource
    IMaterialActionDao materialActionDao;
    @Resource
    IMaterialStockService mssService;

    @Override
    protected IBaseDao getBaseDao() {
        return materialActionDao;
    }

    @Override
    public Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
        return materialActionDao.findPageInfo(filter, page);
    }

    public Map<String, Object> findPageOutInfo(Filter filter, Page page) throws Exception {
        return materialActionDao.findPageOutInfo(filter, page);
    }

    @Override
    public Map<String, Object> findPageDetailInfo(Filter filter, Page page)
            throws Exception {
        return materialActionDao.findPageDetailInfo(filter, page);
    }

    /**
     * 根据ID 冻结\解冻|放行\取消放行
     *
     * @param mssId
     * @param action
     * @param actionValue
     * @throws Exception
     */
    public void action(Long mssId[], String action, String actionValue) throws Exception {
        String codes = mssService.validMaterialStockState(mssId);
        if (!StringUtils.isEmpty(codes)) {
            throw new Exception(codes + "不在库,无法操作");
        }
        materialActionDao.action(mssId, action, actionValue);
    }

    @Override
    public Map<String, Object> findPageForceOutInfo(Filter filter, Page page) throws Exception {
        return materialActionDao.findPageForceOutInfo(filter, page);
    }
}
