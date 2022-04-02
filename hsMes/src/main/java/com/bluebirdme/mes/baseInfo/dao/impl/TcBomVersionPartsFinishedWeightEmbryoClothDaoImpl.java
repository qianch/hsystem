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

import com.bluebirdme.mes.core.base.dao.BaseDaoImpl;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.sql.SQL;

import org.springframework.stereotype.Repository;

import com.bluebirdme.mes.baseInfo.dao.ITcBomVersionPartsDetailDao;
import com.bluebirdme.mes.baseInfo.dao.ITcBomVersionPartsFinishedWeightEmbryoClothDao;

/**
 * 
 * @author 徐秦冬
 * @Date 2017-11-27 18:57:36
 */
@Repository
public class TcBomVersionPartsFinishedWeightEmbryoClothDaoImpl extends BaseDaoImpl implements ITcBomVersionPartsFinishedWeightEmbryoClothDao {
	
	@Resource SessionFactory factory;

	@Override
	public Session getSession() {
		return factory.getCurrentSession();
	}

	@Override
	public <T> Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
		return findPageInfo(filter, page,"partsFinishedWeightEmbryoCloth-list");
	}
    
	public void delete(String ids) {
		getSession().createSQLQuery(SQL.get("partsFinishedWeightEmbryoCloth-delete")).setParameterList("id", ids.split(",")).executeUpdate();
	}

	@Override
	public Map<String, Object> findPageInfo1(Filter filter, Page page) {
		return this.findPageInfo(filter, page,"partsFinishedWeightEmbryoCloth-mirrorList");
	}

}
