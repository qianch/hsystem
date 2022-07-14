/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.baseInfo.dao;

import com.bluebirdme.mes.core.base.dao.IBaseDao;

/**
 * @author 徐秦冬
 * @Date 2017-12-6 16:26:52
 */

public interface IFtcBcBomVersionDetailDao extends IBaseDao {
    void delete(String ids) throws Exception;

    void deleteByPid();
}
