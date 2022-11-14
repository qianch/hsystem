/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.siemens.bom.dao.impl;

import com.bluebirdme.mes.core.base.dao.BaseDaoImpl;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.sql.SQL;
import com.bluebirdme.mes.siemens.bom.dao.IDrawingsDao;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import org.xdemo.superutil.j2se.ListUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author 高飞
 * @Date 2016年4月5日 下午4:35:34
 */
@Repository
public class DrawingsDaoImpl extends BaseDaoImpl implements IDrawingsDao {
    @Resource
    SessionFactory factory;

    @Override
    public Session getSession() {
        return factory.getCurrentSession();
    }

    @Override
    public  Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
        return this.findPageInfo(filter, page, "drawings-list");
    }

    public List<Map<String, Object>> drawingsList(Long partId) {
        String sql = SQL.get("siemens-list-drawings");
        return getSession().createSQLQuery(sql).setParameter("pid", partId).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }

    public int[] getSuitCountPerDrawings(Long partId) {
        String sql = SQL.get("siemens-suitCoutPerDrawings-drawings");
        List list = getSession().createSQLQuery(sql).setParameter("partId", partId).list();
        if (ListUtils.isEmpty(list)) {
            return new int[]{};
        }
        int[] nums = new int[list.size()];
        int i = 0;
        for (Object obj : list) {
            nums[i++] = (int) obj;
        }
        return nums;
    }
}
