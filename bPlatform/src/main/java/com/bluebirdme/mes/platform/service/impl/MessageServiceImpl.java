package com.bluebirdme.mes.platform.service.impl;

import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.platform.dao.IMessageDao;
import com.bluebirdme.mes.platform.entity.Message;
import com.bluebirdme.mes.platform.entity.MessageStatus;
import com.bluebirdme.mes.platform.entity.MessageType;
import com.bluebirdme.mes.platform.entity.Subscription;
import com.bluebirdme.mes.platform.service.IMessageService;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author qianchen
 * @date 2020/05/21
 */

public class MessageServiceImpl extends BaseServiceImpl implements IMessageService {
    @Resource
    IMessageDao msgDao;

    @Override
    protected IBaseDao getBaseDao() {
        return this.msgDao;
    }

    @Override
    public <T> Map<String, Object> findPageInfo(final Filter filter, final Page page) throws Exception {
        return this.msgDao.findPageInfo(filter, page);
    }

    @Override
    public void save(final Message message) {
        if (message.getToUser() != null) {
            super.save(message);
        }
    }

    @Override
    public List<Message> myMessage(final Integer topCount, final Long uid, final Integer readSign) {
        return msgDao.myMessage(topCount, uid, readSign);
    }

    @Override
    public void deleteMessage(final String messageIds, final Long userId) {
        final String[] messageIdsArray = messageIds.split(",");
        for (final String m : messageIdsArray) {
            final Long messageId = Long.parseLong(m);
            final HashMap<String, Object> map = new HashMap();
            map.put("msgid", messageId);
            map.put("userId", userId);
            MessageStatus ms = this.findUniqueByMap(MessageStatus.class, map);
            if (ms == null) {
                ms = new MessageStatus();
                ms.setIsDeleted(1);
                ms.setMsgid(messageId);
                ms.setUserId(userId);
                save(ms);
            } else {
                ms.setIsDeleted(1);
                update(ms);
            }
        }
    }

    @Override
    public void createMessage(final String content, final String messageType, final Long fromUser, final String fromUserName, final Long[] toUser, final String uuid) {
        for (final Long toUserId : toUser) {
            final Message msg = new Message();
            msg.setContent(content);
            msg.setMessageType(messageType);
            msg.setFromUser(fromUser);
            msg.setFromUserName(fromUserName);
            msg.setCreateTime(new Date());
            msg.setToUser(toUserId);
            msg.setUuid(uuid);
            save(msg);
        }
    }

    @Override
    public <T> Map<String, Object> findReadedMessage(final Filter filter, final Page page) throws Exception {
        return msgDao.findReadedMessage(filter, page);
    }

    @Override
    public <T> Map<String, Object> findUnreadMessage(final Filter filter, final Page page) throws Exception {
        return msgDao.findUnreadMessage(filter, page);
    }

    @Override
    public void subMessage(final String messageTypes, final Long userId) throws Exception {
        final String[] mtps = messageTypes.split(",");
        for (final String messageType : mtps) {
            final HashMap<String, Object> map = new HashMap();
            map.put("userId", userId);
            map.put("messageType", messageType);
            if (!msgDao.isExist(Subscription.class, map, true)) {
                final Subscription subscription = new Subscription();
                subscription.setSubDate(new Date());
                subscription.setMessageType(messageType);
                subscription.setUserId(userId);
                msgDao.save(subscription);
            }
        }
    }

    @Override
    public void unsubMessage(final String messageTypes, final Long userId) throws Exception {
        final String[] mtps = messageTypes.split(",");
        for (final String messageType : mtps) {
            final HashMap<String, Object> map = new HashMap();
            map.put("userId", userId);
            map.put("messageType", messageType);
            final Subscription subscription = this.msgDao.findUniqueByMap(Subscription.class, map);
            msgDao.delete(subscription);
        }
    }

    @Override
    public void readMessage(final String messageIds, final Long userId) {
        final String[] idArray = messageIds.split(",");
        for (final String id : idArray) {
            final Long messageId = Long.parseLong(id);
            final HashMap<String, Object> map = new HashMap();
            map.put("msgid", messageId);
            map.put("userId", userId);
            if (!this.isExist(MessageStatus.class, map, true)) {
                final MessageStatus ms = new MessageStatus();
                ms.setMsgid(messageId);
                ms.setUserId(userId);
                ms.setIsDeleted(0);
                save(ms);
            }
        }
    }

    @Override
    public String[] getAllType(final MessageType mt) {
        return mt.getAllType();
    }
}
