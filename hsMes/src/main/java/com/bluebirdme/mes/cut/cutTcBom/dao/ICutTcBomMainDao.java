/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.cut.cutTcBom.dao;

import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.sql.SQLTemplateException;

import java.util.List;
import java.util.Map;

/**
 *
 * @author 徐波
 * @Date 2016-11-2 9:30:07
 */

public interface ICutTcBomMainDao extends IBaseDao {

    /**
     * 根据mainId查询裁剪图纸bom明细
     * @param mainId
     * @return
     */
    public List<Map<String,Object>> findCutTcBomDetailByMainId(Long mainId);


    public List<Map<String, Object>> getCutBomJson(String data) throws SQLTemplateException ;
}
