/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.mobile.produce.dao.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;

import com.bluebirdme.mes.core.base.dao.BaseDaoImpl;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import org.springframework.stereotype.Repository;
import com.bluebirdme.mes.mobile.produce.dao.IFeedingRecordDao;

/**
 * 
 * @author 徐波
 * @Date 2016年4月5日 下午4:35:34
 */
@Repository
public class FeedingRecordDaoImpl extends BaseDaoImpl implements IFeedingRecordDao {
	
	@Resource SessionFactory factory;

	@Override
	public Session getSession() {
		return factory.getCurrentSession();
	}

	@Override
	public <T> Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
		return this.findPageInfo(filter,page,"feedingRecord-list-weave1");
	}
	@Override
	public <T> Map<String, Object> findPageInfo2(Filter filter, Page page) throws Exception {
		return this.findPageInfo(filter,page,"feedingRecord-list-cut");
	}

	@Override
	public List<Map<String, Object>> querylist(String deviceCode) {
		// TODO Auto-generated method stub
		String sql = "select * from HS_Feeding_Record where deviceCode='" + deviceCode + "' ORDER BY feedingDate DESC LIMIT 1";
		String sql1 = "select a.materialCode,a.id,a.deviceCode,a.weaveId,b.salesOrderCode,b.batchCode from HS_Feeding_Record a LEFT JOIN HS_Weave_Plan b on a.weaveId=b.id where a.deviceCode='" + deviceCode + "'";
		List<Map<String, Object>> list= getSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		if(list.size() > 0){
			sql1 = sql1 + " and a.weaveid=" + list.get(0).get("WEAVEID").toString();
		}
		List<Map<String, Object>> list1= getSession().createSQLQuery(sql1).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return list1;
	}

	@Override
	public void editInfo(String weaveid, String userid, String tlids) {
		// TODO Auto-generated method stub
		String sql = "update HS_Feeding_Record set weaveid=" + weaveid
				+ ", operateUserId=" + userid + " where id in(" + tlids + ")";
		getSession().createSQLQuery(sql).executeUpdate();
		
	}

	@Override
	public List<Map<String, Object>> querylist2(String deviceCode) {
		// TODO Auto-generated method stub
		String sql = "select * from HS_Feeding_Record where deviceCode='" + deviceCode + "' ORDER BY feedingDate DESC LIMIT 1";
		String sql1 = "select id,salesOrderCode,batchCode,productName from HS_Weave_Plan ";
		List<Map<String, Object>> list= getSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		if(list.size() > 0){
			sql1 = sql1 + " where  id=" + list.get(0).get("WEAVEID").toString();
		}
		List<Map<String, Object>> list1= getSession().createSQLQuery(sql1).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return list1;
	}

	@Override
	public List<Map<String, Object>> queryYlbh(String ylbh, String oldweaveId, String deviceCode) {
		// TODO Auto-generated method stub
		String sql = "select * from HS_Feeding_Record where weaveId='" + oldweaveId + "' and materialCode='" + ylbh + "' and deviceCode='" + deviceCode + "'";
		List<Map<String, Object>> list1= getSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return list1;
	}

	@Override
	public void deleteYlbh(String ylbh, String oldweaveId, String deviceCode) {
		// TODO Auto-generated method stub
		String sql = "delete from HS_Feeding_Record where materialCode='" + ylbh + "' and weaveId='" + oldweaveId + "' and deviceCode='" + deviceCode + "'";
		getSession().createSQLQuery(sql).executeUpdate();
	}
}
