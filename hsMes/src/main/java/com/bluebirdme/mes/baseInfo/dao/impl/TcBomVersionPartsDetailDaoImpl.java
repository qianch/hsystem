/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.baseInfo.dao.impl;

import com.bluebirdme.mes.baseInfo.dao.ITcBomVersionPartsDetailDao;
import com.bluebirdme.mes.core.base.dao.BaseDaoImpl;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.sql.SQL;
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
public class TcBomVersionPartsDetailDaoImpl extends BaseDaoImpl implements ITcBomVersionPartsDetailDao {
    @Resource
    SessionFactory factory;

    @Override
    public Session getSession() {
        return factory.getCurrentSession();
    }

    @Override
    public <T> Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
        return findPageInfo(filter, page, "partsDetail-list");
    }

    public void delete(String ids) {
        getSession().createSQLQuery(SQL.get("partsDetail-delete")).setParameterList("id", ids.split(",")).executeUpdate();
    }

    @Override
    public Map<String, Object> findPageInfo1(Filter filter, Page page) {
        return this.findPageInfo(filter, page, "partsDetail-mirrorList");
    }
}
