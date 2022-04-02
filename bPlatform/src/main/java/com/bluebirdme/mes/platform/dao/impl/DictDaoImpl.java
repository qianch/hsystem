package com.bluebirdme.mes.platform.dao.impl;

import com.bluebirdme.mes.core.base.dao.BaseDaoImpl;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.sql.SQL;
import com.bluebirdme.mes.core.sql.SQLTemplateException;
import com.bluebirdme.mes.platform.dao.IDictDao;
import com.bluebirdme.mes.platform.entity.Dict;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.xdemo.superutil.j2se.MapUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author qianchen
 * @date 2020/05/21
 */
@Repository
public class DictDaoImpl extends BaseDaoImpl implements IDictDao {
    private static final Logger logger = LoggerFactory.getLogger(DictDaoImpl.class);
    @Resource
    SessionFactory factory;

    @Override
    public Session getSession() {
        return this.factory.getCurrentSession();
    }

    @Override
    public <T> Map<String, Object> findPageInfo(final Filter filter, final Page page) throws Exception {
        final Map<String, Object> ret = super.findPageInfo(filter, page, "dict-list");
        final List<Map<String, Object>> list = (List<Map<String, Object>>) ret.get("rows");
        for (final Map<String, Object> map : list) {
            Object deprecated = map.get("DEPRECATED");
            if (deprecated != null && String.valueOf(deprecated).equals("1")) {
                map.put("NAME_ZH_CN", MapUtils.getAsStringIgnoreCase(map, "NAME_ZH_CN") + "[弃用]");
            }
        }
        return ret;
    }

    @Override
    public List<Map<String, Object>> queryDict(String rootcode) throws SQLTemplateException {
        String[] rootcodes = rootcode.split(",");
        String strRootcode = "";
        for (int i = 0; i < rootcodes.length; i++) {
            strRootcode += "'" + rootcodes[i] + "',";
        }

        if (strRootcode.length() > 0) {
            strRootcode = strRootcode.substring(0, strRootcode.length() - 1);
        }

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("rootcode", strRootcode);
        String sql = SQL.get(map, "dict-combobox");
        return getSession().createSQLQuery(sql)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .list();
    }

    @Override
    public List<Map<String, String>> queryDictAll() throws SQLTemplateException {
        HashMap<String, Object> map = new HashMap<String, Object>();
        String sql = SQL.get(map, "dictall-combobox");
        return getSession().createSQLQuery(sql)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .list();
    }

    @Override
    public void delete(final String ids) {
        this.getSession().createSQLQuery(SQL.get("dict-delete"))
                .setParameterList("id", ids
                        .split(","))
                .executeUpdate();
    }

    @Override
    public List<Map<String, Object>> findByCode(final String code) {
        final SQLQuery query = this.getSession().createSQLQuery(SQL.get("dict-findByCode"));
        query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        query.setParameter("rootcode", code);
        final List<Map<String, Object>> ret = (List<Map<String, Object>>) query.list();
        for (final Map<String, Object> map : ret) {
            Object deprecated = map.get("DEPRECATED");
            if (deprecated != null && String.valueOf(deprecated).equals("1")) {
                map.put("NAME", MapUtils.getAsStringIgnoreCase((Map) map, "NAME_ZH_CN") + "[弃用]");
            }
        }
        return ret;
    }

    @Override
    public List<Map<String, Object>> combotree(final String code, final String defaultId) throws SQLTemplateException {
        final Map<String, String> map = new HashMap();
        map.put("defaultId", defaultId);
        map.put("rootcode", code);
        final String sql = SQL.get(map, "dict-combobox");
        logger.debug(sql);
        final SQLQuery query = getSession().createSQLQuery(sql);
        query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        final List<Map<String, Object>> ret = (List<Map<String, Object>>) query.list();
        for (final Map<String, Object> map2 : ret) {
            if (map2.get("DEPRECATED").toString().equals("1")) {
                map2.put("NAME", MapUtils.getAsStringIgnoreCase(map2, "NAME_ZH_CN") + "[弃用]");
            }
        }
        return ret;
    }

    @Override
    public void update(final Dict dict) {
        super.update(dict);
        if (dict.getChildren() != null) {
            if (dict.getDeprecated() == 0) {
                this.getSession().createSQLQuery(SQL.get("dict-enable-batch"))
                        .setParameterList("id", dict.getChildren().split(","))
                        .executeUpdate();
            } else {
                this.getSession().createSQLQuery(SQL.get("dict-deprecated-batch"))
                        .setParameterList("id", dict.getChildren().split(","))
                        .executeUpdate();
            }
        }
    }
}
