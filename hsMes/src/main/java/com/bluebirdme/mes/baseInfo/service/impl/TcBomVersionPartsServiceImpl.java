/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.baseInfo.service.impl;

import com.bluebirdme.mes.baseInfo.dao.ITcBomVersionPartsDao;
import com.bluebirdme.mes.baseInfo.service.ITcBomVersionPartsService;
import com.bluebirdme.mes.core.annotation.AnyExceptionRollback;
import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author 肖文彬
 * @Date 2016-10-9 16:11:41
 */
@Service
@AnyExceptionRollback
public class TcBomVersionPartsServiceImpl extends BaseServiceImpl implements ITcBomVersionPartsService {

    @Resource
    ITcBomVersionPartsDao tcBomVersionPartsDao;

    @Override
    protected IBaseDao getBaseDao() {
        return tcBomVersionPartsDao;
    }

    @Override
    public Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
        return tcBomVersionPartsDao.findPageInfo(filter, page);
    }

    @Override
    public Map<String, Object> findPageInfo1(Filter filter, Page page) {
        return tcBomVersionPartsDao.findPageInfo1(filter, page);
    }
}
