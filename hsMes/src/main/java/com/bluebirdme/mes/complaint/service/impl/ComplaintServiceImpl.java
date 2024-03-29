/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.complaint.service.impl;

import com.bluebirdme.mes.complaint.dao.IComplaintDao;
import com.bluebirdme.mes.complaint.service.IComplaintService;
import com.bluebirdme.mes.core.annotation.AnyExceptionRollback;
import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author 高飞
 * @Date 2016-11-25 15:40:05
 */
@Service
@AnyExceptionRollback
public class ComplaintServiceImpl extends BaseServiceImpl implements IComplaintService {
    @Resource
    IComplaintDao complaintDao;

    @Override
    protected IBaseDao getBaseDao() {
        return complaintDao;
    }

    public int getSerial(String code, String year) {
        return complaintDao.getSerial(code, year);
    }
}
