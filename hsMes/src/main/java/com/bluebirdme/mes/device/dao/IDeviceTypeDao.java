/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.device.dao;
import java.util.List;

import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.device.entity.DeviceType;

/**
 * 设备类别接口
 * @author 宋黎明
 * @Date 2016年09月28日 下午1:53:36
 */
public interface IDeviceTypeDao extends IBaseDao {
	public void delete(String ids);
	public void batchUpdateDeviceTypeLevel(List<DeviceType> list);
}
