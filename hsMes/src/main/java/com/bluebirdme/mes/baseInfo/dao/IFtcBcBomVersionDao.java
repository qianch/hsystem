/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.baseInfo.dao;

import java.util.List;
import java.util.Map;

import com.bluebirdme.mes.baseInfo.entity.BCBomVersion;
import com.bluebirdme.mes.baseInfo.entity.FtcBcBomVersion;
import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.sql.SQLTemplateException;

import org.springframework.stereotype.Repository;
/**
 * 
 * @author 徐秦冬
 * @Date 2017-12-6 16:26:52
 */

public interface IFtcBcBomVersionDao extends IBaseDao {
	public void delete(String ids) throws Exception ;
	public void deleteByPid(String ids);
	public List<FtcBcBomVersion> getFtcBcBomJson(String id,String productType)throws SQLTemplateException;
}
