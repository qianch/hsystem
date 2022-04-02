/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.baseInfo.dao;

import java.util.Map;

import com.bluebirdme.mes.core.base.dao.IBaseDao;

import org.springframework.stereotype.Repository;
/**
 * 
 * @author 高飞
 * @Date 2016-10-12 11:06:09
 */

public interface IMaterialDao extends IBaseDao {
	public Map<String,Object> materialInfo(String code);
}
