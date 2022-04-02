/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.baseInfo.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.bluebirdme.mes.baseInfo.dao.IFtcBcBomVersionDao;
import com.bluebirdme.mes.baseInfo.entity.FtcBcBomVersion;
import com.bluebirdme.mes.core.base.dao.BaseDaoImpl;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.sql.SQL;
import com.bluebirdme.mes.core.sql.SQLTemplateException;

/**
 * 
 * @author 徐秦冬
 * @Date 2017-12-6 16:26:52
 */
@Repository
public class FtcBcBomVersionDaoImpl extends BaseDaoImpl implements IFtcBcBomVersionDao {
	private static Logger logger = LoggerFactory.getLogger(FtcBcBomVersionDaoImpl.class);
	@Resource
	SessionFactory factory;

	@Override
	public Session getSession() {
		return factory.getCurrentSession();
	}

	@Override
	public <T> Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
		return findPageInfo(filter, page, "bcBomVersion-list");
	}

	@Override
	public void delete(String ids) throws Exception {
		String sql = "delete from hs_ftc_bc_bom_version  where id in :id";
		SQLQuery query = getSession().createSQLQuery(sql);
		String _ids[] = ids.split(",");
		query.setParameterList("id", _ids);
		query.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	public List<FtcBcBomVersion> getFtcBcBomJson(String id,String productType) throws SQLTemplateException {
		String hql="From FtcBcBomVersion where 1=1 and bid="+id+" and productType="+productType+" order by auditState desc,enabled";
		return getSession().createQuery(hql).list();
	}

	@Override
	public void deleteByPid(String ids) {
		HashMap map = new HashMap();
		map.put("id", ids);
		String sql="";
		try {
			sql = SQL.get(map, "bcBomVersion-delete-pid");
		} catch (SQLTemplateException e) {
			logger.error(e.getLocalizedMessage(),e);
		}
		SQLQuery query = getSession().createSQLQuery(sql);
		query.executeUpdate();
	}
}
