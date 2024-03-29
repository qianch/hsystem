/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.stock.dao;

import com.bluebirdme.mes.core.base.dao.IBaseDao;

import java.util.List;
import java.util.Map;

/**
 * @author 肖文彬
 * @Date 2016-11-8 15:25:19
 */

public interface IStockCheckDao extends IBaseDao {
    /**
     * 根据盘库记录id查讯结果
     */
    List<Map<String, Object>> findR(String id);
}
