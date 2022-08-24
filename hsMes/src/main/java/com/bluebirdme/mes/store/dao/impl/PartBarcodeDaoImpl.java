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
import com.bluebirdme.mes.store.dao.IPartBarcodeDao;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 徐波
 * @Date 2016年4月5日 下午4:35:34
 */
@Repository
public class PartBarcodeDaoImpl extends BaseDaoImpl implements IPartBarcodeDao {
    @Resource
    SessionFactory factory;

    @Override
    public Session getSession() {
        return factory.getCurrentSession();
    }

    @Override
    public Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
        return this.findPageInfo(filter, page, "partBarcode-list");
    }

    public List<Map<String, Object>> findMaxPartBarCode(long btwfileId) {
        String sql = SQL.get("findMaxPartBarCode");
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("btwfileId", btwfileId);
        SQLQuery query = getSession().createSQLQuery(sql);
        List<Map<String, Object>> list = query.setParameter("btwfileId", btwfileId).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        return list;
    }

    public List<Map<String, Object>> findMaxPartBarCodeCount() {
        String sql = SQL.get("findMaxPartBarCodeCount");
        SQLQuery query = getSession().createSQLQuery(sql);
        List<Map<String, Object>> list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        return list;
    }
}
