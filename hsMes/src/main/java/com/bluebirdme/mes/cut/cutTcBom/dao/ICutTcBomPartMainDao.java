/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.cut.cutTcBom.dao;

import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.sql.SQLTemplateException;

import java.util.List;
import java.util.Map;

/**
 *
 * @author 徐波
 * @Date 2016-11-2 9:30:07
 */

public interface ICutTcBomPartMainDao extends IBaseDao {

    /**
     * 根据tcBomMainId查询裁片信息
     * @param tcBomMainId
     * @return
     */
    public List<Map<String,Object>> findCutTcBomPartMainByTcBomMainId(Long tcBomMainId);

    /**
     * 根据mainId查询裁片详情
     *
     * @param mainId
     * @return
     */
    public List<Map<String, Object>> findCutTcBomPartDetailByMainId(Long mainId);

}
