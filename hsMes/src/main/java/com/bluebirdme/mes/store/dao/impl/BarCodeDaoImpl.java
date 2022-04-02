/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.store.dao.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.bluebirdme.mes.core.base.dao.BaseDaoImpl;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.sql.SQL;
import com.bluebirdme.mes.store.dao.IBarCodeDao;

/**
 * 
 * @author 高飞
 * @Date 2016年4月5日 下午4:35:34
 */
@Repository
public class BarCodeDaoImpl extends BaseDaoImpl implements IBarCodeDao {
	
	@Resource SessionFactory factory;

	@Override
	public Session getSession() {
		return factory.getCurrentSession();
	}

	@Override
	public <T> Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
		return null;
	}


	public int countRollsInTray(String trayCode){
		String sql=SQL.get("count-rolls-in-tray");
		Map<String,Object> rs= (Map<String, Object>) getSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).setParameter("code", trayCode).uniqueResult();
		return ((BigInteger) rs.get("ROLLS")).intValue();
	}
	
}
