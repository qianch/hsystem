/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.siemens.bom.service;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import com.bluebirdme.mes.baseInfo.entity.FtcBom;
import com.bluebirdme.mes.baseInfo.entity.FtcBomVersion;
import com.bluebirdme.mes.core.base.service.IBaseService;
import com.bluebirdme.mes.core.excel.ExcelContent;
import com.bluebirdme.mes.core.excel.ExcelImportMessage;
import com.bluebirdme.mes.siemens.bom.entity.Fragment;
import com.bluebirdme.mes.siemens.bom.entity.Grid;

/**
 * 
 * @author 高飞
 * @Date 2017-7-19 16:13:04
 */
public interface IFragmentService extends IBaseService {
	public void saveFragmentList(Grid<Fragment> grid);
	public List<Fragment> fragmentList(Long tcBomId);
	/**
	 * Excel西门子裁片导入保存
	 * @param fileId
	 * @param tcBomId
	 * @return
	 * @throws Exception
	 */
	public ExcelImportMessage fragmentImport(Long fileId,Long tcBomId) throws Exception;
	/**
	 * Excel裁剪图纸BOM导入保存
	 * @param fileId
	 * @param paryId
	 * @return
	 * @throws Exception
	 */
	public ExcelImportMessage drawingsImport(Long fileId,Long tcBomId,String partId ) throws Exception;
}
