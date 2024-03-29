/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.store.dao.impl;

import com.bluebirdme.mes.core.base.dao.BaseDaoImpl;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.sql.SQL;
import com.bluebirdme.mes.store.dao.IWarehosePosDao;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author 肖文彬
 * @Date 2016年4月5日 下午4:35:34
 */
@Repository
public class WarehosePosDaoImpl extends BaseDaoImpl implements IWarehosePosDao {
    @Resource
    SessionFactory factory;

    @Override
    public Session getSession() {
        return factory.getCurrentSession();
    }

    @Override
    public Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
        return findPageInfo(filter, page, "warehousePos-list");
    }

    public void delete(String ids) {
        getSession().createSQLQuery(SQL.get("warehousePos-delete")).setParameterList("id", ids.split(",")).executeUpdate();
    }

    @Override
    public void updateS(String ids) {
        getSession().createSQLQuery(SQL.get("warehousePos-update")).setParameterList("id", ids.split(",")).executeUpdate();
    }
}
