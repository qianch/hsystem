/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.store.service;

import com.bluebirdme.mes.core.base.service.IBaseService;
import com.bluebirdme.mes.core.sql.SQLTemplateException;

import java.util.List;
import java.util.Map;

/**
 * @author 肖文彬
 * @Date 2016-9-29 15:45:32
 */
public interface IWarehouseService extends IBaseService {
    void delete(String ids);

    void updateS(String ids);

    List<Map<String, Object>> warehouse();

    List<Map<String, Object>> queryWarehousebyType(String waretype) throws SQLTemplateException;
}
