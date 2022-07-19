/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.device.service.impl;

import com.bluebirdme.mes.core.annotation.AnyExceptionRollback;
import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.device.dao.IWeightCarrierDao;
import com.bluebirdme.mes.device.entity.WeightCarrier;
import com.bluebirdme.mes.device.service.IWeightCarrierService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author 孙利
 * @Date 2017-7-10 8:44:34
 */
@Service
@AnyExceptionRollback
public class WeightCarrierServiceImpl extends BaseServiceImpl implements IWeightCarrierService {
    @Resource
    IWeightCarrierDao weightCarrierDao;

    @Override
    protected IBaseDao getBaseDao() {
        return weightCarrierDao;
    }

    @Override
    public <T> Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
        return weightCarrierDao.findPageInfo(filter, page);
    }

    @Override
    public List<String> getWeightCodes() {
        return weightCarrierDao.getWeightCodes();
    }

    @Override
    public WeightCarrier findCarrierById(int id) {
        return weightCarrierDao.getCarrierById(id);
    }

    @Override
    public WeightCarrier findByCode(String carrierCode) {
        return weightCarrierDao.findByCode(carrierCode);
    }
}
