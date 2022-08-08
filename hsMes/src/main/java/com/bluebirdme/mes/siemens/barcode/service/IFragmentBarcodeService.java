/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.siemens.barcode.service;

import com.bluebirdme.mes.core.base.service.IBaseService;

import java.util.List;
import java.util.Map;

/**
 * @author 高飞
 * @Date 2017-8-3 20:38:40
 */
public interface IFragmentBarcodeService extends IBaseService {
    void extraPrint(String barcodes, String printer, String user, String reason) throws Exception;

    void suit(String ctoCode, String part, String fragments, String user, String device) throws Exception;

    List<String> getFeedingFarbic(Long cutId);

    List<Map<String, Object>> getSuitInfo(String code);
}
