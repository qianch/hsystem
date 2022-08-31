/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.store.service.impl;

import com.bluebirdme.mes.btwManager.service.IBtwFileService;
import com.bluebirdme.mes.core.annotation.AnyExceptionRollback;
import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.store.dao.IRollBarcodeDao;
import com.bluebirdme.mes.store.entity.RollBarcode;
import com.bluebirdme.mes.store.service.IRollBarcodeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author 徐波
 * @Date 2016-11-14 14:37:30
 */
@Service
@AnyExceptionRollback
public class RollBarcodeServiceImpl extends BaseServiceImpl implements IRollBarcodeService {
    @Resource
    IRollBarcodeDao rollBarcodeDao;

    @Resource
    IBtwFileService btwFileService;

    @Override
    protected IBaseDao getBaseDao() {
        return rollBarcodeDao;
    }

    @Override
    public Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
        return rollBarcodeDao.findPageInfo(filter, page);
    }


    public String clearRoll(String ids) throws Exception {
        String[] ids_temp = ids.split(",");
        for (String s : ids_temp) {
            RollBarcode rollBarcode = findById(RollBarcode.class, Long.parseLong(s));
            btwFileService.clearBacode(rollBarcode);
        }
        return "";
    }

    public List<Map<String, Object>> findMaxRollBarCodeCount() {
        return rollBarcodeDao.findMaxRollBarCodeCount();
    }
}
