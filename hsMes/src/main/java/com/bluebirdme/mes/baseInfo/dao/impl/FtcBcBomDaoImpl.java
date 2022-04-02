/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.baseInfo.dao.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.bluebirdme.mes.core.base.dao.BaseDaoImpl;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import org.springframework.stereotype.Repository;
import com.bluebirdme.mes.baseInfo.dao.IFtcBcBomDao;
import com.bluebirdme.mes.baseInfo.entity.FtcBcBom;

/**
 * 
 * @author 高飞
 * @Date 2016年4月5日 下午4:35:34
 */
@Repository
public class FtcBcBomDaoImpl extends BaseDaoImpl implements IFtcBcBomDao {
	
	@Resource SessionFactory factory;

	@Override
	public Session getSession() {
		return factory.getCurrentSession();
	}

	@Override
	public <T> Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
		return this.findPageInfo(filter,page,"ftcBcBomVersion-list");
	}
	
	@SuppressWarnings("unchecked")
	public List<FtcBcBom> tree(String pid,String queryData,Integer level){
		
		String hql="From FtcBcBom f where 1=1 ";
		if(StringUtils.isBlank(queryData)){
			if(StringUtils.isBlank(pid)){
				hql+=" and pid is null";
			}else{
				hql+=" and pid ="+pid;
			}
		}else{//需要查询内容
			if(StringUtils.isBlank(pid)){
				hql+=" and f.pid is null ";
				hql+=" and (f.code like '%"+queryData+"%' or f.name like '%"+queryData+"%') ";
				hql+=" or f.id in(select ff.pid from FtcBcBom ff "+
					"where (ff.code like '%"+queryData+"%' or ff.name like '%"+queryData+"%') and ff.level=2"+
					"or ff.id in(select fff.pid from FtcBcBom fff "+
											"where (fff.code like '%"+queryData+"%' or fff.name like '%"+queryData+"%') and fff.level=3)"+
				")";
			}else{
				hql+=" and pid ="+pid;
				if(level ==2){
					hql+=" and (f.code like '%"+queryData+"%' or f.name like '%"+queryData+"%') ";
					hql+=" or f.id in(select ff.pid from FtcBcBom ff "+
										"where (ff.code like '%"+queryData+"%' or ff.name like '%"+queryData+"%') and ff.level=3"+
										")";
					
				}else if(level ==3){
					hql+=" and (f.code like '%"+queryData+"%' or f.name like '%"+queryData+"%') ";
				}
			}
		}
		
		
		return getSession().createQuery(hql).list();
	}

}
