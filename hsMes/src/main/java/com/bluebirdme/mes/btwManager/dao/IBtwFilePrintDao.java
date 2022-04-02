/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.btwManager.dao;

import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.sql.SQLTemplateException;

import java.util.List;
import java.util.Map;

/**
 *
 * @author 徐波
 * @Date 2016-11-26 23:01:35
 */

public interface IBtwFilePrintDao extends IBaseDao {


    public List<Map<String, Object>> findBtwFilePrints(Long btwFileId) throws Exception;
}
