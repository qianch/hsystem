/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.planner.produce.dao.impl;

import com.bluebirdme.mes.core.base.dao.BaseDaoImpl;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.sql.SQL;
import com.bluebirdme.mes.planner.produce.dao.IProducePlanDetailDao;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author 高飞
 * @Date 2016年4月5日 下午4:35:34
 */
@Repository
public class ProducePlanDetailDaoImpl extends BaseDaoImpl implements IProducePlanDetailDao {
    @Resource
    SessionFactory factory;

    @Override
    public Session getSession() {
        return factory.getCurrentSession();
    }

    @Override
    public Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
        return this.findPageInfo(filter, page, "producePlanDetail-list");
    }

    @Override
    public Map<String, Object> findProducePlanDetail(Filter filter, Page page) {
        return this.findPageInfo(filter, page, "producePlanDetail-list2");
    }

    @Override
    public List<Map<String, Object>> findProducePlanDetailPrints(Long producePlanDetailId) {
        String sql = SQL.get("findProducePlanDetailPrints");
        SQLQuery query = getSession().createSQLQuery(sql);
        query.setParameter("producePlanDetailId", producePlanDetailId);
        query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.list();
    }

    @Override
    public List<Map<String, Object>> findPlanDetailPrintsBybtwFileId(Long producePlanDetailId, long btwFileId) {
        String sql = SQL.get("findPlanDetailPrintsBybtwFileId");
        SQLQuery query = getSession().createSQLQuery(sql);
        query.setParameter("producePlanDetailId", producePlanDetailId);
        query.setParameter("btwFileId", btwFileId);
        query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.list();
    }
}
