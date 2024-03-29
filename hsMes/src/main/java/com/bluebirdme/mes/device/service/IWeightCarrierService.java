/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.device.service;

import com.bluebirdme.mes.core.base.service.IBaseService;
import com.bluebirdme.mes.device.entity.WeightCarrier;

import java.util.List;

/**
 * @author 孙利
 * @Date 2017-7-10 8:44:34
 */
public interface IWeightCarrierService extends IBaseService {
    List<String> getWeightCodes();


    WeightCarrier findByCode(String carrierCode);

    WeightCarrier findCarrierById(int id);
}
