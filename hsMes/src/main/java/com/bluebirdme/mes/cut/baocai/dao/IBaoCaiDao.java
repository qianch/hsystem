/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.cut.baocai.dao;

import com.bluebirdme.mes.core.base.dao.IBaseDao;

import java.util.List;
import java.util.Map;

/**
 *
 * @author 徐波
 * @Date 2016-11-2 9:30:07
 */

public interface IBaoCaiDao extends IBaseDao {

    /**
     * 根据mainId查询裁剪图纸bom明细
     * @param mainId
     * @return
     */
    public List<Map<String,Object>> findPackingDetailByPackingID(Long mainId);


}
