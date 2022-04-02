/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.mobile.produce.service;

import java.util.Map;

import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.IBaseService;
import com.bluebirdme.mes.mobile.produce.entity.FeedingRecord;
import com.bluebirdme.mes.printer.entity.MyException;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

/**
 * 
 * @author 徐波
 * @Date 2016-11-8 14:03:33
 */
public interface IFeedingRecordService extends IBaseService {
	public void add(FeedingRecord feedingRecord)throws MyException;
	public <T> Map<String, Object>  findPageInfo2(Filter filter, Page page)throws Exception;
	public SXSSFWorkbook getForceMaterialExport1(Filter filter) throws Exception;
	public SXSSFWorkbook getweaveExport1(Filter filter) throws Exception;
}
