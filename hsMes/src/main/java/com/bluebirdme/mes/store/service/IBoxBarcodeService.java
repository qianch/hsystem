/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.store.service;

import com.bluebirdme.mes.core.base.service.IBaseService;

import java.util.List;
import java.util.Map;

/**
 * @author 徐波
 * @Date 2016-11-14 14:37:30
 */
public interface IBoxBarcodeService extends IBaseService {
    String clearBox(String ids) throws Exception;

    String editBacode(long id, Integer customerBarCodeRecord, Integer agentBarCodeRecord, long btwfileId);

    List<Map<String, Object>> findMaxBoxBarCodeCount();
}
