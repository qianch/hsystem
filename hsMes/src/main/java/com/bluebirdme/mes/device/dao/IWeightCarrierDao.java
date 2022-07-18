/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.device.dao;

import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.device.entity.WeightCarrier;

import java.util.List;

/**
 * @author 孙利
 * @Date 2017-7-10 8:44:34
 */

public interface IWeightCarrierDao extends IBaseDao {
    List<String> getWeightCodes();

    WeightCarrier getCarrierById(int id);

    WeightCarrier findByCode(String carrierCode);
}
