/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.produce.service;

import com.bluebirdme.mes.btwManager.entity.BtwFile;
import com.bluebirdme.mes.core.base.service.IBaseService;
import com.bluebirdme.mes.produce.entity.FinishedProduct;

import java.util.List;
import java.util.Map;

/**
 * @author 徐波
 * @Date 2016-11-26 23:01:35
 */
public interface IFinishedProductPrintRecordService extends IBaseService {

    public List<Map<String, Object>> findFinishedProductPrintRecords(Long productId) throws Exception;

    public boolean saveFinishedProductPrintRecords(FinishedProduct finishedProduct, String userId);
}
