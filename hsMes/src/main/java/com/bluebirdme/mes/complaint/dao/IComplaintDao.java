/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.complaint.dao;

import com.bluebirdme.mes.core.base.dao.IBaseDao;

/**
 * @author 高飞
 * @Date 2016-11-25 15:40:05
 */

public interface IComplaintDao extends IBaseDao {
    int getSerial(String code, String year);
}
