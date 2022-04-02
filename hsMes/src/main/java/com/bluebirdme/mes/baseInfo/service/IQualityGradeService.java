/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.baseInfo.service;

import java.util.HashMap;
import java.util.List;

import com.bluebirdme.mes.core.base.service.IBaseService;

/**
 * 
 * @author 高飞
 * @Date 2016-10-12 10:34:41
 */
public interface IQualityGradeService extends IBaseService {
	public List<HashMap<String,Object>> getQualitySelections();
}
