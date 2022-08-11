/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.statistics.dao.impl;

import com.bluebirdme.mes.core.base.dao.BaseDaoImpl;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.sql.SQL;
import com.bluebirdme.mes.statistics.dao.ITotalStatisticsDao;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author 徐波
 * @Date 2016年4月5日 下午4:35:34
 */
@Repository
public class TotalStatisticsDaoImpl extends BaseDaoImpl implements ITotalStatisticsDao {
    @Resource
    SessionFactory factory;

    @Override
    public Session getSession() {
        return factory.getCurrentSession();
    }

    @Override
    public Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
        Map<String, Object> map = new HashMap<>();
        String sql = SQL.get(filter.getFilter(), "totalStatistics-list");
        if (page.getAll() == 0) {
            sql += " limit " + (page.getPage() - 1) * page.getRows() + "," + page.getRows();
        }
        SQLQuery dataQuery = getSession().createSQLQuery(sql);
        Iterator<Entry<String, String>> it = filter.getFilter().entrySet().iterator();
        Entry<String, String> entry;
        while (it.hasNext()) {
            entry = it.next();
            if (!(entry.getKey().startsWith("_language") || entry.getKey().startsWith("sort") || entry.getKey().startsWith("order"))) {
                if (entry.getValue().startsWith("in:")) {
                    dataQuery.setParameterList(entry.getKey(), entry.getValue().substring(3).split(","));
                } else if (entry.getValue().startsWith("like:")) {
                    dataQuery.setParameter(entry.getKey(), "%" + entry.getValue().substring(5) + "%");
                } else {
                    dataQuery.setParameter(entry.getKey(), entry.getValue());
                }
            }
        }
        dataQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        map.put("rows", dataQuery.list());
        if (page.getTotalRows() == 0) {
            SQLQuery totalQuery = getSession().createSQLQuery(SQL.get(filter.getFilter(), "totalStatistics-list-count"));
            Iterator<Entry<String, String>> totalIt = filter.getFilter().entrySet().iterator();
            Entry<String, String> totalEntry;
            while (totalIt.hasNext()) {
                totalEntry = totalIt.next();
                if (!(totalEntry.getKey().startsWith("_language") || totalEntry.getKey().startsWith("sort") || totalEntry.getKey().startsWith("order"))) {
                    if (totalEntry.getValue().startsWith("in:")) {
                        totalQuery.setParameterList(totalEntry.getKey(), totalEntry.getValue().substring(3).split(","));
                    } else if (totalEntry.getValue().startsWith("like:")) {
                        totalQuery.setParameter(totalEntry.getKey(), "%" + totalEntry.getValue().substring(5) + "%");
                    } else {
                        totalQuery.setParameter(totalEntry.getKey(), totalEntry.getValue());
                    }
                }
            }
            map.put("total", totalQuery.uniqueResult());
        } else {
            map.put("total", page.getTotalRows());
        }
        return map;
    }

    @Override
    public Map<String, Object> findPageInfoByRoll(Filter filter, Page page) {
        return this.findPageInfo(filter, page, "totalStatistics-list-roll");
    }

    @Override
    public Map<String, Object> findPageInfoByBox(Filter filter, Page page) {
        return this.findPageInfo(filter, page, "totalStatistics-list-box");
    }

    @Override
    public Map<String, Object> findPageInfoByTray(Filter filter, Page page) {
        return this.findPageInfo(filter, page, "totalStatistics-list-tray");
    }

    @Override
    public Map<String, Object> findPageInfoByPart(Filter filter, Page page) {
        return this.findPageInfo(filter, page, "totalStatistics-list-part");
    }

    public boolean isPackaged(String barcode) {
        return ((Number) getSession().createSQLQuery("totalStatistics-packed").setParameter("barCode", barcode).uniqueResult()).intValue() > 0;
    }

    @Override
    public Map<String, Object> productsShopSummary(Filter filter, Page page) {
        return super.findPageInfo(filter, page, "shop-summary-list");
    }

    @Override
    public Map<String, Object> genericFactorySummary(Filter filter, Page page) {
        return super.findPageInfo(filter, page, "shop-factoryName-summary-list");
    }

    @Override
    public Map<String, Object> getPickingStatistics(Filter filter, Page page) {
        return super.findPageInfo(filter, page, "produce-pickingStatistics-list");
    }
}
