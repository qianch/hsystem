/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.baseInfo.dao;

import com.bluebirdme.mes.core.base.dao.IBaseDao;

import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * 
 * @author 肖文彬
 * @Date 2016-10-9 16:11:41
 */

public interface ITcBomVersionPartsDao extends IBaseDao {
	//伪删除
		public void deleteParts(Long id);

 Map<String, Object> findPageInfo1(Filter filter, Page page);
}
