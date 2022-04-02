package com.bluebirdme.mes.platform.service.impl;

import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.platform.dao.IAttachmentDao;
import com.bluebirdme.mes.platform.service.IAttachmentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author qianchen
 * @date 2020/05/21
 */
@Service
public class AttachmentServiceImpl extends BaseServiceImpl implements IAttachmentService {
    @Resource
    IAttachmentDao attachmentDao;

    @Override
    protected IBaseDao getBaseDao() {
        return attachmentDao;
    }

    @Override
    public <T> Map<String, Object> findPageInfo(final Filter filter, final Page page) throws Exception {
        return null;
    }
}
