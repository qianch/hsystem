/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.complaint.service;

import com.bluebirdme.mes.core.base.service.IBaseService;

/**
 * @author 高飞
 * @Date 2016-11-25 15:40:05
 */
public interface IComplaintService extends IBaseService {
    public int getSerial(String code, String year);
}
