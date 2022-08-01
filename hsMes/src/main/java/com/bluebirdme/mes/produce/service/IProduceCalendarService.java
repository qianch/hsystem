/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.produce.service;

import com.bluebirdme.mes.core.base.service.IBaseService;

import java.util.List;
import java.util.Map;

/**
 * @author 高飞
 * @Date 2016-11-1 13:05:53
 */
public interface IProduceCalendarService extends IBaseService {
    List<Map<String, Object>> findList(String start, String end);
}
