package com.bluebirdme.mes.excel.export;

import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.excel.ExcelContent;
import com.bluebirdme.mes.core.excel.ExcelExportHandler;

/**
 * 翻包导出功能
 * @author 宋黎明
 * @Date 2016年11月26日 下午12:43:28
 */
public class trunBagExportHandler extends ExcelExportHandler {

	@Override
	public ExcelContent getContent() {
		return null;
	}

	@Override
	public ExcelContent getContent(String[] id) {
		return null;
	}

	@Override
	public ExcelContent getContent(Filter filter) {/*
		ExcelContent content=new ExcelContent();
		String[] titles = new String[] { "翻包任务单号", "客户名称", "订单号", "批次号",
				"带翻包物料批次号","带翻包追溯号","翻包数量","库存位置","翻包部门","审核状态"};

		List<String> titleList = new ArrayList<String>();
		Collections.addAll(titleList, titles);
		content.setTitles(new ArrayList<String>(titleList));
		
		ITrunBagPlanService service = (ITrunBagPlanService) SpringCtx.getBean("trunBagPlanServiceImpl");
		
		Page page = new Page();
		page.setAll(1);
		
		try {
			List<Map<String, Object>> rs = (List<Map<String, Object>>) service.findPageInfo(filter, page).get("rows");
			List<String[]> data = new ArrayList<String[]>();
			String[] temp;
			for (Map<String, Object> map : rs) {
				temp = new String[10];
				temp[0] = map.get("TRUNBAGCODE").toString();
				temp[1] = map.get("CONSUMERNAME").toString();
				temp[2] = map.get("SALESORDERCODE")==null?"":map.get("SALESORDERCODE").toString();
				temp[3] = map.get("BATCHCODE")==null?"":map.get("BATCHCODE").toString();
				temp[4] = map.get("MATERIALBATCHCODE")==null?"":map.get("MATERIALBATCHCODE").toString();
				temp[5] = map.get("MATERIALREVIEWCODE")==null?"":map.get("MATERIALREVIEWCODE").toString();
				temp[6] = map.get("COUNT")==null?"":map.get("COUNT").toString();
				temp[7] = map.get("WAREHOUSENAME")==null?"":map.get("WAREHOUSENAME").toString();
				temp[8] = map.get("DEPTNAME")==null?"":map.get("DEPTNAME").toString();
				temp[9] = (int)map.get("AUDITSTATE")==-1?"未通过":(int)map.get("AUDITSTATE")==1?"审核中":(int)map.get("AUDITSTATE")==2?"已通过":"未审核";
				data.add(temp);
				content.setData(data);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return content;*/
		return null;
	}

}
