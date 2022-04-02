/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.siemens.order.service;

import java.util.List;

import com.bluebirdme.mes.core.base.service.IBaseService;
import com.bluebirdme.mes.siemens.order.entity.CutTaskDrawings;
import com.bluebirdme.mes.siemens.order.entity.CutTaskOrder;
import com.bluebirdme.mes.siemens.order.entity.CutTaskOrderDrawings;

/**
 * 
 * @author 高飞
 * @Date 2017-7-31 17:04:13
 */
public interface ICutTaskOrderService extends IBaseService {
	public String getSerial();
	public List<CutTaskDrawings> getCutTaskDrawings(Long ctId);
	public void save(CutTaskOrder cto) throws Exception;
	public void deleteTask(String id) throws Exception;
	public void close(String id,Integer closed) throws Exception;
	public void printBarcode(Long ctoId,String[] drawingsNo, Integer suitCount, String printer,String cutPlanId ,String user) throws Exception;
	public void rePrint(Long ctoId,Long dwId,Integer rePrintCount,String printer,String user,String reason) throws Exception;
	public int getTotalPrintedCount(Long ctoId);
	public String getDrawingNo(Long ctoId);
	public void print(String printer,String ip, Long ctoId, Long drawingId, String order, Integer levelCount) throws Exception;
	public CutTaskOrderDrawings next(String ip,String ctoCode,String drawingNo);
}
