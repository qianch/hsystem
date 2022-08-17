/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.stock.service.impl;

import com.bluebirdme.mes.core.annotation.AnyExceptionRollback;
import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.stock.dao.IProductOutRecordDao;
import com.bluebirdme.mes.stock.service.IProductOutRecordService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author 宋黎明
 * @Date 2016-11-16 13:44:47
 */
@Service
@AnyExceptionRollback
public class ProductOutRecordServiceImpl extends BaseServiceImpl implements IProductOutRecordService {
    @Resource
    IProductOutRecordDao productOutRecordDao;

    @Override
    protected IBaseDao getBaseDao() {
        return productOutRecordDao;
    }

    @Override
    public Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
        return productOutRecordDao.findPageInfo(filter, page);
    }
}
