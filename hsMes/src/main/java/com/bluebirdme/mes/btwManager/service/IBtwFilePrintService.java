/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.btwManager.service;

import com.bluebirdme.mes.btwManager.entity.BtwFile;
import com.bluebirdme.mes.core.base.service.IBaseService;

import java.util.List;
import java.util.Map;

/**
 * @author 徐波
 * @Date 2016-11-26 23:01:35
 */
public interface IBtwFilePrintService extends IBaseService {

    List<Map<String, Object>> findBtwFilePrints(Long btwFileId) throws Exception;

    boolean saveBtwFilePrints(BtwFile btwFile, String userId);
}
