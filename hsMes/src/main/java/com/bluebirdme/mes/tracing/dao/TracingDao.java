package com.bluebirdme.mes.tracing.dao;

import com.bluebirdme.mes.core.base.dao.BaseDaoImpl;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.sql.SQL;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class TracingDao extends BaseDaoImpl implements ITracingDao {
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

    /**
     * 追溯信息<br>
     * 打包信息<br>
     * 原料<br>
     * 卷信息<br>
     * 工艺<br>
     * 发货信息<br>
     * 包装任务<br>
     * 拆包信息<br>
     * 条码作废信息<br>
     *
     * @param code
     * @return
     */
    public Map<String, Object> tracing(String code) {
        Map<String, Object> ret = new HashMap<>();
        ret.put("feeding", tracing_material(code));
        ret.put("pack", tracing_pack(code));
        ret.put("roll", tracing_roll_info(code));
        ret.put("pack_task", tracing_pack_task(code));
        ret.put("process", tracing_process(code));
        ret.put("delivery", tracing_delivery(code));
        ret.put("rkinfo", tracing_rkinfo(code));
        ret.put("ykinfo", tracing_ykinfo(code));
        ret.put("pdinfo", tracing_pdinfo(code));
        ret.put("openpackbarcode", openPackBarCodeByBarcode(code));
        ret.put("abandonbarcode", abandonBarCode(code));
        return ret;
    }


    public List<Map<String, Object>> tracing_rkinfo(String code) {
        String sql = SQL.get("tracingRkinfoByCode");
        List<Map<String, Object>> list = getSession().createSQLQuery(sql).setParameter("code", code).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        return list;
    }


    public List<Map<String, Object>> tracing_ykinfo(String code) {
        String sql = "SELECT t.*,a.userName from  hs_stock_move t  left join platform_user a on t.moveUserId=a.id where t.barcode = '" + code + "' ORDER BY moveTime ";
        List<Map<String, Object>> list = getSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        return list;
    }


    public List<Map<String, Object>> tracing_pdinfo(String code) {
        String sql = "select t.* , a.username from HS_Pending_In_Record t  left join platform_user a on t.operateUserId = a.id where barcode='" + code + "' ORDER BY t.inTime  LIMIT 1";
        List<Map<String, Object>> list = getSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        return list;
    }

    public List<Map<String, Object>> openPackBarCodeByBarcode(String code) {
        String sql = SQL.get("openPackBarCode-byBarcode");
        List<Map<String, Object>> list = getSession().createSQLQuery(sql).setParameter("code", code).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        return list;
    }

    public List<Map<String, Object>> abandonBarCode(String code) {
        String sql = SQL.get("abandonBarCode");
        List<Map<String, Object>> list = getSession().createSQLQuery(sql).setParameter("code", code).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        return list;
    }

    /**
     * 投料信息
     *
     * @param code
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> tracing_material(String code) {
        String sql_1 = "	SELECT " +
                "		s.palletCode code,m.materialModel model,fr.deviceCode,fr.feedingDate,u.userCode,u.userName " +
                "	FROM " +
                "		hs_feeding_record fr " +
                "	LEFT JOIN hs_material_stock_state s ON s.palletCode = fr.materialCode " +
                "	LEFT JOIN hs_material m ON m.id = s.mid " +
                "	LEFT JOIN hs_weave_plan w ON w.id = fr.weaveId " +
                "	LEFT JOIN platform_user u on u.id=fr.operateUserId " +
                "	WHERE " +
                "		w.id = ( " +
                "	SELECT " +
                "		planId " +
                "	FROM " +
                "		hs_roll_barcode b " +
                "	LEFT JOIN hs_roll r ON r.rollBarcode = b.barcode " +
                "	WHERE " +
                "		r.rollBarcode = '" + code + "' " +
                "	) and palletCode is not null group by palletCode";
        String sql_2 = "	SELECT " +
                "		fr.rollCode code,ppd.productModel model,fr.deviceCode,fr.feedingDate,u.userCode,u.userName " +
                "	FROM " +
                "		hs_feeding_record fr " +
                "	LEFT JOIN hs_cut_plan c ON c.id = fr.cutId " +
                "	LEFT JOIN hs_roll r on r.partBarcode=fr.rollCode " +
                "	LEFT JOIN hs_roll_barcode rb on rb.barcode=r.rollBarcode " +
                "	LEFT JOIN hs_produce_plan_detail ppd on ppd.id=rb.producePlanDetailId " +
                "	LEFT JOIN platform_user u on u.id=fr.operateUserId " +
                "	WHERE " +
                "		c.id = ( " +
                "	SELECT " +
                "		planId " +
                "	FROM " +
                "		hs_roll_barcode b " +
                "	LEFT JOIN hs_roll r ON r.partBarcode = b.barcode " +
                "	WHERE " +
                "		r.partBarcode = '" + code + "' " +
                "	)";
        if (code.startsWith("R")) {
            return getSession().createSQLQuery(sql_1).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        }
        return getSession().createSQLQuery(sql_2).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }

    /**
     * 卷信息
     *
     * @param code
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> tracing_roll_info(String code) {
        String sql = SQL.get("tracingRollInfoByCode");
        return getSession().createSQLQuery(sql).setParameter("code", code).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }

    /**
     * 打包信息
     *
     * @param code
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> tracing_pack(String code) {
        String sql = SQL.get("tracingPackByCode");
        return getSession().createSQLQuery(sql).setParameter("code", code).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }

    /**
     * 发货信息
     *
     * @param code
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> tracing_delivery(String code) {
        String sql = SQL.get("tracingDeliveryByCode");
        System.out.println(sql);
        return getSession().createSQLQuery(sql).setParameter("code", code).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }

    /**
     * 工艺信息
     *
     * @param code
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> tracing_process(String code) {
        String sql = "	SELECT processBomCode,processBomVersion,bcBomCode,bcBomVersion from hs_weave_plan where id=(SELECT " +
                "		planId " +
                "	FROM " +
                "		hs_roll_barcode b " +
                "	LEFT JOIN hs_roll r ON r.rollBarcode = b.barcode " +
                "	WHERE " +
                "		r.rollBarcode = '" + code + "') " +
                "	union all " +
                "	SELECT processBomCode,processBomVersion,bcBomCode,bcBomVersion from hs_cut_plan where id=(SELECT " +
                "		planId " +
                "	FROM " +
                "		hs_roll_barcode b " +
                "	LEFT JOIN hs_roll r ON r.partBarcode = b.barcode " +
                "	WHERE " +
                "		r.partBarcode = '" + code + "')";
        return getSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }

    /**
     * 包装任务
     *
     * @param code
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> tracing_pack_task(String code) {
        String sql = "	SELECT o.code,o.version from hs_pack_task o left join hs_pack_task p on p.ptId=o.id " +
                "	where p.ppdId=(" +
                "		SELECT producePlanDetailId from  hs_roll_barcode where barcode='" + code + "'" +
                "	)";
        System.out.println(sql);
        return getSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }

}
