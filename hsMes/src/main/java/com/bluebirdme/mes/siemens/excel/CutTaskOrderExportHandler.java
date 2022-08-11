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
import com.bluebirdme.mes.siemens.order.service.ICutTaskOrderService;

/**
 * 裁剪派工单导出
 * @author Goofy
 * @Date 2017年7月27日 下午2:46:04
 */
public class CutTaskOrderExportHandler extends ExcelExportHandler {
	private static final Logger logger = LoggerFactory.getLogger(CutTaskOrderExportHandler.class);
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
		ExcelContent excel=new ExcelContent();
		String[] titles={"状态","派工单编号","组别","机长","任务单编号","订单号","部件名称","批次号","客户简称","客户大类","任务单套数","派工套数","已打包套数","发货日期","派工时间","派工人","完成情况"};
		excel.setTitles(Arrays.asList(titles));
		ICutTaskOrderService cutTaskOrderService=(ICutTaskOrderService) SpringCtx.getBean("cutTaskOrderServiceImpl");
		Page page=new Page();
		page.setAll(1);
		
		try {
			Map<String,Object> result=cutTaskOrderService.findPageInfo(filter, page);
			List<String[]> data= new ArrayList<>();
			List<Map<String,Object>> rows=(List<Map<String, Object>>) result.get("rows");
			String[] rowData;
			for(Map<String,Object> row:rows){
				rowData=new String[titles.length];
				rowData[0]=MapUtils.getAsStringIgnoreCase(row, "ISCLOSED");
				if(MapUtils.getAsStringIgnoreCase(row, "ISCLOSED").equals("0")){
					rowData[0]="启用";
				}else{
					rowData[0]="关闭";
				}
				rowData[1]=MapUtils.getAsStringIgnoreCase(row, "CTOCODE");
				rowData[2]=MapUtils.getAsStringIgnoreCase(row, "CTOGROUPNAME");
				rowData[3]=MapUtils.getAsStringIgnoreCase(row, "CTOGROUPLEADER");
				rowData[4]=MapUtils.getAsStringIgnoreCase(row, "TASKCODE");
				rowData[5]=MapUtils.getAsStringIgnoreCase(row, "ORDERCODE");
				rowData[6]=MapUtils.getAsStringIgnoreCase(row, "PARTNAME");
				rowData[7]=MapUtils.getAsStringIgnoreCase(row, "BATCHCODE");
				rowData[8]=MapUtils.getAsStringIgnoreCase(row, "CONSUMERSIMPLENAME");
				rowData[9]=MapUtils.getAsStringIgnoreCase(row, "CONSUMERCATEGORY");
				if(MapUtils.getAsStringIgnoreCase(row, "CONSUMERCATEGORY").equals("1")){
					rowData[9]="国内";
				}else{
					rowData[9]="国外";
				}
				rowData[10]=MapUtils.getAsStringIgnoreCase(row, "SUITCOUNT");
				rowData[11]=MapUtils.getAsStringIgnoreCase(row, "ASSIGNSUITCOUNT");
				rowData[12]=MapUtils.getAsStringIgnoreCase(row, "PACKEDSUITCOUNT");
				rowData[13]=MapUtils.getAsStringIgnoreCase(row, "DELIVERYDATE").substring(0,10);
				rowData[14]=MapUtils.getAsStringIgnoreCase(row, "CREATETIME").substring(0,10);
				rowData[15]=MapUtils.getAsStringIgnoreCase(row, "CREATEUSERNAME");
				rowData[16]=MapUtils.getAsStringIgnoreCase(row, "ISCOMPLETE");
				if(MapUtils.getAsStringIgnoreCase(row, "ISCOMPLETE").equals("0")){
					rowData[16]="未完成";
				}else{
					rowData[16]="已完成";
				}
				data.add(rowData);
			}
			excel.setData(data);
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage(),e);
		}
		return excel;
	}
}
