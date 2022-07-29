/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.produce.dao;

import com.bluebirdme.mes.baseInfo.entity.BCBomVersion;
import com.bluebirdme.mes.baseInfo.entity.FtcBomVersion;
import com.bluebirdme.mes.baseInfo.entity.TcBomVersion;
import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.sql.SQLTemplateException;

import java.util.List;
import java.util.Map;

/**
 * @author 宋黎明
 * @Date 2016-9-30 10:49:34
 */

public interface IFinishedProductDao extends IBaseDao {
    Map<String, Object> findPageInfo2(Filter filter, Page page) throws Exception;

    void delete(String ids);

    //查询tcBom的信息
    List<Map<String, Object>> findTcBom(String data) throws SQLTemplateException;

    //查询ftcBom的信息
    List<Map<String, Object>> findFtcBom(String data) throws SQLTemplateException;

    //查询bcBom的信息
    List<Map<String, Object>> findBcBom(String data) throws SQLTemplateException;


    int querySlBycode(String wlbh) throws SQLTemplateException;

    int querySlinfo(String wlbh) throws SQLTemplateException;

    int queryMfnewcode(String wlbh) throws SQLTemplateException;

    int queryJznewcode(String wlbh) throws SQLTemplateException;

    int queryJcnewcode(String wlbh) throws SQLTemplateException;

    int queryJzcnewcode(String wlbh) throws SQLTemplateException;

    String getzgmcbycode(String code) throws SQLTemplateException;


    //根据ftcbom查询下面的版本
    List<FtcBomVersion> findFtcV(Long id);

    //根据tcbom查询下面的版本
    List<TcBomVersion> findTcV(Long id);

    //根据bcbom查询下面的版本
    List<BCBomVersion> findBcV(Long id);

    //更新成品废弃状态
    void updates(Long id);

    Map<String, Object> findPageInfoDelivery(Filter filter, Page page) throws Exception;

    /**
     * 产成品汇总（按成品类别）
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
     * 查询所有的成品
     *
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findAllFinishProduct() throws Exception;


    List<Map<String, Object>> checkYxInfo(String planCode) throws SQLTemplateException;

    List<Map<String, Object>> queryMfinfo(String wlbh, String mf) throws SQLTemplateException;


    List<Map<String, Object>> queryJZinfo(String wlbh, String jz) throws SQLTemplateException;

    List<Map<String, Object>> queryJCinfo(String wlbh, String jc) throws SQLTemplateException;

    List<Map<String, Object>> queryJZCinfo(String wlbh, String jz, String jc) throws SQLTemplateException;

    List<Map<String, Object>> queryGGinfo(String wlbh, String gg) throws SQLTemplateException;


    int queryCpggInfo(String wlbh) throws SQLTemplateException;


    List<Map<String, Object>> queryBcinfoByBcbm(String bcmb) throws SQLTemplateException;


    Map<String, Object> findPageInfo1(Filter filter, Page page);

    List<Map<String, Object>> queryYxInfo(long finishedProductId) throws SQLTemplateException;

    //产品的总克重
    int queryProcBomDetail(String ftcBomVersionId) throws SQLTemplateException;
}
