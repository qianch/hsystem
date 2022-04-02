/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2017版权所有
 */
package com.bluebirdme.mes.planner.pack.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.bluebirdme.mes.baseInfo.entity.FtcBcBom;
import com.bluebirdme.mes.core.base.dao.BaseDaoImpl;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.planner.pack.dao.IPackTaskDao;
import com.bluebirdme.mes.planner.pack.entity.PackTask;
import com.bluebirdme.mes.produce.entity.FinishedProduct;

/**
 * 
 * @author Goofy
 * @Date 2017年12月7日 上午10:26:42
 */
@Repository
public class PackTaskDaoImpl extends BaseDaoImpl implements IPackTaskDao {
	
	@Resource SessionFactory factory;

	@Override
	public Session getSession() {
		return factory.getCurrentSession();
	}

	@Override
	public <T> Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
		return null;
	}
	
	public List<PackTask> findProductTask(Long productId){
		
		FinishedProduct product=findById(FinishedProduct.class, productId);
		
		if(product==null||product.getProductIsTc()!=2){
			return new ArrayList<PackTask>();
		}
		
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("code", product.getProductPackagingCode());
		
		FtcBcBom fbb=findUniqueByMap(FtcBcBom.class, map);
		
		if(fbb==null){
			return new ArrayList<PackTask>();
		}
		
		String sql=
				"SELECT v.id id,null sodId,null ppdId,0 printedTrayBarcodeCount,null ptId,null produceTotalCount,0 leftCount,0 totalCount,null memo,v.id vid,v.version,rollsPerPallet rollsPerTray,CONCAT(b.code,'/',b.name,'/每托',v.rollsPerPallet,'卷') code \n" +
				"	from hs_ftc_bc_bom_version v\n" +
				"	left join (SELECT * from hs_ftc_bc_bom _v where _v.pid="+fbb.getId()+" ) b on v.bid=b.id\n" +
				"	where auditState=2 and enabled=0 and v.bid in (SELECT id from hs_ftc_bc_bom _b where _b.pid="+fbb.getId()+")";
		
		@SuppressWarnings("unchecked")
		List<PackTask> list=getSession().createSQLQuery(sql).addEntity(PackTask.class).list();
		
		return list;
	}
	
	public List<PackTask> findProduceTask(Long ppdId){
		String sql="SELECT ot.id,ot.code,ot.leftCount,ot.rollsPerTray,ot.sodId,ot.totalCount,ot.version,ot.vid,pt.memo,pt.produceTotalCount,pt.ptId,pt.printedTrayBarcodeCount,pt.ppdId from hs_pack_task pt LEFT JOIN hs_pack_task ot on pt.ptId=ot.id where pt.ppdId="+ppdId;
		@SuppressWarnings("unchecked")
		List<PackTask> list=getSession().createSQLQuery(sql).addEntity(PackTask.class).list();
		for(PackTask pt:list){
			if(pt==null)continue;
			getSession().evict(pt);
		}
		return list;
	}
	
	
	@Override
	public void updateLeftCount(List<PackTask> list) {
		for(PackTask t:list){
			getSession().createSQLQuery("update hs_pack_task,(SELECT case  when sum(produceTotalCount) is null then 0 else sum(produceTotalCount) end count from hs_pack_task where ptId="+t.getPtId()+") x set leftCount=totalCount -x.count where id="+t.getPtId()).executeUpdate();
		}
	}

}
