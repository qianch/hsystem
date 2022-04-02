/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.produce.service.impl;

import com.bluebirdme.mes.btwManager.entity.BtwFilePrint;
import com.bluebirdme.mes.core.annotation.AnyExceptionRollback;
import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.produce.dao.IFinishedProductPrintRecordDao;
import com.bluebirdme.mes.produce.entity.FinishedProduct;
import com.bluebirdme.mes.produce.entity.FinishedProductPrintRecord;
import com.bluebirdme.mes.produce.service.IFinishedProductPrintRecordService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author 徐波
 * @Date 2016-11-26 23:01:35
 */
@Service
@AnyExceptionRollback
public class FinishedProductPrintRecordServiceImpl extends BaseServiceImpl implements IFinishedProductPrintRecordService {

    @Resource
    IFinishedProductPrintRecordDao finishedProductPrintRecordDao;

    @Override
    protected IBaseDao getBaseDao() {
        return finishedProductPrintRecordDao;
    }

    @Override
    public <T> Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
        return null;
    }

    public List<Map<String, Object>> findFinishedProductPrintRecords(Long productId) throws Exception {

        return finishedProductPrintRecordDao.findFinishedProductPrintRecords(productId);
    }

    public boolean saveFinishedProductPrintRecords(FinishedProduct finishedProduct, String userId) {

        List<FinishedProductPrintRecord> listFinishedProductPrintRecordSave = new ArrayList<FinishedProductPrintRecord>();
        for (FinishedProductPrintRecord finishedProductPrintRecord : finishedProduct.getListFinishedProductPrintRecord()) {
            finishedProductPrintRecord.setProductId(finishedProduct.getId());
            if (finishedProductPrintRecord.getId() == null) {
                finishedProductPrintRecord.setCreateTime(new Date());
                finishedProductPrintRecord.setCreater(userId);
            }
            finishedProductPrintRecord.setModifyTime(new Date());
            finishedProductPrintRecord.setModifyUser(userId);
            listFinishedProductPrintRecordSave.add(finishedProductPrintRecord);
        }

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("productId", finishedProduct.getId());
        delete(FinishedProductPrintRecord.class, map);

        finishedProductPrintRecordDao.save(listFinishedProductPrintRecordSave.toArray(new FinishedProductPrintRecord[]{}));

        return true;
    }
}
