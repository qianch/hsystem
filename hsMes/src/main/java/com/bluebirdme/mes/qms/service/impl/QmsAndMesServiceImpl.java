package com.bluebirdme.mes.qms.service.impl;

import com.bluebirdme.mes.core.annotation.AnyExceptionRollback;
import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.qms.dao.QmsAndMesDao;
import com.bluebirdme.mes.qms.service.QmsAndMesService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
@AnyExceptionRollback
public class QmsAndMesServiceImpl extends BaseServiceImpl implements QmsAndMesService {
    @Resource
    QmsAndMesDao qmsAndMesDao;

    @Override
    protected IBaseDao getBaseDao() {
        return qmsAndMesDao;
    }

    @Override
    public List<Map<String, Object>> findRollandPartByBatchCode(String batchCode, String deliveryCode) throws Exception {
        return qmsAndMesDao.findRollandPartByBatchCode(batchCode, deliveryCode);
    }

    @Override
    public List<Map<String, Object>> selectBomByCode(String salesOrderId, String procBomCode) throws Exception {
        return qmsAndMesDao.selectBomByCode(salesOrderId, procBomCode);
    }

    @Override
    public List<Map<String, Object>> selectOUTOrder(String deliveryCode, String batchCode) throws Exception {
        List<Map<String, Object>> list = qmsAndMesDao.selectOUTOrderOfMirror(deliveryCode, batchCode);
        if (list.size() == 0) {
            list = qmsAndMesDao.selectOUTOrder(deliveryCode, batchCode);
        }
        return list;
    }

    @Override
    public List<Map<String, Object>> selectOUTOrderByByDeliveryPlan(String deliveryCode, String batchCode) throws Exception {
        List<Map<String, Object>> list = qmsAndMesDao.selectOUTOrderByDeliveryPlanOfMirror(deliveryCode, batchCode);
        if (list.size() == 0) {
            list = qmsAndMesDao.selectOUTOrderByDeliveryPlan(deliveryCode, batchCode);
        }
        return list;
    }
}
