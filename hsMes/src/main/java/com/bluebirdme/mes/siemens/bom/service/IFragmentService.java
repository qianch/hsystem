/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.siemens.bom.service;

import com.bluebirdme.mes.core.base.service.IBaseService;
import com.bluebirdme.mes.core.excel.ExcelImportMessage;
import com.bluebirdme.mes.siemens.bom.entity.Fragment;
import com.bluebirdme.mes.siemens.bom.entity.Grid;

import java.util.List;

/**
 * @author 高飞
 * @Date 2017-7-19 16:13:04
 */
public interface IFragmentService extends IBaseService {
    void saveFragmentList(Grid<Fragment> grid);

    List<Fragment> fragmentList(Long tcBomId);

    /**
     * Excel西门子裁片导入保存
     */
    ExcelImportMessage fragmentImport(Long fileId, Long tcBomId) throws Exception;

    /**
     * Excel裁剪图纸BOM导入保存
     */
    ExcelImportMessage drawingsImport(Long fileId, Long tcBomId, String partId) throws Exception;
}
