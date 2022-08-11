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
import com.bluebirdme.mes.siemens.bom.dao.IFragmentDao;
import com.bluebirdme.mes.siemens.bom.entity.Fragment;
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
public class FragmentDaoImpl extends BaseDaoImpl implements IFragmentDao {
    @Resource
    SessionFactory factory;

    @Override
    public Session getSession() {
        return factory.getCurrentSession();
    }

    @Override
    public <T> Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
        return this.findPageInfo(filter, page, "fragment-list");
    }

    public List<Fragment> fragmentList(Long tcBomId) {
        String hql = "From Fragment where tcBomId=" + tcBomId + " and isDeleted=0 order by fragmentCode asc";
        return getSession().createQuery(hql).list();
    }

    @Override
    public List<Map<String, Object>> findFragmentBytcBomIdAndfragmentCode(Long tcBomId, String fragmentCode) {
        String sql = SQL.get("siemens-list");
        return getSession().createSQLQuery(sql).setParameter("tcBomId", tcBomId).setParameter("fragmentCode", fragmentCode).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }
}
