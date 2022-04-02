/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.siemens.order.dao;

import java.util.List;
import java.util.Map;

import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.siemens.barcode.entity.FragmentBarcode;
import com.bluebirdme.mes.siemens.order.entity.CutTaskDrawings;
import com.bluebirdme.mes.siemens.order.entity.CutTaskOrderDrawings;
/**
 * 
 * @author 高飞
 * @Date 2017-7-31 17:04:13
 */

public interface ICutTaskOrderDao extends IBaseDao {
	public String getSerial();
	public List<CutTaskDrawings> getCutTaskDrawings(Long ctId);
	public void deleteTask(String id) throws Exception;
	public void close(String id,Integer closed) throws Exception;
	public List<CutTaskOrderDrawings> getDrawings(String[] drawingsNo,Long ctoId);
	public FragmentBarcode getLatestFragmentBarcode(String preffix);
	public void updatePrintedCount(Map<String,Integer> list,List<Long> dwIds,Long ctId,Long ctoId) throws Exception;
	public int getTotalPrintedCount(Long ctoId);
	public int[] getSuitCountPerDrawings(String[] drawingsNo,Long ctoId);
	public String getDrawingNo(Long ctoId);
}
