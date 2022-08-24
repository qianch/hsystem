/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.store.dao;

import com.bluebirdme.mes.core.base.dao.IBaseDao;

/**
 * @author 肖文彬
 * @Date 2016-9-29 16:26:04
 */

public interface IWarehosePosDao extends IBaseDao {
    void delete(String ids);

    void updateS(String ids);
}
