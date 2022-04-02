/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.common.service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import com.bluebirdme.mes.core.base.service.IBaseService;

/**
 * 
 * @author 高飞
 * @Date 2017-1-5 18:10:36
 */
public interface ICommonService extends IBaseService {
	public List<Map<String, Object>> getMsg();
	public void close(String ids, String type) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException ;
}
