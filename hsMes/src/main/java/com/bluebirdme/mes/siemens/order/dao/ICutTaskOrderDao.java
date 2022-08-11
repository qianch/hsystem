/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.siemens.order.dao;

import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.siemens.barcode.entity.FragmentBarcode;
import com.bluebirdme.mes.siemens.order.entity.CutTaskDrawings;
import com.bluebirdme.mes.siemens.order.entity.CutTaskOrderDrawings;

import java.util.List;
import java.util.Map;

/**
 * @author 高飞
 * @Date 2017-7-31 17:04:13
 */

public interface ICutTaskOrderDao extends IBaseDao {
    String getSerial();

    List<CutTaskDrawings> getCutTaskDrawings(Long ctId);

    void deleteTask(String id) throws Exception;

    void close(String id, Integer closed) throws Exception;

    List<CutTaskOrderDrawings> getDrawings(String[] drawingsNo, Long ctoId);

    FragmentBarcode getLatestFragmentBarcode(String preffix);

    void updatePrintedCount(Map<String, Integer> list, List<Long> dwIds, Long ctId, Long ctoId) throws Exception;

    int getTotalPrintedCount(Long ctoId);

    int[] getSuitCountPerDrawings(String[] drawingsNo, Long ctoId);

    String getDrawingNo(Long ctoId);
}
