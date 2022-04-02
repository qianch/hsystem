/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.store.dao.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.bluebirdme.mes.core.sql.SQL;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.bluebirdme.mes.core.base.dao.BaseDaoImpl;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import com.bluebirdme.mes.store.dao.IRollBarcodeDao;

/**
 *
 * @author 徐波
 * @Date 2016年4月5日 下午4:35:34
 */
@Repository
public class RollBarcodeDaoImpl extends BaseDaoImpl implements IRollBarcodeDao {

	@Resource SessionFactory factory;

	@Override
	public Session getSession() {
		return factory.getCurrentSession();
	}

	@Override
	public <T> Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
		return this.findPageInfo(filter,page,"rollBarcode-list");
	}

	public List<Map<String, Object>> findMaxRollBarCode(long btwfileId) {
		String sql = SQL.get("findMaxRollBarCode");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("btwfileId", btwfileId);
		SQLQuery query = getSession().createSQLQuery(sql);
		List<Map<String, Object>> list = query.setParameter("btwfileId", btwfileId).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return list;
	}

	public List<Map<String, Object>> findMaxRollBarCodeCount() {
		String sql = SQL.get("findMaxRollBarCodeCount");
		SQLQuery query = getSession().createSQLQuery(sql);
		List<Map<String, Object>> list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return list;
	}

}
