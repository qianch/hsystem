/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.complaint.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;
import com.bluebirdme.mes.core.annotation.AnyExceptionRollback;

import org.springframework.stereotype.Service;

import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.core.sql.SQL;
import com.bluebirdme.mes.complaint.service.IComplaintService;
import com.bluebirdme.mes.complaint.dao.IComplaintDao;

/**
 * 
 * @author 高飞
 * @Date 2016-11-25 15:40:05
 */
@Service
@AnyExceptionRollback
public class ComplaintServiceImpl extends BaseServiceImpl implements IComplaintService {
	
	@Resource IComplaintDao complaintDao;
	
	@Override
	protected IBaseDao getBaseDao() {
		return complaintDao;
	}

	@Override
	public <T> Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
		return complaintDao.findPageInfo(filter,page);
	}
	
	public int getSerial(String code,String year){
		return complaintDao.getSerial(code,year);
	}

}
