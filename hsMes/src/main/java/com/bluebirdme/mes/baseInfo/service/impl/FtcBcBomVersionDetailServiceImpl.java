/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.baseInfo.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import com.bluebirdme.mes.core.annotation.AnyExceptionRollback;

import org.springframework.stereotype.Service;

import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.baseInfo.service.IBcBomVersionDetailService;
import com.bluebirdme.mes.baseInfo.service.IFtcBcBomVersionDetailService;
import com.bluebirdme.mes.baseInfo.dao.IBcBomVersionDetailDao;
import com.bluebirdme.mes.baseInfo.dao.IFtcBcBomVersionDetailDao;
import com.bluebirdme.mes.baseInfo.entity.BcBomVersionDetail;

/**
 * 
 * @author 徐秦冬
 * @Date 2017-12-6 16:26:52
 */
@Service
@AnyExceptionRollback
public class FtcBcBomVersionDetailServiceImpl extends BaseServiceImpl implements IFtcBcBomVersionDetailService {
	
	@Resource IFtcBcBomVersionDetailDao ftcBcBomVersionDetailDao;
	
	@Override
	protected IBaseDao getBaseDao() {
		return ftcBcBomVersionDetailDao;
	}

	@Override
	public <T> Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
		return ftcBcBomVersionDetailDao.findPageInfo(filter, page);
	}

	@Override
	/**
	* delete 方法的简述.
	* 根据传入的包材bom明细的id删除对应的包材bom明细<br>
	* @param ids 类型:String，多个id用‘,’号分割
	* @return 无
	*/
	public void deleteAll(String ids) throws Exception {
		ftcBcBomVersionDetailDao.delete(ids);
	}
	
	
	
}
