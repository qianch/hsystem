/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.store.dao;

import com.bluebirdme.mes.core.base.dao.IBaseDao;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 *
 * @author 徐波
 * @Date 2016-11-14 14:37:30
 */

public interface IBoxBarcodeDao extends IBaseDao {
    /**
     * 根据标签id查询条码里面的最大值
     * @param btwfileId 标签id
     * @return
     */
    public List<Map<String, Object>> findMaxBoxBarCode(long btwfileId);

    public List<Map<String, Object>> findMaxBoxBarCodeCount();
}
