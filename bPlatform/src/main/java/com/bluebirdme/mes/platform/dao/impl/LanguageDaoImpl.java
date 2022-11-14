package com.bluebirdme.mes.platform.dao.impl;

import com.bluebirdme.mes.core.base.dao.BaseDaoImpl;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.sql.SQL;
import com.bluebirdme.mes.core.sql.SQLTemplateException;
import com.bluebirdme.mes.platform.dao.ILanguageDao;
import com.bluebirdme.mes.platform.entity.Language;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author qianchen
 * @date 2020/06/30
 */
@Repository
public class LanguageDaoImpl extends BaseDaoImpl implements ILanguageDao {
    @Resource
    SessionFactory factory;

    @Override
    public Session getSession() {
        return factory.getCurrentSession();
    }

    @Override
    public Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
        return findPageInfo(filter, page, "language-list");
    }

    @Override
    public List<Language> queryLanguageList(String languageCode) throws SQLTemplateException {
        HashMap<String, Object> map = new HashMap();
        map.put("languageCode", languageCode);
        String sql = SQL.get(map, "language-list");
        return getSession()
                .createSQLQuery(sql)
                .setParameter("languageCode", "%" + languageCode + "%")
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }
}
