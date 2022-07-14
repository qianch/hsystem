/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.baseInfo.dao;

import com.bluebirdme.mes.baseInfo.entity.FtcBcBom;
import com.bluebirdme.mes.core.base.dao.IBaseDao;

import java.util.List;

/**
 * @author 高飞
 * @Date 2017-11-28 11:10:48
 */

public interface IFtcBcBomDao extends IBaseDao {

    /**
     * @param pid       父ID
     * @param queryData 查询内容
     * @param level     目录级别
     * @return
     */
	List<FtcBcBom> tree(String pid, String queryData, Integer level);
}
