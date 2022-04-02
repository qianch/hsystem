/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.btwManager.dao.impl;

import com.bluebirdme.mes.btwManager.dao.IBtwFilePrintDao;
import com.bluebirdme.mes.core.base.dao.BaseDaoImpl;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.sql.SQL;
import com.bluebirdme.mes.core.sql.SQLTemplateException;
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
 *
 * @author 徐波
 * @Date 2016年4月5日 下午4:35:34
 */
@Repository
public class BtwFilePrintDaoImpl extends BaseDaoImpl implements IBtwFilePrintDao {

	@Resource SessionFactory factory;

	@Override
	public Session getSession() {
		return factory.getCurrentSession();
	}

	@Override
	public <T> Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
		return null;
	}

	public List<Map<String, Object>> findBtwFilePrints(Long btwFileId) throws Exception
	{
		String sql= SQL.get("findBtwFilePrints");
		SQLQuery query=getSession().createSQLQuery(sql);
		query.setParameter("btwFileId", btwFileId);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return query.list();
	}

}
