/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.siemens.order.service;

import com.bluebirdme.mes.core.base.service.IBaseService;
import com.bluebirdme.mes.siemens.order.entity.CutTask;

/**
 * @author 高飞
 * @Date 2017-7-26 10:56:16
 */
public interface ICutTaskService extends IBaseService {
    String getSerial();

    void save(CutTask task, Long pcId) throws Exception;

    void close(Integer closed, String id) throws Exception;

    int[] getSuitCountPerDrawings(Long partId, Long ctid);
}
