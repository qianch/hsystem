/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.mobile.produce.dao;

import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;

import java.util.List;
import java.util.Map;

/**
 * @author 徐波
 * @Date 2016-11-8 14:03:33
 */

public interface IFeedingRecordDao extends IBaseDao {
    <T> Map<String, Object> findPageInfo2(Filter filter, Page page) throws Exception;

    List<Map<String, Object>> querylist(String deviceCode);

    List<Map<String, Object>> querylist2(String deviceCode);

    void editInfo(String weaveid, String userid, String tlids);

    List<Map<String, Object>> queryYlbh(String ylbh, String oldweaveId, String deviceCode);

    void deleteYlbh(String ylbh, String oldweaveId, String deviceCode);
}
