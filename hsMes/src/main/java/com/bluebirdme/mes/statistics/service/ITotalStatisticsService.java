/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.statistics.service;

import java.util.List;
import java.util.Map;

import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.IBaseService;
import com.bluebirdme.mes.statistics.entity.TotalStatistics;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

/**
 *
 * @author 徐波
 * @Date 2016-11-26 14:44:04
 */
public interface ITotalStatisticsService extends IBaseService {
	public <T> Map<String, Object> findPageInfoByBox(Filter filter, Page page) throws Exception;
	public <T> Map<String, Object> findPageInfoByTray(Filter filter, Page page) throws Exception;
	public <T> Map<String, Object> findPageInfoByPart(Filter filter, Page page) throws Exception;
	public <T> Map<String, Object> findPageInfoByRoll(Filter filter, Page page) throws Exception;
	public void saveLockState(List<TotalStatistics> list,String complaintCode,String reasons);
	public void saveUnLockState(List<TotalStatistics> list,String complaintCode,String reasons);
	public List<TotalStatistics> getByIds(String ids);
	public void iflockByBarcode(String barcodes,int state);
	public void quality(String barcodes,String state) throws Exception;
	public Integer isFrozen(String barcode);
	public boolean isPackaged(String barcode);
	public void changeInfo(String barcode,String parentBarocde,String topBarcode,Double newWeight)throws Exception;
	public void judge(String ids, String qualityGrade)throws Exception;

	/**
	 * 车间生产汇总(车间生产统计(产品大类、订单号、次号、车间))
	 * @param filter
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> productsShopSummary(Filter filter, Page page)throws Exception;

	/**
	 * 车间生产统计（产品大类、厂内名称）
	 * @param filter
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> genericFactorySummary(Filter filter, Page page)throws Exception;

	/**
	 * 生产领料汇总
	 * @param filter
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getPickingStatistics(Filter filter, Page page) throws Exception;

	public String TopBarcode(String barcode) throws Exception;

	public SXSSFWorkbook exportDailyStatistics(Filter filter, String searchType) throws Exception;
}
