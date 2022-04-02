/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.complaint.dao.impl;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.bluebirdme.mes.core.base.dao.BaseDaoImpl;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.sql.SQL;

import org.springframework.stereotype.Repository;

import com.bluebirdme.mes.complaint.dao.IComplaintDao;
import com.bluebirdme.mes.complaint.entity.Complaint;

/**
 * 
 * @author 高飞
 * @Date 2016年4月5日 下午4:35:34
 */
@Repository
public class ComplaintDaoImpl extends BaseDaoImpl implements IComplaintDao {
	
	@Resource SessionFactory factory;

	@Override
	public Session getSession() {
		return factory.getCurrentSession();
	}

	@Override
	public <T> Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
		return this.findPageInfo(filter,page,"complaint-list");
	}
	
	public int getSerial(String code,String year){
		Complaint c=(Complaint) getSession().createQuery(SQL.get("complaint-serial")).setParameter("code", code+year+"%").setMaxResults(1).uniqueResult();
		
		if(c==null){
			return 0;
		}else{
			return Integer.parseInt(c.getComplaintCode().substring(10));
		}
	}

}
