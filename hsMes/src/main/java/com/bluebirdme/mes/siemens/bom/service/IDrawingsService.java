/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.siemens.bom.service;

import java.util.List;
import java.util.Map;

import com.bluebirdme.mes.core.base.service.IBaseService;
import com.bluebirdme.mes.siemens.bom.entity.Drawings;
import com.bluebirdme.mes.siemens.bom.entity.Grid;

/**
 * 
 * @author 高飞
 * @Date 2017-7-18 14:06:57
 */
public interface IDrawingsService extends IBaseService {
	public List<Map<String,Object>> drawingsList(Long partId);
	public  void saveDrawingsList(Grid<Drawings> grid);
	public int[] getSuitCountPerDrawings(Long partId);
}
