/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.common.service;

import com.bluebirdme.mes.core.base.service.IBaseService;

import java.lang.reflect.InvocationTargetException;

/**
 * @author 高飞
 * @Date 2017-1-5 18:10:36
 */
public interface IFixService extends IBaseService {
    void fixRollNO(String[] batchCodes);

    void fixRollCountInTray();

    void produceToWeave(Long producePlanDetailId) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException;
}
