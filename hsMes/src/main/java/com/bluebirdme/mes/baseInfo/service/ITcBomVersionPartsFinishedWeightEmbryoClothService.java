/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.baseInfo.service;

import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.IBaseService;

import java.util.Map;

/**
 * 
 * @author 徐秦冬
 * @Date 2017-11-27 18:57:36
 */
public interface ITcBomVersionPartsFinishedWeightEmbryoClothService extends IBaseService {
	public void delete(String ids);

 Map<String, Object> findPageInfo1(Filter filter, Page page);
}
