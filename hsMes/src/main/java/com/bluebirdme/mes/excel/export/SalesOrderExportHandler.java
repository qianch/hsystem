package com.bluebirdme.mes.excel.export;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.excel.ExcelContent;
import com.bluebirdme.mes.core.excel.ExcelExportHandler;
import com.bluebirdme.mes.core.spring.SpringCtx;
import com.bluebirdme.mes.sales.entity.SalesOrderDetail;
import com.bluebirdme.mes.sales.service.ISalesOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 销售订单导出功能
 * @author 宋黎明
 * @Date 2016年11月26日 下午12:43:28
 */
public class SalesOrderExportHandler extends ExcelExportHandler {
	private static Logger logger = LoggerFactory.getLogger(SalesOrderExportHandler.class);
	@Override
	public ExcelContent getContent() {
		return null;
	}

	@Override
	public ExcelContent getContent(String[] id) {
		return null;
	}

	@Override
	public ExcelContent getContent(Filter filter) {
		ExcelContent content=new ExcelContent();
		String[] titles = new String[] { "订单号", "下单日期", "客户名称", "业务员", "内销/外销","订单类型","订单总金额","发货时间","定单审核状态","订单状态", "订单备注"};

		List<String> titleList = new ArrayList();
		Collections.addAll(titleList, titles);
		content.setTitles(new ArrayList<String>(titleList));
		
		ISalesOrderService service = (ISalesOrderService) SpringCtx.getBean("salesOrderServiceImpl");
		
		Page page = new Page();
		page.setAll(1);
		
		try {
			List<Map<String, Object>> rs = (List<Map<String, Object>>) service.findPageInfo(filter, page).get("rows");
			List<String[]> data = new ArrayList<String[]>();
			Map<String,Object> smap=new HashMap<String,Object>();
			String[] temp;
			String[] temp1;
			for (Map<String, Object> map : rs) {
				temp = new String[11];
				temp[0] = map.get("SALESORDERCODE").toString();
				temp[1] = map.get("SALESORDERDATE").toString();
				temp[2] = map.get("CONSUMERNAME").toString();
				temp[3] = map.get("USERNAME").toString();
				temp[4] = (int)map.get("SALESORDERISEXPORT")==1?"内销":"外销";
				temp[5] = (int)map.get("SALESORDERTYPE")==-1?"未知":(int)map.get("SALESORDERTYPE")==1?"常规产品":(int)map.get("SALESORDERTYPE")==2?"试样":"新品";
				temp[6] = map.get("SALESORDERTOTALMONEY")==null?"":map.get("SALESORDERTOTALMONEY").toString();
				temp[7] = map.get("SALESORDERDELIVERYTIME")==null?"":map.get("SALESORDERDELIVERYTIME").toString();
				temp[8] = (int)map.get("AUDITSTATE")==-1?"未通过":(int)map.get("AUDITSTATE")==1?"审核中":(int)map.get("AUDITSTATE")==2?"已通过":"未审核";
				temp[9] = (int)map.get("ISCLOSED")==1?"关闭":"未关闭";
				temp[10] = map.get("SALESORDERMEMO")==null?"":map.get("SALESORDERMEMO").toString();
					//遍历销售明细
					smap.put("salesOrderId", map.get("ID"));
					List<SalesOrderDetail> sodDetails=service.findListByMap(SalesOrderDetail.class, map);
					for(SalesOrderDetail sod :sodDetails){
						temp1 = new String[2];
						temp1[0]="";
						temp1[1]= sod.getConsumerProductName();
					}
				data.add(temp);
				content.setData(data);
			}
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage(),e);
		}
		return content;
	}
}
