/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.printer.service.impl;

import com.bluebirdme.mes.audit.entity.AuditConstant;
import com.bluebirdme.mes.common.service.IMessageCreateService;
import com.bluebirdme.mes.core.annotation.AnyExceptionRollback;
import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.planner.deliveryontheway.dao.IDeliveryOnTheWayPlanDao;
import com.bluebirdme.mes.planner.deliveryontheway.entity.DeliveryOnTheWayPlan;
import com.bluebirdme.mes.planner.deliveryontheway.entity.DeliveryOnTheWayPlanDetails;
import com.bluebirdme.mes.planner.deliveryontheway.service.IDeliveryOnTheWayPlanService;
import com.bluebirdme.mes.printer.dao.IPrintTemplateDao;
import com.bluebirdme.mes.printer.service.IPrintTemplateService;
import com.bluebirdme.mes.sales.entity.SalesOrderDetail;
import com.bluebirdme.mes.stock.entity.ProductInRecord;
import com.bluebirdme.mes.stock.entity.ProductStockState;
import com.bluebirdme.mes.stock.service.IProductStockService;
import com.bluebirdme.mes.store.dao.ITrayBarCodeDao;
import com.bluebirdme.mes.store.entity.Tray;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * 
 * @author 徐波
 * @Date 2016-11-2 9:30:07
 */
@Service
@AnyExceptionRollback
public class PrintTemplateServiceImpl extends BaseServiceImpl implements IPrintTemplateService {

	@Resource
	IPrintTemplateDao printTemplateDao;



	@Override
	protected IBaseDao getBaseDao() {
		return printTemplateDao;
	}

	@Override
	public <T> Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {

		Map<String, Object> findPageInfo = printTemplateDao.findPageInfo(filter, page);
		return findPageInfo;
	}


}
