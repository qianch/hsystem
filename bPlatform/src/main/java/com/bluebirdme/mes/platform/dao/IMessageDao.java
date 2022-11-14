package com.bluebirdme.mes.platform.dao;

import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.platform.entity.Message;

import java.util.List;
import java.util.Map;

/**
 * @author qianchen
 * @date 2020/05/21
 */

public interface IMessageDao extends IBaseDao {
    @Override
    <T> Map<String, Object> findPageInfo(final Filter filter, final Page page) throws Exception;

    List<Message> myMessage(final Integer p0, final Long p1, final Integer p2);

    <T> Map<String, Object> findReadedMessage(final Filter p0, final Page p1) throws Exception;

    <T> Map<String, Object> findUnreadMessage(final Filter p0, final Page p1) throws Exception;
}
