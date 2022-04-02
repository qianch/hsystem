/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.siemens.bom.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.bluebirdme.mes.baseInfo.entity.TcBomVersionParts;
import com.bluebirdme.mes.core.base.dao.BaseDaoImpl;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.sql.SQL;
import com.bluebirdme.mes.siemens.bom.dao.ISiemensBomDao;

/**
 * 
 * @author 高飞
 * @Date 2016年4月5日 下午4:35:34
 */
@Repository
public class SiemensBomDaoImpl extends BaseDaoImpl implements ISiemensBomDao {
	
	@Resource SessionFactory factory;

	@Override
	public Session getSession() {
		return factory.getCurrentSession();
	}

	@Override
	public <T> Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
		return this.findPageInfo(filter,page,"siemens-tc-list");
	}
	
	@Override
	public List<Map<String, Object>> findPageInfo(Boolean siemens,String code) throws Exception {
		Map<String, Object> map=new HashMap<String, Object>();
		if(siemens!=null&&siemens)
			map.put("siemens", siemens);
		
		if(!StringUtils.isBlank(code)){
			map.put("code", code);
		}
		
		String sql=SQL.get(map,"siemens-tc-list");
		
		return getSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}

	
	public List<Map<String,Object>> listAllParts(Long tcBomId){
		String sql=SQL.get("siemens-tc-parts");
		return getSession().createSQLQuery(sql).setParameter("tcBomId", tcBomId).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}
	
	public int enableOrDisable(Long partId,Integer enable,Boolean isDrawingsBom){
		TcBomVersionParts part=findById(TcBomVersionParts.class, partId);
		if(enable==null){
			if(isDrawingsBom){
				return part.getDrawingsComplete()==null?0:part.getDrawingsComplete();
			}else{
				return part.getSuitComplete()==null?0:part.getSuitComplete();
			}
		}
		if(isDrawingsBom){
			part.setDrawingsComplete(enable);
			update(part);
			return part.getDrawingsComplete();
		}else{
			part.setSuitComplete(enable);
			update(part);
			return part.getSuitComplete();
		}
	}
	
}
