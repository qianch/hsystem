/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.sales.service;

import com.bluebirdme.mes.core.base.service.IBaseService;

/**
 * 
 * @author 肖文彬
 * @Date 2016-9-28 11:24:47
 */
public interface IConsumerService extends IBaseService {
     public void delete(String ids);
     public void old(String ids);
}
