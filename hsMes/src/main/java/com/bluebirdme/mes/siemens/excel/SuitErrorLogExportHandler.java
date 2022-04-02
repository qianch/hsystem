package com.bluebirdme.mes.siemens.excel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xdemo.superutil.j2se.MapUtils;

import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.excel.ExcelContent;
import com.bluebirdme.mes.core.excel.ExcelExportHandler;
import com.bluebirdme.mes.core.spring.SpringCtx;
import com.bluebirdme.mes.siemens.barcode.service.ISuitErrorLogService;
import com.bluebirdme.mes.siemens.bom.service.ISuitService;

/**
 * @author Goofy
 * @Date 2017年8月4日 下午12:21:18
 */
public class SuitErrorLogExportHandler extends ExcelExportHandler {
	private static Logger logger = LoggerFactory.getLogger(SuitErrorLogExportHandler.class);
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
		ExcelContent excel = new ExcelContent();
		String[] titles = {"条码号","打印类型","任务单号","订单号","批次号","派工单号","客户简称","客户大类","部件名称","小部件名称","组套任务单号","组套订单号","组套批次号","组套派工单号","错误信息","扫描时间","操作人","部件条码"};
		excel.setTitles(Arrays.asList(titles));
		ISuitErrorLogService suitErrorLogService = (ISuitErrorLogService) SpringCtx.getBean("suitErrorLogServiceImpl");

		Page page = new Page();
		page.setAll(1);

		try {
			Map<String, Object> result = suitErrorLogService.findPageInfo(filter, page);
			List<String[]> data = new ArrayList<String[]>();
			List<Map<String, Object>> rows = (List<Map<String, Object>>) result.get("rows");
			String[] rowData;
			for (Map<String, Object> row : rows) {
				rowData = new String[titles.length];
				rowData[0] = MapUtils.getAsStringIgnoreCase(row, "FRAGMENTBARCODE");
				rowData[1] = MapUtils.getAsStringIgnoreCase(row, "FRAGMENTPRINTTYPE");
				rowData[2] = MapUtils.getAsStringIgnoreCase(row, "FRAGMENTCTCODE");
				rowData[3] = MapUtils.getAsStringIgnoreCase(row, "FRAGMENTORDERCODE");
				rowData[4] = MapUtils.getAsStringIgnoreCase(row, "FRAGMENTBATCHCODE");
				rowData[5] = MapUtils.getAsStringIgnoreCase(row, "FRAGMENTCTOCODE");
				rowData[6] = MapUtils.getAsStringIgnoreCase(row, "FRAGMENTCONSUMERSIMPLENAME");
				rowData[7] = MapUtils.getAsStringIgnoreCase(row, "FRAGMENTCONSUMERCATEGORY");
				rowData[8] = MapUtils.getAsStringIgnoreCase(row, "PARTNAME");
				rowData[9] = MapUtils.getAsStringIgnoreCase(row, "FRAGMENTNAME");
				rowData[10] = MapUtils.getAsStringIgnoreCase(row, "SUITCTCODE");
				rowData[11] = MapUtils.getAsStringIgnoreCase(row, "SUITORDERCODE");
				rowData[12] = MapUtils.getAsStringIgnoreCase(row, "SUITBATCHCODE");
				rowData[13] = MapUtils.getAsStringIgnoreCase(row, "SUITCTOCODE");
				rowData[14] = MapUtils.getAsStringIgnoreCase(row, "ERRORMSG");
				rowData[15] = MapUtils.getAsStringIgnoreCase(row, "SCANTIME");
				rowData[16] = MapUtils.getAsStringIgnoreCase(row, "SCANUSER");
				rowData[17] = MapUtils.getAsStringIgnoreCase(row, "PARTBARCODE");

				data.add(rowData);
			}
			excel.setData(data);
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage(),e);
		}
		return excel;
	}
}
