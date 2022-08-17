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
import com.bluebirdme.mes.stock.dao.IProductDeliveryRecordDao;
import com.bluebirdme.mes.stock.service.IProductDeliveryRecordService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author 宋黎明
 * @Date 2016-11-27 13:57:45
 */
@Service
@AnyExceptionRollback
public class ProductDeliveryRecordServiceImpl extends BaseServiceImpl implements IProductDeliveryRecordService {
    @Resource
    IProductDeliveryRecordDao productDeliveryRecordDao;

    @Override
    protected IBaseDao getBaseDao() {
        return productDeliveryRecordDao;
    }

    @Override
    public Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
        return productDeliveryRecordDao.findPageInfo(filter, page);
    }

    @Override
    public List<Map<String, Object>> findOutRecordByDeliveryCode(String packingNumber) {
        return productDeliveryRecordDao.findOutRecordByDeliveryCode(packingNumber);
    }
}
