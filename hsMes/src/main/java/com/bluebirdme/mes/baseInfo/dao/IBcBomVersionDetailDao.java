/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.baseInfo.dao;

import java.util.List;

import com.bluebirdme.mes.baseInfo.entity.BcBomVersionDetail;
import com.bluebirdme.mes.core.base.dao.IBaseDao;

import org.springframework.stereotype.Repository;
/**
 * 
 * @author 徐波
 * @Date 2016-10-8 16:53:24
 */

public interface IBcBomVersionDetailDao extends IBaseDao {
	public void delete(String ids) throws Exception ;
	public void deleteByPid();
}
