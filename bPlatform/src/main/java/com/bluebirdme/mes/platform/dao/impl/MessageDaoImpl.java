package com.bluebirdme.mes.platform.dao.impl;

import com.bluebirdme.mes.core.base.dao.BaseDaoImpl;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.platform.dao.IMessageDao;
import com.bluebirdme.mes.platform.entity.Message;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author qianchen
 * @date 2020/05/21
 */
@Repository
public class MessageDaoImpl extends BaseDaoImpl implements IMessageDao {
    @Resource
    SessionFactory factory;

    @Override
    public Session getSession() {
        return this.factory.getCurrentSession();
    }

    @Override
    public Map<String, Object> findPageInfo(final Filter filter, final Page page) throws Exception {
        return this.findPageInfo(filter, page, "message-all-list");
    }

    @Override
    public <T> Map<String, Object> findReadedMessage(final Filter filter, final Page page) throws Exception {
        return this.findPageInfo(filter, page, "message-readed-list");
    }

    @Override
    public <T> Map<String, Object> findUnreadMessage(final Filter filter, final Page page) throws Exception {
        return this.findPageInfo(filter, page, "message-unread-list");
    }

    @Override
    public List<Message> myMessage(final Integer topCount, final Long uid, final Integer readSign) {
        final StringBuffer sql = new StringBuffer("select m.*,u.userName as sender from platform_message m left join platform_message_status ms on m.id=ms.msgid left join platform_user u on m.fromUser=u.id where m.toUser=" + uid);
        switch ((readSign == null) ? -1 : readSign) {
            case 0: {
                sql.append(" and ms.status is null or ms.status=0");
                break;
            }
            case 1: {
                sql.append(" and ms.status=1");
                break;
            }
            default: {
                break;
            }
        }
        sql.append(" and isDeleted=0");
        final SQLQuery query = this.getSession().createSQLQuery(sql.toString()).addEntity(Message.class);
        query.setFirstResult(0);
        query.setMaxResults(topCount);
        return (List<Message>) query.list();
    }
}
