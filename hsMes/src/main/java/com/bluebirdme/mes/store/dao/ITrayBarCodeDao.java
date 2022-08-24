/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.store.dao;

import com.bluebirdme.mes.core.base.dao.IBaseDao;

import java.util.List;
import java.util.Map;

/**
 * @author 宋黎明
 * @Date 2016-11-8 14:59:26
 */

public interface ITrayBarCodeDao extends IBaseDao {
    /**
     * 根据卷条码查询销售订单信息
     *
     * @param code 卷条码
     * @return
     */
    List<Map<String, Object>> findSalesOrderByRollcode(String code);

    /**
     * 根据箱条码查询销售订单信息
     *
     * @param code 箱条码
     * @return
     */
    List<Map<String, Object>> findSalesOrderByBoxcode(String code);

    /**
     * 根据托条码查询销售订单信息
     *
     * @param code 托条码
     * @return
     */
    List<Map<String, Object>> findSalesOrderByTraycode(String code);

    /**
     * 根据卷条码查询产品信息
     *
     * @param code 卷条码
     * @return
     */
    List<Map<String, Object>> findProductByRollcode(String code);

    /**
     * 根据箱条码查询产品信息
     *
     * @param code 箱条码
     * @return
     */
    List<Map<String, Object>> findProductByBoxcode(String code);


    /**
     * 根据托条码查询产品信息
     *
     * @param code 托条码
     * @return
     */
    List<Map<String, Object>> findSalesOrderByBarcode(String code);

    /**
     * 根据条码查询卷或者部件条码信息
     *
     * @param code 卷，部件条码
     * @return
     */
    List<Map<String, Object>> findIbarcodeByBarcode(String code);

    /**
     * 获取托箱盒条码信息
     *
     * @param code 卷，部件条码
     * @return
     * @paramtrayBarcode 托条码
     */
    List<Map<String, Object>> findTrayBoxRollByBarcode(String code);

    /**
     * 根据标签id查询条码里面的最大值
     *
     * @param btwfileId 标签id
     * @return
     */
    List<Map<String, Object>> findMaxTrayBarCode(long btwfileId);

    List<Map<String, Object>> findMaxTrayBarCodeCount();

    List<Map<String, Object>> findMaxTrayPartBarCodeCount();

}
