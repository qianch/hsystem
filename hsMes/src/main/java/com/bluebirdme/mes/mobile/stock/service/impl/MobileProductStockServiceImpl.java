package com.bluebirdme.mes.mobile.stock.service.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.bluebirdme.mes.core.annotation.AnyExceptionRollback;
import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.mobile.stock.dao.IMobileProductStockDao;
import com.bluebirdme.mes.mobile.stock.dao.IMobileTurnBagDao;
import com.bluebirdme.mes.mobile.stock.service.IMobileProductStockService;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

@Service
@AnyExceptionRollback
public  class MobileProductStockServiceImpl extends BaseServiceImpl implements IMobileProductStockService {

	@Resource IMobileProductStockDao productStockDao ;

	@Override
	protected IBaseDao getBaseDao() {
		return productStockDao;
	}

	@Override
	public <T> Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
		return null;
	}

	@Override
	public String findDeliveryPlanDetail(String project, String content) throws Exception {
		return  productStockDao.findDeliveryPlanDetail(project,content);
	}

	@Override
	public String findDeliveryPlanById(String id,String pn) throws Exception {
		return productStockDao.findDeliveryPlanById(id,pn);
	}

	@Override
	public String findDeliveryPlanProductById(String id,String pn) throws Exception {

		return productStockDao.findDeliveryPlanProductById(id,pn);
	}

	@Override
	public List<Map<String, Object>> searchProduct(String salesOrderSubCode,
			String batchCode, Long productId, Long partId) throws Exception{
		return productStockDao.searchProduct(salesOrderSubCode, batchCode, productId, partId);
	}

	@Override
	public String findTrayByBarCode(String barcode) throws Exception {
		List<Map<String, Object>> tray = productStockDao.findTrayByBarCode(barcode);

		List<Map<String, Object>> part = productStockDao.findPartByBarCode(barcode);
		tray.addAll(part);
		return GsonTools.toJson(tray);
	}
}
