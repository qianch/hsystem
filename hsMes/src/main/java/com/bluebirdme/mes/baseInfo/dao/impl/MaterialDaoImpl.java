/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.baseInfo.dao.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;

import com.bluebirdme.mes.core.base.dao.BaseDaoImpl;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.sql.SQL;

import org.springframework.stereotype.Repository;

import com.bluebirdme.mes.baseInfo.dao.IMaterialDao;

/**
 * 
 * @author 高飞
 * @Date 2016年4月5日 下午4:35:34
 */
@Repository
public class MaterialDaoImpl extends BaseDaoImpl implements IMaterialDao {
	
	@Resource SessionFactory factory;

	@Override
	public Session getSession() {
		return factory.getCurrentSession();
	}

	@Override
	public <T> Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
		return this.findPageInfo(filter,page,"material-list");
	}
	
	public Map<String,Object> materialInfo(String code){
		String sql="select * from hs_material_stock_detail_view where palletCode=:code";
		return (Map<String, Object>) getSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).setParameter("code", code).uniqueResult();
	}
	

}
