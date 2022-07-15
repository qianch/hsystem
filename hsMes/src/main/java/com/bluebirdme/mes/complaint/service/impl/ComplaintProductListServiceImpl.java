/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.complaint.service.impl;

import com.bluebirdme.mes.complaint.dao.IComplaintProductListDao;
import com.bluebirdme.mes.complaint.service.IComplaintProductListService;
import com.bluebirdme.mes.core.annotation.AnyExceptionRollback;
import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author 徐波
 * @Date 2016-10-13 10:27:03
 */
@Service
@AnyExceptionRollback
public class ComplaintProductListServiceImpl extends BaseServiceImpl implements IComplaintProductListService {
    @Resource
    IComplaintProductListDao complaintProductListDao;

    @Override
    protected IBaseDao getBaseDao() {
        return complaintProductListDao;
    }

    @Override
    public <T> Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
        return complaintProductListDao.findPageInfo(filter, page);
    }
}
