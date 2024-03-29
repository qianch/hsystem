/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.siemens.bom.service;

import com.bluebirdme.mes.core.base.service.IBaseService;
import com.bluebirdme.mes.siemens.bom.entity.Grid;
import com.bluebirdme.mes.siemens.bom.entity.Suit;

import java.util.List;
import java.util.Map;

/**
 * @author 高飞
 * @Date 2017-7-20 13:16:40
 */
public interface ISuitService extends IBaseService {
    List<Map<String, Object>> suitList(Long partId);

    void saveSuitGird(Grid<Suit> grid);
}
