/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.cut.cutTcBom.service;

import com.bluebirdme.mes.core.base.service.IBaseService;
import com.bluebirdme.mes.cut.cutTcBom.entity.CutTcBomMain;
import com.bluebirdme.mes.cut.cutTcBom.entity.CutTcBomPartDetail;
import com.bluebirdme.mes.cut.cutTcBom.entity.CutTcBomPartMain;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 *
 * @author 季晓龙
 * @Date 2018-8-20 11:10:00
 */
public interface ICutTcBomPartMainService extends IBaseService {


	/**
	 * 根据tcBomMainId查询裁片信息
	 *
	 * @param tcBomMainId
	 * @return
	 */
	public List<Map<String, Object>> findCutTcBomPartMainByTcBomMainId(Long tcBomMainId);

	/**
	 * 根据mainId查询裁片详情
	 *
	 * @param mainId
	 * @return
	 */
	public List<Map<String, Object>> findCutTcBomPartDetailByMainId(Long mainId);

	/**
	 * 保存裁片bom信息
	 * saveCutTcBomPartMain
	 *
	 * @param cutTcBomPartMain
	 * @return
	 */
	public String saveCutTcBomPartMain(CutTcBomPartMain cutTcBomPartMain, String userId) throws Exception;

	/**
	 * 保存裁片bom明细
	 * saveCutTcBomPartDetail
	 *
	 * @param cutTcBomPartDetail
	 * @return
	 */
	public String saveCutTcBomPartDetail(CutTcBomPartDetail cutTcBomPartDetail, String userId) throws Exception;

	/**
	 * 导入裁剪裁片文件
	 *
	 * @param file 导入的文件
	 * @return
	 */
	public String importCutTcBomPartMainUploadFile(MultipartFile file, String userId) throws Exception;

	/**
	 * 删除裁片
	 *
	 * @param ids 裁片id
	 * @return
	 */
	public String doDeletePartMain(String ids) throws Exception;

	/**
	 * 删除裁片明细
	 *
	 * @param ids 裁片明细id
	 * @return
	 */
	public String doDeletePartDetail(String ids) throws Exception;

	/**
	 * 导出质检确认表
	 *
	 * @param tcBomMainId 裁剪bomid
	 * @return
	 */
	public SXSSFWorkbook exportCutTcBomPart(Long tcBomMainId) throws Exception;
}
