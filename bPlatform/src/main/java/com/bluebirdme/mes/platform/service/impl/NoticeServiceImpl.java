package com.bluebirdme.mes.platform.service.impl;

import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.platform.dao.INoticeDao;
import com.bluebirdme.mes.platform.entity.Notice;
import com.bluebirdme.mes.platform.service.INoticeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author qianchen
 * @date 2020/05/21
 */
@Service
@Transactional
public class NoticeServiceImpl extends BaseServiceImpl implements INoticeService {
    @Resource
    INoticeDao noticedao;

    @Override
    protected IBaseDao getBaseDao() {
        return this.noticedao;
    }

    @Override
    public <T> Map<String, Object> findPageInfo(final Filter filter, final Page page) throws Exception {
        return this.noticedao.findPageInfo(filter, page);
    }

    @Override
    public void save(final Notice notice) throws InterruptedException {
        notice.setInputTime(new Date());
        this.noticedao.save(notice);
    }

    @Override
    public void delete(final String ids) {
        this.noticedao.delete(ids);
    }

    @Override
    public List<Map<String, Object>> findNotice() {
        return this.noticedao.findNotice();
    }
}
