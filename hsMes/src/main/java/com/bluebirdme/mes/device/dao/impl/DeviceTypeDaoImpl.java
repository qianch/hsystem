package com.bluebirdme.mes.device.dao.impl;

import com.bluebirdme.mes.core.base.dao.BaseDaoImpl;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.sql.SQL;
import com.bluebirdme.mes.device.dao.IDeviceTypeDao;
import com.bluebirdme.mes.device.entity.DeviceType;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Repository
public class DeviceTypeDaoImpl extends BaseDaoImpl implements IDeviceTypeDao {
    @Resource
    SessionFactory factory;

    @Override
    public Session getSession() {
        return factory.getCurrentSession();
    }

    @Override
    public <T> Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
        return findPageInfo(filter, page, "deviceType-list");
    }

    public void delete(String ids) {
        getSession().createSQLQuery(SQL.get("deviceType-delete")).setParameterList("id", ids.split(",")).executeUpdate();
    }

    @Override
    public void batchUpdateDeviceTypeLevel(List<DeviceType> list) {
        String sql = "update HSMES_DEVICETYPE set CATEGORYPARENTID=:categoryParentId where id=:id";
        SQLQuery query = getSession().createSQLQuery(sql);
        for (DeviceType dept : list) {
            query.setParameter("CATEGORYPARENTID", dept.getCategoryParentId());
            query.setParameter("id", dept.getId());
            query.executeUpdate();
        }
    }
}
