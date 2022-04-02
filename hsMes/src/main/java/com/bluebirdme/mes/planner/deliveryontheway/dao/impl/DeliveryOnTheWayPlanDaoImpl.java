/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.planner.deliveryontheway.dao.impl;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.bluebirdme.mes.planner.deliveryontheway.dao.IDeliveryOnTheWayPlanDao;
import com.bluebirdme.mes.planner.deliveryontheway.entity.DeliveryOnTheWayPlan;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;

import com.bluebirdme.mes.core.base.dao.BaseDaoImpl;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.sql.SQL;

import org.springframework.stereotype.Repository;


/**
 * 
 * @author 徐波
 * @Date 2016年4月5日 下午4:35:34
 */
@Repository
public class DeliveryOnTheWayPlanDaoImpl extends BaseDaoImpl implements IDeliveryOnTheWayPlanDao {
	
	@Resource SessionFactory factory;

	@Override
	public Session getSession() {
		return factory.getCurrentSession();
	}



	@Override
	public <T> Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
		//return this.findPageInfo(filter,page,"productDeliveryRecord-list");
		return this.findPageInfo(filter,page,"productDeliveryOnTheWayRecord-list");
	}

	@Override
	public String getSerial(String type){
		Date now=new Date();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMM");
		String preffix=type+sdf.format(now);
		DeliveryOnTheWayPlan dp= (DeliveryOnTheWayPlan) getSession().createQuery(SQL.get("onthewaydelivery-code")).setParameter("code", preffix+"%").setMaxResults(1).uniqueResult();
		System.out.println(type);
		if(dp==null){
			System.out.println("NULL");
			return preffix+"0001";
		}else{
			String suffix="0000"+(Integer.parseInt(dp.getDeliveryCode().replace(preffix, ""))+1);
			return preffix+suffix.substring(suffix.length()-4);
		}
	}


	@Override
	public List<Map<String, Object>> findDeliveryOnTheWayPlanDetails(Long deliveryId) {
		String sql=SQL.get("findDeliveryOnTheWayPlanDetailsByDeliveryId");
		SQLQuery query=getSession().createSQLQuery(sql);
		query.setParameter("deliveryId", deliveryId);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return query.list();
	}

	@Override
	public List<Map<String, Object>> findProductDeliveryOnTheWayPlanDetailsByDeliveryId(Long deliveryId) {
		String sql=SQL.get("findProductDeliveryOnTheWayPlanDetailsByDeliveryId");
		SQLQuery query=getSession().createSQLQuery(sql);
		query.setParameter("deliveryId", deliveryId);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return query.list();
	}

	public <T> Map<String,Object> findPageInfoTotalWeight(Filter filter,Page page)
			throws Exception{
		return  this.findPageInfo(filter,page,"productDeliveryOnTheWayRecord-totalweight");
	}


	public <T> Map<String,Object> findDeliveryOnTheWayPlanDetails(Filter filter,Page page)throws Exception{
		return  this.findPageInfo(filter,page,"findProductDeliveryOnTheWayPlanDetails");
	}
}
