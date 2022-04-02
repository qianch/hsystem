/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.siemens.order.dao.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.xdemo.superutil.j2se.ListUtils;

import com.bluebirdme.mes.core.base.dao.BaseDaoImpl;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.sql.SQL;
import com.bluebirdme.mes.siemens.order.dao.ICutTaskDao;
import com.bluebirdme.mes.siemens.order.entity.CutTask;

/**
 * 
 * @author 高飞
 * @Date 2016年4月5日 下午4:35:34
 */
@Repository
public class CutTaskDaoImpl extends BaseDaoImpl implements ICutTaskDao {
	
	@Resource SessionFactory factory;

	@Override
	public Session getSession() {
		return factory.getCurrentSession();
	}

	@Override
	public <T> Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
		return this.findPageInfo(filter,page,"cutTask-list");
	}
	
	public String getSerial(){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
		String preffix="XMZ"+sdf.format(new Date());
		
		String hql="From CutTask where taskCode like '"+preffix+"%' order by id desc";
		
		CutTask task=(CutTask) getSession().createQuery(hql).setMaxResults(1).uniqueResult();
		if(task==null)
			return preffix+"001";
		StringBuffer sb=new StringBuffer("000"+(Integer.parseInt(task.getTaskCode().replace(preffix, ""))+1));
		return preffix+sb.substring(sb.length()-3);
	}
	
	public void close(Integer closed,String id){
		getSession().createSQLQuery(SQL.get("cutTask-enable")).setParameter("closed", closed).setParameterList("id", id.split(",")).executeUpdate();
	}
	
	public int[] getSuitCountPerDrawings(Long partId,Long ctId){
		String sql=SQL.get("siemens-suitCoutPerDrawings-cutTask");
		List list=getSession().createSQLQuery(sql).setParameter("partId", partId).setParameter("ctId",ctId).list();
		if(ListUtils.isEmpty(list)){
			return new int[]{};
		}
		int[] nums=new int[list.size()];
		int i=0;
		for(Object obj:list){
			nums[i++]=(int) obj;
		}
		return nums;
	}

}
