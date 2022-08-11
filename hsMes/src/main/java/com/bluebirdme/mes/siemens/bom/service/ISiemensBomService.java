/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.siemens.bom.service;

import com.bluebirdme.mes.core.base.service.IBaseService;

import java.util.List;
import java.util.Map;

/**
 * @author 高飞
 * @Date 2017-7-18 14:54:41
 */
public interface ISiemensBomService extends IBaseService {
    List<Map<String, Object>> findPageInfo(Boolean siemens, String code) throws Exception;

    List<Map<String, Object>> listAllParts(Long tcBomId);

    int enableOrDisable(Long partId, Integer enable, Boolean isDrawingsBom);
}
