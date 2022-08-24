/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.store.dao;

import com.bluebirdme.mes.core.base.dao.IBaseDao;

import java.util.List;
import java.util.Map;

/**
 * @author 肖文彬
 * @Date 2016-10-14 9:48:31
 */

public interface ITrayDao extends IBaseDao {
    /**
     * 查询箱和卷的信息
     */
    List<Map<String, Object>> findBoxRoll(String code);

    /**
     * 查询卷的信息
     */
    List<Map<String, Object>> findRoll(String code);
}
