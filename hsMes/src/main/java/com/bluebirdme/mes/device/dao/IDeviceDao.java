/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.device.dao;

import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.sql.SQLTemplateException;

import java.util.List;
import java.util.Map;

/**
 * @author 宋黎明
 * @Date 2016-9-29 11:46:46
 */

public interface IDeviceDao extends IBaseDao {
    void delete(String ids);

    List<Map<String, Object>> find(String id);

    Map<String, Object> getDeliveryDate(Filter filter, Page page);

    List<Map<String, Object>> getDeviceDepartment() throws SQLTemplateException;

    void saveDeviceAndOrder(String dids, String wids);

    void saveDeviceAndOrder2(String dids, String wids);

    void deleteDeviceAndOrder(String dids, String wids);

    /**
     * 获取机台的计划
     *
     * @param dids
     * @return
     */
    Map<String, Object> findDevicePlans(String dids);


    Map<String, Object> getBjDetails(String devcode, String yx, String partname);


    void deleteDevicePlans(String dids, String wids);

    <T> Map<String, Object> findAllDevicePlans(Filter filter, Page page) throws Exception;

    List<Map<String, Object>> findDevicePlans(Long did);

    void setProducing(Long deviceId, Long weavePlanId);

    List<Map<String, Object>> getFtcBcBomVersionDetail(Integer packVersionId);

    List<Map<String, Object>> getCutWorkShop() throws SQLTemplateException;
}
