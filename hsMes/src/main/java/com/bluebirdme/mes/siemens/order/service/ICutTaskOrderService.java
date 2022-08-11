/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.siemens.order.service;

import com.bluebirdme.mes.core.base.service.IBaseService;
import com.bluebirdme.mes.siemens.order.entity.CutTaskDrawings;
import com.bluebirdme.mes.siemens.order.entity.CutTaskOrder;
import com.bluebirdme.mes.siemens.order.entity.CutTaskOrderDrawings;

import java.util.List;

/**
 * @author 高飞
 * @Date 2017-7-31 17:04:13
 */
public interface ICutTaskOrderService extends IBaseService {
    String getSerial();

    List<CutTaskDrawings> getCutTaskDrawings(Long ctId);

    void save(CutTaskOrder cto) throws Exception;

    void deleteTask(String id) throws Exception;

    void close(String id, Integer closed) throws Exception;

    void printBarcode(Long ctoId, String[] drawingsNo, Integer suitCount, String printer, String cutPlanId, String user) throws Exception;

    void rePrint(Long ctoId, Long dwId, Integer rePrintCount, String printer, String user, String reason) throws Exception;

    int getTotalPrintedCount(Long ctoId);

    String getDrawingNo(Long ctoId);

    void print(String printer, String ip, Long ctoId, Long drawingId, String order, Integer levelCount) throws Exception;

    CutTaskOrderDrawings next(String ip, String ctoCode, String drawingNo);
}
