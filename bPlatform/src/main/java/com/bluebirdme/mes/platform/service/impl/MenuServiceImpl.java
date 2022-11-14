package com.bluebirdme.mes.platform.service.impl;

import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.core.sql.SQLTemplateException;
import com.bluebirdme.mes.platform.dao.IMenuDao;
import com.bluebirdme.mes.platform.entity.Menu;
import com.bluebirdme.mes.platform.service.IMenuService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author qianchen
 * @date 2020/05/21
 */

@Service
@Transactional
public class MenuServiceImpl extends BaseServiceImpl implements IMenuService {
    @Resource
    IMenuDao menuDao;

    @Override
    protected IBaseDao getBaseDao() {
        return menuDao;
    }

    @Override
    public Map<String, Object> findPageInfo(final Filter filter, final Page page) throws Exception {
        return menuDao.findPageInfo(filter, page);
    }

    @Override
    public void batchUpdateMenuLevel(final List<Menu> list) {
        menuDao.batchUpdateMenuLevel(list);
    }

    @Override
    public void batchDelete(final List<Menu> list) {
        menuDao.batchDelete(list);
    }

    @Override
    public List<Menu> myMenu(final Long uid) throws SQLTemplateException {
        return menuDao.myMenu(uid);
    }

    @Override
    public String getMenuCode(final Long pid) {
        return menuDao.getMenuCode(pid);
    }

    @Override
    public List<Menu> m() {
        return menuDao.m();
    }
}
