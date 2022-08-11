/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.siemens.bom.service.impl;

import com.bluebirdme.mes.core.annotation.AnyExceptionRollback;
import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.siemens.bom.dao.ISuitDao;
import com.bluebirdme.mes.siemens.bom.entity.Grid;
import com.bluebirdme.mes.siemens.bom.entity.Suit;
import com.bluebirdme.mes.siemens.bom.service.ISuitService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author 高飞
 * @Date 2017-7-20 13:16:40
 */
@Service
@AnyExceptionRollback
public class SuitServiceImpl extends BaseServiceImpl implements ISuitService {
    @Resource
    ISuitDao suitDao;

    @Override
    protected IBaseDao getBaseDao() {
        return suitDao;
    }

    @Override
    public Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
        return suitDao.findPageInfo(filter, page);
    }

    public List<Map<String, Object>> suitList(Long partId) {
        return suitDao.suitList(partId);
    }

    public void saveSuitGird(Grid<Suit> grid) {
        saveList(grid.getInserted());
        for (Suit suit : grid.getDeleted()) {
            delete(suit);
        }

        for (Suit suit : grid.getUpdated()) {
            update(suit);
        }
    }
}
