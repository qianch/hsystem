/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.baseInfo.dao.impl;

import com.bluebirdme.mes.baseInfo.dao.IBCBomVersionDao;
import com.bluebirdme.mes.core.base.dao.BaseDaoImpl;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.sql.SQL;
import com.bluebirdme.mes.core.sql.SQLTemplateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class BCBomVersionDaoImpl extends BaseDaoImpl implements IBCBomVersionDao {
    private static final Logger logger = LoggerFactory.getLogger(BCBomVersionDaoImpl.class);
    @Resource
    SessionFactory factory;

    @Override
    public Session getSession() {
        return factory.getCurrentSession();
    }

    @Override
    public Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
        return findPageInfo(filter, page, "bcBomVersion-list");
    }

    @Override
    public void delete(String ids) throws Exception {
        String sql = "delete from HS_Bc_Bom_Version  where id in :id";
        SQLQuery query = getSession().createSQLQuery(sql);
        String _ids[] = ids.split(",");
        query.setParameterList("id", _ids);
        query.executeUpdate();
    }

    public List<Map<String, Object>> getBcBomJson(String id) throws SQLTemplateException {
        HashMap map = new HashMap();
        map.put("id", id);
        String sql = SQL.get(map, "bcBomVersion-list");
        SQLQuery query = getSession().createSQLQuery(sql);
        query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.list();
    }

    @Override
    public void deleteByPid(String ids) {
        HashMap map = new HashMap();
        map.put("id", ids);
        String sql = "";
        try {
            sql = SQL.get(map, "bcBomVersion-delete-pid");
        } catch (SQLTemplateException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        SQLQuery query = getSession().createSQLQuery(sql);
        query.executeUpdate();
    }
}
