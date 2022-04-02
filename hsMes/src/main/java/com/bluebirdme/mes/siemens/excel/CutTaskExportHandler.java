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
import com.bluebirdme.mes.siemens.order.service.ICutTaskService;

/**
 * 裁剪任务单导出
 * @author Goofy
 * @Date 2017年7月27日 下午2:46:04
 */
public class CutTaskExportHandler extends ExcelExportHandler {
	private static Logger logger = LoggerFactory.getLogger(CutTaskExportHandler.class);
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
		String[] titles={"启用状态","任务单编号","订单号","部件名称","批次号","客户简称","客户大类","发货日期","总套数","已派工套数","已打包套数","完成情况","创建时间","创建人"};
		excel.setTitles(Arrays.asList(titles));
		ICutTaskService cutService=(ICutTaskService) SpringCtx.getBean("cutTaskServiceImpl");
		
		Page page=new Page();
		page.setAll(1);
		
		try {
			Map<String,Object> result=cutService.findPageInfo(filter, page);
			List<String[]> data=new ArrayList<String[]>();
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
				rowData[1]=MapUtils.getAsStringIgnoreCase(row, "TASKCODE");
				rowData[2]=MapUtils.getAsStringIgnoreCase(row, "ORDERCODE");
				rowData[3]=MapUtils.getAsStringIgnoreCase(row, "PARTNAME");
				rowData[4]=MapUtils.getAsStringIgnoreCase(row, "BATCHCODE");
				rowData[5]=MapUtils.getAsStringIgnoreCase(row, "CONSUMERSIMPLENAME");
				rowData[6]=MapUtils.getAsStringIgnoreCase(row, "CONSUMERCATEGORY");
				if(MapUtils.getAsStringIgnoreCase(row, "CONSUMERCATEGORY").equals("1")){
					rowData[6]="国内";
				}else{
					rowData[6]="国外";
				}
				rowData[7]=MapUtils.getAsStringIgnoreCase(row, "DELIVERYDATE").substring(0,10);
				rowData[8]=MapUtils.getAsStringIgnoreCase(row, "SUITCOUNT");
				rowData[9]=MapUtils.getAsStringIgnoreCase(row, "ASSIGNSUITCOUNT");
				rowData[10]=MapUtils.getAsStringIgnoreCase(row, "PACKEDSUITCOUNT");
				rowData[11]=MapUtils.getAsStringIgnoreCase(row, "ISCOMPLETE");
				if(MapUtils.getAsStringIgnoreCase(row, "ISCOMPLETE").equals("0")){
					rowData[11]="未完成";
				}else{
					rowData[11]="已完成";
				}
				rowData[12]=MapUtils.getAsStringIgnoreCase(row, "CREATETIME").substring(0,10);
				rowData[13]=MapUtils.getAsStringIgnoreCase(row, "CREATEUSERNAME");
				
				data.add(rowData);
			}
			excel.setData(data);
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage(),e);
		}
		return excel;
	}
}
