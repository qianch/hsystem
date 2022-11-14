package com.bluebirdme.mes.stock.dao.impl;

import com.bluebirdme.mes.core.base.dao.BaseDaoImpl;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.sql.SQL;
import com.bluebirdme.mes.core.sql.SQLTemplateException;
import com.bluebirdme.mes.stock.dao.IStockDao;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 库存查询
 *
 * @author Goofy
 * @Date 2016年11月24日 下午3:05:02
 */
@Repository
public class StockDaoImpl extends BaseDaoImpl implements IStockDao {
    @Resource
    SessionFactory factory;

    @Override
    public Session getSession() {
        return factory.getCurrentSession();
    }

    @Override
    public Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
        return null;
    }

    public Map<String, Object> list(String type, String[] kuweis) throws SQLTemplateException {
        Map<String, Object> param = new HashMap<>();
        param.put("wtype", type);
        String sql;
        Map<String, Object> ret = new HashMap<>();
        List<Map<String, Object>> trays;
        List<Map<String, Object>> rolls;
        List<Map<String, Object>> materials;
        if (type.equals("cp")) {
            param.put("ptype", "tray");
            sql = SQL.get(param, "stock-sum");
            trays = getSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).setParameterList("pos", kuweis).list();
            param.put("ptype", "roll");
            sql = SQL.get(param, "stock-sum");
            rolls = getSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).setParameterList("pos", kuweis).list();

            if (trays != null) {
                for (Map<String, Object> map : trays) {
                    ret.put(map.get("WAREHOUSEPOSCODE").toString(), map.get("TRAYCOUNT"));
                }
            }

            if (rolls != null) {
                for (Map<String, Object> map : rolls) {
                    ret.put(map.get("WAREHOUSEPOSCODE").toString(), ret.get(map.get("WAREHOUSEPOSCODE").toString()) == null ? (";" + map.get("ROLLCOUNT")) : (ret.get(map.get("WAREHOUSEPOSCODE").toString()) + ";" + map.get("ROLLCOUNT")));
                }
            }
        } else {
            sql = SQL.get(param, "stock-sum");
            materials = getSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).setParameterList("pos", kuweis).list();
            if (materials != null) {
                for (Map<String, Object> map : materials) {
                    ret.put(map.get("WAREHOUSEPOSCODE").toString(), map.get("WEIGTH"));
                }
            }
        }
        return ret;
    }
}
