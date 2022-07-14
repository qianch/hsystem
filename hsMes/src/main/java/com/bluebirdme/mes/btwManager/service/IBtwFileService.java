/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.btwManager.service;

import com.bluebirdme.mes.btwManager.entity.BtwFile;
import com.bluebirdme.mes.core.base.service.IBaseService;
import com.bluebirdme.mes.store.entity.IBarcode;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author 徐波
 * @Date 2016-11-26 23:01:35
 */
public interface IBtwFileService extends IBaseService {
    String queryBtwFile(String weavePlanId, String type) throws Exception;

    List<Map<String, Object>> getBtwFilebyCustomerId(String customerId, String type, Boolean hasStandard) throws Exception;

    String editBacode(IBarcode iBarcode, Integer customerBarCodeRecord, Integer agentBarCodeRecord, long btwfileId);

    String clearBacode(IBarcode iBarcode) throws Exception;

    String saveBtwFilePrints(BtwFile btwFile, String userId) throws Exception;

    String importbtwFileUpload(MultipartFile file, long btwFileId, String userId, HttpServletRequest request) throws Exception;
}
