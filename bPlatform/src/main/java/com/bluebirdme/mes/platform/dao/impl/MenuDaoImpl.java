package com.bluebirdme.mes.platform.dao.impl;

import com.bluebirdme.mes.core.base.dao.BaseDaoImpl;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.sql.SQL;
import com.bluebirdme.mes.core.sql.SQLTemplateException;
import com.bluebirdme.mes.platform.dao.IMenuDao;
import com.bluebirdme.mes.platform.entity.Menu;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author qianchen
 * @date 2020/05/21
 */
@Repository
public class MenuDaoImpl extends BaseDaoImpl implements IMenuDao {
    @Resource
    SessionFactory factory;
    private List<Menu> menuList;

    @Override
    public Session getSession() {
        return this.factory.getCurrentSession();
    }

    @Override
    public Map<String, Object> findPageInfo(final Filter filter, final Page page) throws Exception {
        return super.findPageInfo(filter, page, "menu-list");
    }

    @Override
    public void batchUpdateMenuLevel(final List<Menu> list) {
        final String sql = SQL.get("menu-batchUpdateMenuLevel");
        final SQLQuery query = getSession().createSQLQuery(sql);
        for (final Menu menu : list) {
            query.setParameter("parentId", menu.getParentId());
            query.setParameter("id", menu.getId());
            query.executeUpdate();
        }
    }

    @Override
    public void batchDelete(final List<Menu> list) {
        menuList = new ArrayList();
        menuList.addAll(list);
        for (final Menu menu : list) {
            this.getSubMenus(menu);
        }
        final Query query = getSession().createQuery(SQL.get("menu-batchDelete"));
        final Long[] ids = new Long[menuList.size()];
        for (int i = 0; i < ids.length; ++i) {
            ids[i] = menuList.get(i).getId();
        }
        query.setParameterList("id", ids);
        query.executeUpdate();
    }

    public void batchDelete(final Menu menu) {
    }

    public void getSubMenus(final Menu menu) {
        final String sql = "From Menu where parentId=:parentId";
        final Query query = getSession().createQuery(sql);
        final List<Menu> list = (List<Menu>) query.setParameter("parentId", menu.getId()).list();
        if (list != null && list.size() != 0) {
            menuList.addAll(list);
            for (final Menu subMenu : list) {
                getSubMenus(subMenu);
            }
        }
    }

    @Override
    public List<Menu> myMenu(final Long uid) throws SQLTemplateException {
        final Filter filter = new Filter();
        filter.set("uid", uid + "");
        final SQLQuery query = this.getSession().createSQLQuery(SQL.get(filter.getFilter(), "menu-mymenu"));
        if (uid != -1L) {
            query.setParameter("u.uid", uid);
        }
        query.addEntity(Menu.class);
        return (List<Menu>) query.list();
    }

    @Override
    public String getMenuCode(final Long pid) {
        final Menu parent = super.findById(Menu.class, pid);
        final Query query = this.getSession().createQuery("select max(code) from Menu where parentId=:pid");
        query.setParameter("pid", parent.getId());
        final Object o = query.uniqueResult();
        if (o == null) {
            return (parent.getCode() == null) ? "001" : (parent.getCode() + "001");
        }
        final Long i = new Long(o.toString());
        return "00" + (i + 1L);
    }

    @Override
    public List<Menu> m() {
        return super.findAll(Menu.class);
    }
}
