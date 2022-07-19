/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.mobile.common.dao;

import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.mobile.common.entity.AppVersion;

/**
 * @author 高飞
 * @Date 2016-11-6 10:22:52
 */

public interface IAppVersionDao extends IBaseDao {
    void save(AppVersion app);
}
