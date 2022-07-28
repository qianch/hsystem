/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.planner.produce.dao.impl;

import com.bluebirdme.mes.baseInfo.entity.BCBomVersion;
import com.bluebirdme.mes.baseInfo.entity.BcBom;
import com.bluebirdme.mes.core.base.dao.BaseDaoImpl;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.sql.SQL;
import com.bluebirdme.mes.planner.produce.dao.IPackageTaskDao;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import org.xdemo.superutil.j2se.MapUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 高飞
 * @Date 2016年4月5日 下午4:35:34
 */
@Repository
public class PackageTaskDaoImpl extends BaseDaoImpl implements IPackageTaskDao {
    @Resource
    SessionFactory factory;

    @Override
    public Session getSession() {
        return factory.getCurrentSession();
    }

    @Override
    public Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
        return this.findPageInfo(filter, page, "packageTask-list");
    }

    public List<Map<String, Object>> findTasks(Long producePlanDetailId) {
        String sql = SQL.get("packageTask-list");
        List<Map<String, Object>> list = getSession().createSQLQuery(sql).setParameter("id", producePlanDetailId).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        List<Map<String, Object>> ret = new ArrayList<>();
        Map<String, Object> unit;
        for (Map<String, Object> map : list) {
            unit = new HashMap<>();
            unit.put("ID", MapUtils.getAsLong(map, "ID"));
            unit.put("PACKBOMID", MapUtils.getAsLong(map, "BID"));
            unit.put("PACKAGECODE", MapUtils.getAsString(map, "PACKAGECODE"));
            unit.put("ROLLCOUNTPERTRAY", MapUtils.getAsLong(map, "ROLLCOUNTPERTRAY"));
            unit.put("TRAYCOUNT", MapUtils.getAsLong(map, "TRAYCOUNT"));
            unit.put("PRINTTRAYCOUNT", MapUtils.getAsLong(map, "PRINTTRAYCOUNT"));
            ret.add(unit);
        }
        return ret;
    }

    public List<Map<String, Object>> findPakcageInfo(Long bcBomId) {
        BcBom bb = findById(BcBom.class, findById(BCBomVersion.class, bcBomId).getPackBomId());
        Map<String, Object> map = new HashMap<>();
        map.put("packBomCode", bb.getPackBomCode());
        List<BcBom> list = findListByMap(BcBom.class, map);
        List<Map<String, Object>> ret = new ArrayList<>();
        Map<String, Object> unit;
        for (BcBom bom : list) {
            unit = new HashMap<>();
            unit.put("PACKAGEBOMID", bom.getId());
            unit.put("PACKAGECODE", bom.getPackBomCode() + "/" + bom.getPackBomGenericName() + "/每托" + bom.getPackBomRollsPerTray().intValue() + "卷");
            unit.put("ROLLCOUNTPERTRAY", bom.getPackBomRollsPerTray());
            unit.put("TRAYCOUNT", "1");
            ret.add(unit);
        }
        return ret;
    }
}
