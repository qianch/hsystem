/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.siemens.order.dao;

import com.bluebirdme.mes.core.base.dao.IBaseDao;
/**
 * 
 * @author 高飞
 * @Date 2017-7-26 10:56:16
 */

public interface ICutTaskDao extends IBaseDao {
	public String getSerial();
	public void close(Integer closed,String id);
	public int[] getSuitCountPerDrawings(Long partId,Long ctId);
}
