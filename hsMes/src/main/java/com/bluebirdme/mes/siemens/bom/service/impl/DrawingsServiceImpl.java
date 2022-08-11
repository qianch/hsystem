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
import com.bluebirdme.mes.siemens.bom.dao.IDrawingsDao;
import com.bluebirdme.mes.siemens.bom.entity.Drawings;
import com.bluebirdme.mes.siemens.bom.entity.Grid;
import com.bluebirdme.mes.siemens.bom.service.IDrawingsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author 高飞
 * @Date 2017-7-18 14:06:57
 */
@Service
@AnyExceptionRollback
public class DrawingsServiceImpl extends BaseServiceImpl implements IDrawingsService {
    @Resource
    IDrawingsDao drawingsDao;

    @Override
    protected IBaseDao getBaseDao() {
        return drawingsDao;
    }

    @Override
    public Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
        return drawingsDao.findPageInfo(filter, page);
    }

    public List<Map<String, Object>> drawingsList(Long partId) {
        return drawingsDao.drawingsList(partId);
    }

    public void saveDrawingsList(Grid<Drawings> grid) {
        saveList(grid.getInserted());
        for (Drawings dw : grid.getDeleted()) {
            delete(dw);
        }

        for (Drawings dw : grid.getUpdated()) {
            update(dw);
        }
    }

    public int[] getSuitCountPerDrawings(Long partId) {
        return drawingsDao.getSuitCountPerDrawings(partId);
    }
}
