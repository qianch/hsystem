package com.bluebirdme.mes.stock.dao.impl;

import com.bluebirdme.mes.core.base.dao.BaseDaoImpl;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.stock.dao.IMaterialActionDao;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Map;

@Repository
public class MaterialActionDaoImpl extends BaseDaoImpl implements IMaterialActionDao {
    @Resource
    SessionFactory factory;

    @Override
    public Session getSession() {
        return factory.getCurrentSession();
    }

    @Override
    public Map<String, Object> findPageInfo(Filter filter, Page page)
            throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//24小时制
        if (filter.get("start") != null) {
            long startTimes = simpleDateFormat.parse(filter.get("start").toString()).getTime();
            filter.set("start", String.valueOf(startTimes));
        }
        if (filter.get("end") != null) {
            long startTimes = simpleDateFormat.parse(filter.get("end")).getTime();
            filter.set("end", String.valueOf(startTimes));
        }
        return this.findPageInfo(filter, page, "materialIn-list");
    }

    public Map<String, Object> findPageOutInfo(Filter filter, Page page) throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (filter.get("start") != null) {
            long startTimes = simpleDateFormat.parse(filter.get("start")).getTime();
            filter.set("start", String.valueOf(startTimes));
        }
        if (filter.get("end") != null) {
            long startTimes = simpleDateFormat.parse(filter.get("end")).getTime();
            filter.set("end", String.valueOf(startTimes));
        }
        return this.findPageInfo(filter, page, "materialOut-list");
    }

    @Override
    public Map<String, Object> findPageDetailInfo(Filter filter, Page page) throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (filter.get("start") != null) {
            long startTimes = simpleDateFormat.parse(filter.get("start")).getTime();
            filter.set("start", String.valueOf(startTimes));
        }
        if (filter.get("end") != null) {
            long startTimes = simpleDateFormat.parse(filter.get("end")).getTime();
            filter.set("end", String.valueOf(startTimes));
        }
        return this.findPageInfo(filter, page, "materialDetail-list");
    }

    public void action(Long[] mssId, String action, String actionValue) {
        String sql = "update hs_material_stock_state set " + action + "=" + actionValue + " where id in (:ids)";
        getSession().createSQLQuery(sql).setParameterList("ids", mssId).executeUpdate();
    }

    @Override
    public Map<String, Object> findPageForceOutInfo(Filter filter, Page page) throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (filter.get("start") != null) {
            long startTimes = simpleDateFormat.parse(filter.get("start")).getTime();
            filter.set("start", String.valueOf(startTimes));
        }
        if (filter.get("end") != null) {
            long startTimes = simpleDateFormat.parse(filter.get("end")).getTime();
            filter.set("end", String.valueOf(startTimes));
        }
        return this.findPageInfo(filter, page, "materialForceOut-list");
    }
}
