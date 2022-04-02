/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.cut.cutTcBom.service;

import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.IBaseService;
import com.bluebirdme.mes.cut.cutTcBom.entity.CutTcBomMain;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * @author 季晓龙
 * @Date 2018-8-20 11:10:00
 */
public interface ICutTcBomMainService extends IBaseService {

    /**
     * 根据mainId查询裁剪图纸bom明细
     *
     * @param mainId
     * @return
     */
    public List<Map<String, Object>> findCutTcBomDetailByMainId(Long mainId);

    /**
     * 保存裁剪套材bom信息
     * importCutTcBomDetailPartMainUploadFile
     *
     * @param cutTcBomMain
     * @return
     */
    public String saveCutTcBomMain(CutTcBomMain cutTcBomMain, String userId) throws Exception;

    /**
     * 导入裁剪套才图纸
     *
     * @param file 导入的文件
     * @return
     */
    public String importCutTcBomMainUploadFile(MultipartFile file, String userId) throws Exception;

    /**
     * 根据裁剪套才bomid导出裁剪套才Excel
     *
     * @param id
     * @throws Exception
     */
    public SXSSFWorkbook exportcutTcBomMain(Long id) throws Exception;

    /**
     * 根据搜索条件获取裁剪图纸bom内容
     *
     * @param data
     * @throws Exception
     */
    public List<Map<String, Object>> getCutBomJson(String data);

}
