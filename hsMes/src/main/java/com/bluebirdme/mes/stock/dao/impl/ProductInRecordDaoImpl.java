/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.stock.dao.impl;

import com.bluebirdme.mes.core.base.dao.BaseDaoImpl;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.sql.SQL;
import com.bluebirdme.mes.stock.dao.IProductInRecordDao;
import com.bluebirdme.mes.stock.entity.ProductInRecord;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import org.xdemo.superutil.j2se.ArrayUtils;

import javax.annotation.Resource;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author 宋黎明
 * @Date 2016年4月5日 下午4:35:34
 */
@Repository
public class ProductInRecordDaoImpl extends BaseDaoImpl implements IProductInRecordDao {
    @Resource
    SessionFactory factory;

    @Override
    public Session getSession() {
        return factory.getCurrentSession();
    }

    @Override
    public Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
        Map<String, Object> pageInfo = this.findPageInfo(filter, page, "productInRecord-list");
        SQLQuery query = getSession().createSQLQuery(SQL.get(filter.getFilter(), "productInRecord-list-weight"));
        Iterator<Entry<String, String>> it = filter.getFilter().entrySet().iterator();
        Entry<String, String> entry;
        while (it.hasNext()) {
            entry = it.next();
            if (ArrayUtils.contains(SQL.IGNORE_KEYS, entry.getKey())) continue;
            if (entry.getValue().startsWith("in:")) {
                query.setParameterList(entry.getKey(), entry.getValue().substring(3).split(","));
            } else if (entry.getValue().startsWith("like:")) {
                query.setParameter(entry.getKey(), "%" + entry.getValue().substring(5).trim() + "%");
            } else {
                query.setParameter(entry.getKey(), entry.getValue().trim());
            }
        }
        query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> list = query.list();
        Double weights = 0.0;
        for (Map<String, Object> stringObjectMap : list) {
            if (stringObjectMap.get("WEIGHTS") == null) {
                continue;
            }
            weights += (Double) stringObjectMap.get("WEIGHTS");
        }
        pageInfo.put("weights", weights);
        return pageInfo;
    }

    @Override
    public Map<String, Object> productsShopStatistics(Filter filter, Page page) {
        return this.findPageInfo(filter, page, "ShopStatistics-list");
    }

    @Override
    public Map<String, Object> shopStorageCategoryStatistics(Filter filter, Page page) {
        return this.findPageInfo(filter, page, "shop-storage-category-list");
    }

    @Override
    public Map<String, Object> findPageInfoDRK(Filter filter, Page page) {
        return this.findPageInfo(filter, page, "productInRecord-drkList");
    }

    @Override
    public ProductInRecord findfirstProductInRecord(String column, Object value) {
        String hql = "From " + ProductInRecord.class.getSimpleName() + "  where " + column + "=:" + column + " order  by id desc ";
        List<ProductInRecord> list = getSession().createQuery(hql).setParameter(column, value).list();
        return (list != null && list.size() > 0) ? list.get(0) : null;
    }
}
