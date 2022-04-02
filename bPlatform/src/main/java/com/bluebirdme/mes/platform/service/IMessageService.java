package com.bluebirdme.mes.platform.service;

import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.IBaseService;
import com.bluebirdme.mes.platform.entity.Message;
import com.bluebirdme.mes.platform.entity.MessageType;

import java.util.List;
import java.util.Map;

/**
 * @author qianchen
 * @date 2020/05/21
 */

public interface IMessageService extends IBaseService {
    void save(final Message p0);

    @Override
    <T> Map<String, Object> findPageInfo(final Filter p0, final Page p1) throws Exception;

    List<Message> myMessage(final Integer p0, final Long p1, final Integer p2);

    void deleteMessage(final String p0, final Long p1);

    void readMessage(final String p0, final Long p1);

    String[] getAllType(final MessageType p0);

    void createMessage(final String p0, final String p1, final Long p2, final String p3, final Long[] p4, final String p5);

    <T> Map<String, Object> findReadedMessage(final Filter p0, final Page p1) throws Exception;

    <T> Map<String, Object> findUnreadMessage(final Filter p0, final Page p1) throws Exception;

    void subMessage(final String p0, final Long p1) throws Exception;

    void unsubMessage(final String p0, final Long p1) throws Exception;
}
