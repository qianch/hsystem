/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.produce.service;

import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.IBaseService;
import com.bluebirdme.mes.core.excel.ExcelContent;
import com.bluebirdme.mes.core.excel.ExcelImportMessage;
import com.bluebirdme.mes.core.sql.SQLTemplateException;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

/**
 * @author 宋黎明
 * @Date 2016-9-30 10:49:34
 */
public interface IFinishedProductService extends IBaseService {
    Map<String, Object> findPageInfo2(Filter filter, Page page) throws Exception;

    public void delete(String ids);

    //查找tcBom的信息
    List<Map<String, Object>> tcTree(String data) throws SQLTemplateException;

    //查找ftcBom的信息
    List<Map<String, Object>> ftcTree(String data) throws SQLTemplateException;

    //查找bcBom的信息
    List<Map<String, Object>> bcTree(String data) throws SQLTemplateException;

    int querySlBycode(String wlbh) throws SQLTemplateException;

    int queryCpggInfo(String wlbh) throws SQLTemplateException;


    int queryMfnewcode(String wlbh) throws SQLTemplateException;

    public int queryJznewcode(String wlbh) throws SQLTemplateException;

    int queryJcnewcode(String wlbh) throws SQLTemplateException;


    int queryJzcnewcode(String wlbh) throws SQLTemplateException;

    int querySlinfo(String wlbh) throws SQLTemplateException;

    String getzgmcbycode(String code) throws SQLTemplateException;


    void completeBomId() throws SQLTemplateException, IOException;

    //废弃状态
    void updates(Long id);

    Map<String, Object> findPageInfoDelivery(Filter filter, Page page) throws Exception;

    /**
     * 产成品汇总（按成品类别统计）
     *
     * @param filter
     * @param page
     * @return
     * @throws Exception
     */
    Map<String, Object> productsSummary(Filter filter, Page page) throws Exception;

    /**
     * 产成品汇总（按厂内名称）
     *
     * @param filter
     * @param page
     * @return
     * @throws Exception
     */
    Map<String, Object> productsFactorySummary(Filter filter, Page page) throws Exception;

    /**
     * 产成品汇总（订单号、批次号、厂内名称）
     *
     * @param filter
     * @param page
     * @return
     * @throws Exception
     */
    Map<String, Object> productsSundrySummary(Filter filter, Page page) throws Exception;

    /**
     * 产成品汇总(按客户统计)
     *
     * @param filter
     * @param page
     * @return
     * @throws Exception
     */
    Map<String, Object> productsCustomerStockSummary(Filter filter, Page page) throws Exception;

    /**
     * Excel导入保存
     *
     * @param content
     * @return
     * @throws NoSuchAlgorithmException
     */
    ExcelImportMessage saveFinishedProductFromExcel(ExcelContent content) throws NoSuchAlgorithmException;

    /**
     * 查询所有未作废的成品
     */

    List<Map<String, Object>> findAllFinishProduct() throws Exception;


    List<Map<String, Object>> checkYxInfo(String planCode) throws SQLTemplateException;

    List<Map<String, Object>> queryMfinfo(String wlbh, String mf) throws SQLTemplateException;

    List<Map<String, Object>> queryJZinfo(String wlbh, String jz) throws SQLTemplateException;

    List<Map<String, Object>> queryJCinfo(String wlbh, String jc) throws SQLTemplateException;

    List<Map<String, Object>> queryJZCinfo(String wlbh, String jz, String jc) throws SQLTemplateException;


    List<Map<String, Object>> queryGGinfo(String wlbh, String gg) throws SQLTemplateException;

    List<Map<String, Object>> queryBcinfoByBcbm(String bcmb) throws SQLTemplateException;


    void resumeFinishProduct(Long id);

    Map<String, Object> findPageInfo1(Filter filter, Page page);

    List<Map<String, Object>> queryYxInfo(long finishedProductId) throws SQLTemplateException;

    //产品的总克重
    int queryProcBomDetail(String ftcBomVersionId) throws SQLTemplateException;
}
