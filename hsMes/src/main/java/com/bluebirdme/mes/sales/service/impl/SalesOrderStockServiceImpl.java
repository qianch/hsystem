/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.sales.service.impl;

import java.util.Map;

import javax.annotation.Resource;
import com.bluebirdme.mes.core.annotation.AnyExceptionRollback;

import org.springframework.stereotype.Service;

import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.sales.dao.ISalesOrderStockDao;
import com.bluebirdme.mes.sales.service.ISalesOrderStockService;

/**
 * 
 * @author 高飞
 * @Date 2016-12-15 18:28:22
 */
@Service
@AnyExceptionRollback
public class SalesOrderStockServiceImpl extends BaseServiceImpl implements ISalesOrderStockService {
	
	@Resource ISalesOrderStockDao salesOrderStockDao;
	
	@Override
	protected IBaseDao getBaseDao() {
		return salesOrderStockDao;
	}

	@Override
	public <T> Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
		return salesOrderStockDao.findPageInfo(filter,page);
	}
	
	
	/**
	 * 套材添加库存
	 * @param salesOrderSubCode
	 * @param product
	 * @param partName
	 *//*
	public void addStock(String salesOrderSubCode,FinishedProduct product,String partName){
		
		SalesOrderStock sos=getSalesOrderStock(salesOrderSubCode, product.getId(),partName);
		
		if(sos!=null){
			sos.setTrayCount(sos.getTrayCount()+1);
			update(sos);
		}else{
			sos=new SalesOrderStock();
			sos.setPartName(partName);
			sos.setConsumerProductName(product.getConsumerProductName());
			sos.setFactoryProductName(product.getFactoryProductName());
			sos.setProductId(product.getId());
			sos.setSalesOrderSubCode(salesOrderSubCode);
			sos.setUsedTrayCount(0);
			sos.setTrayCount(1);
			save(sos);
		}
	}
	
	*//**
	 * 添加非套材库存
	 * @param salesOrderSubCode
	 * @param product
	 *//*
	public void addStock(String salesOrderSubCode,FinishedProduct product,int count){
		
		SalesOrderStock sos=getSalesOrderStock(salesOrderSubCode, product.getId(),null);
		
		
		if(sos!=null){
			int finalCount=(sos.getTrayCount()==null?0:sos.getTrayCount())+count;
			if(finalCount<0){
				finalCount=0;
			}
			sos.setTrayCount(finalCount);
			update(sos);
		}else{
			sos=new SalesOrderStock();
			sos.setPartName(null);
			sos.setConsumerProductName(product.getConsumerProductName());
			sos.setFactoryProductName(product.getFactoryProductName());
			sos.setProductId(product.getId());
			sos.setSalesOrderSubCode(salesOrderSubCode);
			sos.setUsedTrayCount(0);
			sos.setTrayCount((count<0?0:count));
			save(sos);
		}
	}
public void addStock(String salesOrderSubCode,FinishedProduct product){
		
		SalesOrderStock sos=getSalesOrderStock(salesOrderSubCode, product.getId(),null);
		
		if(sos!=null){
			sos.setTrayCount(sos.getTrayCount()+1);
			update(sos);
		}else{
			sos=new SalesOrderStock();
			sos.setPartName(null);
			sos.setConsumerProductName(product.getConsumerProductName());
			sos.setFactoryProductName(product.getFactoryProductName());
			sos.setProductId(product.getId());
			sos.setSalesOrderSubCode(salesOrderSubCode);
			sos.setUsedTrayCount(0);
			sos.setTrayCount(1);
			save(sos);
		}
	}*/
	
	/**
	 * 生产发货单时候调用
	 * @param sos
	 * @throws Exception 
	 */
	/*public void preOutStock(List<SalesOrderStock> sosList) throws Exception{
		
		SalesOrderStock _sos;
		
		for(SalesOrderStock sos:sosList ){
			
			_sos=getSalesOrderStock(sos.getSalesOrderSubCode(), sos.getProductId(), sos.getPartName());
			
			_sos.setUsedTrayCount(_sos.getTrayCount()+sos.getTrayCount());
			
			if(_sos.getUsedTrayCount()>_sos.getTrayCount()){
				throw new Exception("订单:"+sos.getSalesOrderSubCode()+"产品"+sos.getFactoryProductName()+"库存不足");
			}
			
			update(_sos);
		}
		
	}*/
	
	
	
	/**
	 * 真正出库时候调用
	 * @param sos
	 * @throws Exception 
	 */
	/*public void outStock(List<SalesOrderStock> sosList) throws Exception{
		
		SalesOrderStock _sos;
		
		for(SalesOrderStock sos:sosList ){
			
			_sos=getSalesOrderStock(sos.getSalesOrderSubCode(), sos.getProductId(), sos.getPartName());
			
			_sos.setTrayCount(_sos.getTrayCount()-sos.getTrayCount());
			_sos.setUsedTrayCount(_sos.getUsedTrayCount()-sos.getTrayCount());
			
			if(_sos.getTrayCount()<0){
				if(_sos.getUsedTrayCount()>_sos.getTrayCount()){
					throw new Exception("订单:"+sos.getSalesOrderSubCode()+"产品"+sos.getFactoryProductName()+"库存不足");
				}
			}
			update(_sos);
		}
		
	}*/
	
	/*public void giveBackStock(List<SalesOrderStock> sosList) throws Exception{

		SalesOrderStock _sos;
		
		for(SalesOrderStock sos:sosList ){
			
			_sos=getSalesOrderStock(sos.getSalesOrderSubCode(), sos.getProductId(), sos.getPartName());
			
			_sos.setUsedTrayCount(_sos.getUsedTrayCount()+sos.getTrayCount());
			
			if(_sos.getTrayCount()<0){
				if(_sos.getUsedTrayCount()>_sos.getTrayCount()){
					throw new Exception("订单:"+sos.getSalesOrderSubCode()+"产品"+sos.getFactoryProductName()+"库存不足");
				}
			}
			
			update(_sos);
		}
	}*/
	
	
	/**
	 * 根据订单号，产品ID，部件名称，查询订单库存信息
	 * @param salesOrderSubCode
	 * @param productId
	 * @param partName
	 * @return
	 */
	/*public SalesOrderStock  getSalesOrderStock(String salesOrderSubCode,Long productId,String partName){
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("salesOrderSubCode", salesOrderSubCode);
		map.put("productId", productId);
		map.put("closed", null);
		if(partName!=null){
			map.put("partName", partName);
		}
		
		return findUniqueByMap(SalesOrderStock.class, map);
	}*/
	
	

}
