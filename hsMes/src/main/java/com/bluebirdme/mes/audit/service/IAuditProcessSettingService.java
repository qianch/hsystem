/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.audit.service;

import com.bluebirdme.mes.audit.entity.AuditProcessSetting;
import com.bluebirdme.mes.core.base.service.IBaseService;

/**
 * @author 高飞
 * @Date 2016-10-24 14:51:44
 */
public interface IAuditProcessSettingService extends IBaseService {
    void saveAuditSetting(AuditProcessSetting setting);
}
