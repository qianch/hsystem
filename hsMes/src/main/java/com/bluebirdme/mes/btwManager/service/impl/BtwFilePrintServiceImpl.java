/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.btwManager.service.impl;

import com.bluebirdme.mes.btwManager.dao.IBtwFilePrintDao;
import com.bluebirdme.mes.btwManager.entity.BtwFile;
import com.bluebirdme.mes.btwManager.entity.BtwFilePrint;
import com.bluebirdme.mes.btwManager.service.IBtwFilePrintService;
import com.bluebirdme.mes.core.annotation.AnyExceptionRollback;
import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author 徐波
 * @Date 2016-11-26 23:01:35
 */
@Service
@AnyExceptionRollback
public class BtwFilePrintServiceImpl extends BaseServiceImpl implements IBtwFilePrintService {
    @Resource
    IBtwFilePrintDao BtwFilePrintDao;

    @Override
    protected IBaseDao getBaseDao() {
        return BtwFilePrintDao;
    }

    @Override
    public <T> Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
        return null;
    }

    public List<Map<String, Object>> findBtwFilePrints(Long btwFileId) throws Exception {
        return BtwFilePrintDao.findBtwFilePrints(btwFileId);
    }

    public boolean saveBtwFilePrints(BtwFile btwFile, String userId) {
        List<BtwFilePrint> listBtwFilePrintSave = new ArrayList<BtwFilePrint>();
        for (BtwFilePrint btwFilePrint : btwFile.getListBtwFilePrint()) {
            btwFilePrint.setBtwFileId(btwFile.getId());
            if (btwFilePrint.getId() == null) {
                btwFilePrint.setCreateTime(new Date());
                btwFilePrint.setCreater(userId);
            }
            btwFilePrint.setModifyTime(new Date());
            btwFilePrint.setModifyUser(userId);
            listBtwFilePrintSave.add(btwFilePrint);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("btwFileId", btwFile.getId());
        delete(BtwFilePrint.class, map);
        BtwFilePrintDao.save(listBtwFilePrintSave.toArray(new BtwFilePrint[]{}));
        return true;
    }
}
