/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.mobile.common.service;

import com.bluebirdme.mes.core.base.service.IBaseService;
import com.bluebirdme.mes.mobile.common.entity.AppVersion;

/**
 * @author 高飞
 * @Date 2016-11-6 10:22:52
 */
public interface IAppVersionService extends IBaseService {
    void save(AppVersion app);
}
