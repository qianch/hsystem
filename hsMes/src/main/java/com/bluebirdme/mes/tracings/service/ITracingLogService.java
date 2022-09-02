/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.tracings.service;

import com.bluebirdme.mes.core.base.service.IBaseService;

import java.util.List;
import java.util.Map;

/**
 * @author 徐波
 * @Date 2016-11-30 14:03:19
 */
public interface ITracingLogService extends IBaseService {
    List<Map<String, Object>> findByCondition(Map<String, Object> map) throws Exception;

    List<Map<String, Object>> findByTraceBackTo(String rollbarcode);
}
