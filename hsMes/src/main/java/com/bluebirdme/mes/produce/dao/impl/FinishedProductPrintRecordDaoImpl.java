/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.produce.dao.impl;
import com.bluebirdme.mes.core.base.dao.BaseDaoImpl;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.sql.SQL;
import com.bluebirdme.mes.produce.dao.IFinishedProductPrintRecordDao;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * @author 徐波
 * @Date 2016年4月5日 下午4:35:34
 */
@Repository
public class FinishedProductPrintRecordDaoImpl extends BaseDaoImpl implements IFinishedProductPrintRecordDao {

	@Resource SessionFactory factory;

	@Override
	public Session getSession() {
		return factory.getCurrentSession();
	}

	@Override
	public <T> Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
		return null;
	}

	public List<Map<String, Object>> findFinishedProductPrintRecords(Long productId) throws Exception
	{
		String sql= SQL.get("findFinishedProductPrintRecords");
		SQLQuery query=getSession().createSQLQuery(sql);
		query.setParameter("productId", productId);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return query.list();
	}

}
