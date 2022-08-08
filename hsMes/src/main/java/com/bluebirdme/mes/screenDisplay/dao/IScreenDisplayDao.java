/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.screenDisplay.dao;

import com.bluebirdme.mes.core.base.dao.IBaseDao;

import java.util.List;
import java.util.Map;

/**
 * @author 宋黎明
 * @Date 2016-11-25 11:25:41
 */

public interface IScreenDisplayDao extends IBaseDao {
    /**
     * 根据机台ip查询产品信息及生产进度
     *
     * @param ip 机台ip
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findProductInfo(String ip) throws Exception;

    /**
     * 根据机台ip查询优先的产品信息及生产进度
     *
     * @param ip 机台ip
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findFirstProductInfo(String ip) throws Exception;

    /**
     * 根据产品id和设备id查询该机台生产数量
     *
     * @param productId
     * @param deviceId
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findProduceNum(Long productId, Long deviceId) throws Exception;

    List<Map<String, Object>> initCombox() throws Exception;
}
