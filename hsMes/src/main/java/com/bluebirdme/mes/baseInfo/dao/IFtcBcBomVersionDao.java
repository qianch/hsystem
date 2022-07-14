/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.baseInfo.dao;

import com.bluebirdme.mes.baseInfo.entity.FtcBcBomVersion;
import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.sql.SQLTemplateException;

import java.util.List;

/**
 * @author 徐秦冬
 * @Date 2017-12-6 16:26:52
 */

public interface IFtcBcBomVersionDao extends IBaseDao {
    void delete(String ids) throws Exception;

    void deleteByPid(String ids);

    List<FtcBcBomVersion> getFtcBcBomJson(String id, String productType) throws SQLTemplateException;
}
