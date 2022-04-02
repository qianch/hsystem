/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.baseInfo.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.bluebirdme.mes.core.base.dao.BaseDaoImpl;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.sql.SQL;

import org.springframework.stereotype.Repository;

import com.bluebirdme.mes.baseInfo.dao.IBcBomVersionDetailDao;
import com.bluebirdme.mes.baseInfo.entity.BcBomVersionDetail;

/**
 * 
 * @author 徐波
 * @Date 2016年4月5日 下午4:35:34
 */
@Repository
public class BcBomVersionDetailDaoImpl extends BaseDaoImpl implements IBcBomVersionDetailDao {
	
	@Resource SessionFactory factory;

	@Override
	public Session getSession() {
		return factory.getCurrentSession();
	}

	@Override
	public <T> Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
		return findPageInfo(filter, page,"bcBomVersionDetail-list");
	}

	@Override
	public void delete(String ids) throws Exception {
		 String sql = "delete from HS_Bc_Bom_Version_Detail  where id in :id";
		    SQLQuery query = getSession().createSQLQuery(sql);
		    ArrayList list1=new ArrayList();
		    String _ids[]=ids.split(",");
		    for (int a=0;a<_ids.length;a++) {
		    	list1.add(_ids[a]);
		      
		    }
		    query.setParameterList("id", list1);
		    query.executeUpdate();
	}
	
	@Override
	public void deleteByPid() {
		String sql = SQL.get( "bcBomVersionDetail-delete-pid");;
		SQLQuery query = getSession().createSQLQuery(sql);

		query.executeUpdate();
		
	}

}
