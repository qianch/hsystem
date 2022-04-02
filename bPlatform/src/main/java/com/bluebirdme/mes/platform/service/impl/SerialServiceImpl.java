package com.bluebirdme.mes.platform.service.impl;

import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.platform.dao.ISerialDao;
import com.bluebirdme.mes.platform.entity.Serial;
import com.bluebirdme.mes.platform.service.ISerialService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * @author qianchen
 * @date 2020/05/21
 */
@Service
@Transactional
public class SerialServiceImpl extends BaseServiceImpl implements ISerialService {
    @Resource
    ISerialDao serialDao;

    @Override
    protected IBaseDao getBaseDao() {
        return this.serialDao;
    }

    @Override
    public <T> Map<String, Object> findPageInfo(final Filter filter, final Page page) throws Exception {
        return null;
    }

    @Override
    public synchronized String getSerialNumber(final String preffix, final int length) {
        int v;
        final Serial next = new Serial();
        final Date now = new Date();
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        final Serial s = serialDao.getSerial(preffix);
        if (s == null) {
            v = 1;
            next.setLastUpdateTime(new Date());
            next.setPreffix(preffix);
            next.setMaxNumber(1);
            serialDao.save(next);
        } else {
            if (sdf.format(now).equals(sdf.format(s.getLastUpdateTime()))) {
                v = s.getMaxNumber() + 1;
            } else {
                v = 1;
            }
            s.setLastUpdateTime(now);
            s.setMaxNumber(v);
            serialDao.update(s);
        }
        final StringBuffer buffer = new StringBuffer();
        for (int x = 0; x < length - String.valueOf(v).length(); ++x) {
            buffer.append("0");
        }
        return preffix + buffer + v;
    }
}
