/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.baseInfo.dao;

import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.sql.SQLTemplateException;

import java.util.List;
import java.util.Map;

/**
 * @author 宋黎明
 * @Date 2016-10-8 13:36:52
 */

public interface IFtcBomDao extends IBaseDao {

    //删除工艺BOM及它的所有节点
    void delete(String ids);

    //删除BOM明细
    void deleteDetail(String ids);

    //删除BOM版本及它的明细
    void deleteBomVersion(String ids);

    //获取工艺BOM数据
    List<Map<String, Object>> getFtcBomJson(String data) throws SQLTemplateException;

    List<Map<String, Object>> getFtcBomJsonTest(String data) throws SQLTemplateException;

    List<Map<String, Object>> getFtcBomJsonTest1(String data) throws SQLTemplateException;

    //获取BOM版本数据
    List<Map<String, Object>> getFtcBomByVersionJson(String id) throws SQLTemplateException;

    Map<String, Object> findPageInfo1(Filter filter, Page page);
}
