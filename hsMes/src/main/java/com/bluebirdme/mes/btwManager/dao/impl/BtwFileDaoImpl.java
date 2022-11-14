/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.btwManager.dao.impl;

import com.bluebirdme.mes.btwManager.dao.IBtwFileDao;
import com.bluebirdme.mes.core.base.dao.BaseDaoImpl;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.sql.SQL;
import com.bluebirdme.mes.core.sql.SQLTemplateException;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 徐波
 * @Date 2016年4月5日 下午4:35:34
 */
@Repository
public class BtwFileDaoImpl extends BaseDaoImpl implements IBtwFileDao {
    @Resource
    SessionFactory factory;

    @Override
    public Session getSession() {
        return factory.getCurrentSession();
    }

    @Override
    public Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
        return this.findPageInfo(filter, page, "btwFile-list");
    }

    @Override
    public List<Map<String, Object>> queryBtwFilebyCustomerId(long consumerId, String tagType) throws SQLTemplateException {
        if (StringUtils.isNotEmpty(tagType)) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("consumerId", consumerId);
            map.put("tagType", tagType);
            String sql = SQL.get(map, "btwFile-listOfSelect");
            SQLQuery query = getSession().createSQLQuery(sql);
            query.setParameter("consumerId", consumerId).setParameter("tagType", tagType).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            return query.list();
        } else {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("consumerId", consumerId);
            String sql = SQL.get(map, "btwFile-listOfNoTagtypeSelect");
            SQLQuery query = getSession().createSQLQuery(sql);
            query.setParameter("consumerId", consumerId).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            return query.list();
        }
    }
}
