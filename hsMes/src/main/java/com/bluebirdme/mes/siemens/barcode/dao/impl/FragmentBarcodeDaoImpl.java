/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.siemens.barcode.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import com.bluebirdme.mes.core.base.dao.BaseDaoImpl;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.sql.SQL;
import com.bluebirdme.mes.core.sql.SQLTemplateException;
import com.bluebirdme.mes.siemens.barcode.dao.IFragmentBarcodeDao;

/**
 * 
 * @author 高飞
 * @Date 2016年4月5日 下午4:35:34
 */
@Repository
public class FragmentBarcodeDaoImpl extends BaseDaoImpl implements IFragmentBarcodeDao {
	
	@Resource SessionFactory factory;

	@Override
	public Session getSession() {
		return factory.getCurrentSession();
	}

	@Override
	public <T> Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
		return this.findPageInfo(filter,page,"fragmentBarcode-list");
	}
	
	public String fragmentCheckPacked(String[] fragmentBarcodes){
		String sql=SQL.get("fragment-check-packed");
		String codes=(String) getSession().createSQLQuery(sql).setParameterList("codeList", fragmentBarcodes).uniqueResult();
		return codes;
	}
	
	public void updateFragmentBarcodeInfo(String codes,String user,String date,String device) throws SQLTemplateException{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("codes", codes);
		map.put("user", user);
		map.put("date", date);
		map.put("device", device);
		String sql=SQL.get(map,"fragment-update-suit-info");
		//System.out.println(sql);
		getSession().createSQLQuery(sql).executeUpdate();
	}
	
	public List<String> getFeedingFarbic(Long cutId){
		String sql=SQL.get("fragment-review-feeding");
		return getSession().createSQLQuery(sql).setParameter("cutId", cutId).list();
	}
	
	public List<Map<String,Object>> getSuitInfo(String code){
		String sql=SQL.get("fragment-suit-info");
		return getSession().createSQLQuery(sql).setParameter("code", code).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}

}
