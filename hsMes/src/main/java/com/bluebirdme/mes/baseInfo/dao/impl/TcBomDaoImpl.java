/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.baseInfo.dao.impl;

import com.bluebirdme.mes.baseInfo.dao.ITcBomDao;
import com.bluebirdme.mes.baseInfo.entity.TcBomVersionParts;
import com.bluebirdme.mes.baseInfo.entity.TcBomVersionPartsDetail;
import com.bluebirdme.mes.baseInfo.entity.TcBomVersionPartsFinishedWeightEmbryoCloth;
import com.bluebirdme.mes.core.base.dao.BaseDaoImpl;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.sql.SQL;
import com.bluebirdme.mes.core.sql.SQLTemplateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author 肖文彬
 * @Date 2016年4月5日 下午4:35:34
 */
@Repository
public class TcBomDaoImpl extends BaseDaoImpl implements ITcBomDao {
    @Resource
    SessionFactory factory;

    @Override
    public Session getSession() {
        return factory.getCurrentSession();
    }

    @Override
    public Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
        return findPageInfo(filter, page, "parts-list");
    }

    @Override
    public List<Map<String, Object>> findBom(String data, String state) throws SQLTemplateException {
        HashMap<String, Object> map = new HashMap<>();
        if (state == null || "".equals(state)) {
            map.put("data", data);
            String sql = SQL.get(map, "findBom-list");
            SQLQuery query = getSession().createSQLQuery(sql);
            query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            return query.list();
        } else {
            map.put("data", data);
            String sql = SQL.get(map, "findBom1-list");
            SQLQuery query = getSession().createSQLQuery(sql);
            query.setParameter("state", state).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            return query.list();
        }
    }

    @Override
    public List<Map<String, Object>> getTcBomJsonTest(String data, String state) throws SQLTemplateException {
        HashMap<String, Object> map = new HashMap<String, Object>();
        if (state == null || state == "") {
            map.put("data", data);
            String sql = SQL.get(map, "findBom-list1");
            SQLQuery query = getSession().createSQLQuery(sql);
            query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            return query.list();
        } else {
            map.put("data", data);
            String sql = SQL.get(map, "findBom1-list1");
            SQLQuery query = getSession().createSQLQuery(sql);
            query.setParameter("state", state).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            return query.list();
        }
    }

    @Override
    public List<Map<String, Object>> getTcBomJsonTest1(String data, String state) throws SQLTemplateException {
        HashMap<String, Object> map = new HashMap<String, Object>();
        if (state == null || state == "") {
            map.put("data", data);
            String sql = SQL.get(map, "findBom-list2");
            SQLQuery query = getSession().createSQLQuery(sql);
            query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            return query.list();
        } else {
            map.put("data", data);
            String sql = SQL.get(map, "findBom2-list2");
            SQLQuery query = getSession().createSQLQuery(sql);
            query.setParameter("state", state).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            return query.list();
        }
    }

    @Override
    public List<Map<String, Object>> findV(String id) {
        String sql = SQL.get("findV-list");
        SQLQuery query = getSession().createSQLQuery(sql);
        query.setParameter("id", id).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.list();
    }

    @Override
    public List<Map<String, Object>> findP(String id) {
        String sql = SQL.get("findP-list");
        SQLQuery query = getSession().createSQLQuery(sql);
        query.setParameter("id", id).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.list();
    }

    @Override
    public void deleteP(String ids) {
        getSession().createSQLQuery(SQL.get("part-delete")).setParameterList("id", ids.split(",")).executeUpdate();

    }

    @Override
    public List<Map<String, Object>> findPC(String id) {
        String sql = SQL.get("findPC-list");
        SQLQuery query = getSession().createSQLQuery(sql);
        query.setParameter("id", id).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.list();
    }

    @Override
    public List<TcBomVersionParts> findParts(ArrayList<Long> list1) {
        String sql = SQL.get("findPC-list");
        SQLQuery query = getSession().createSQLQuery(sql);
        query.setParameterList("id", list1.toArray(new Long[]{}));
        query.setResultTransformer(Transformers.aliasToBean(TcBomVersionParts.class));
        query.addScalar("id", StandardBasicTypes.LONG);
        query.addScalar("tcProcBomVersoinId", StandardBasicTypes.LONG);
        query.addScalar("tcProcBomVersionPartsName", StandardBasicTypes.STRING);
        query.addScalar("tcProcBomVersionParentParts", StandardBasicTypes.LONG);
        query.addScalar("tcProcBomVersionPartsCutCode", StandardBasicTypes.STRING);
        query.addScalar("tcProcBomVersionPartsCount", StandardBasicTypes.DOUBLE);
        query.addScalar("tcProcBomVersionPartsSubsCount", StandardBasicTypes.DOUBLE);
        query.addScalar("tcProcBomVersionPartsWeight", StandardBasicTypes.DOUBLE);
        return query.list();
    }

    @Override
    public List<TcBomVersionParts> findPP(Long id) {
        String sql = SQL.get("findP-list");
        SQLQuery query = getSession().createSQLQuery(sql);
        query.setParameter("id", id);
        query.setResultTransformer(Transformers.aliasToBean(TcBomVersionParts.class));
        query.addScalar("id", StandardBasicTypes.LONG);
        query.addScalar("tcProcBomVersoinId", StandardBasicTypes.LONG);
        query.addScalar("tcProcBomVersionPartsName", StandardBasicTypes.STRING);
        query.addScalar("tcProcBomVersionMaterialNumber", StandardBasicTypes.STRING);
        query.addScalar("tcProcBomVersionParentParts", StandardBasicTypes.LONG);
        query.addScalar("tcProcBomVersionPartsCutCode", StandardBasicTypes.STRING);
        query.addScalar("tcProcBomVersionPartsType", StandardBasicTypes.STRING);
        query.addScalar("tcProcBomVersionPartsCount", StandardBasicTypes.DOUBLE);
        query.addScalar("tcProcBomVersionPartsSubsCount", StandardBasicTypes.DOUBLE);
        query.addScalar("tcProcBomVersionPartsWeight", StandardBasicTypes.DOUBLE);
        return query.list();
    }

    @Override
    public void deleteV(String id) {
        getSession().createSQLQuery(SQL.get("delete-v")).setParameter("id", id).executeUpdate();

    }

    @Override
    public void deleteAP() {
        getSession().createSQLQuery(SQL.get("delete-allparts")).executeUpdate();
    }

    @Override
    public void deleteAPD() {
        getSession().createSQLQuery(SQL.get("delete-allD")).executeUpdate();

    }


    @Override
    public List<TcBomVersionParts> findAP(List<Long> list) {
        String sql = SQL.get("findParts-list");
        SQLQuery query = getSession().createSQLQuery(sql);
        query.setParameterList("id", list.toArray(new Long[]{}));
        query.setResultTransformer(Transformers.aliasToBean(TcBomVersionParts.class));
        query.addScalar("id", StandardBasicTypes.LONG);
        query.addScalar("tcProcBomVersoinId", StandardBasicTypes.LONG);
        query.addScalar("tcProcBomVersionPartsName", StandardBasicTypes.STRING);
        query.addScalar("tcProcBomVersionParentParts", StandardBasicTypes.LONG);
        query.addScalar("tcProcBomVersionPartsCutCode", StandardBasicTypes.STRING);
        query.addScalar("tcProcBomVersionPartsCount", StandardBasicTypes.DOUBLE);
        query.addScalar("tcProcBomVersionPartsSubsCount", StandardBasicTypes.DOUBLE);
        query.addScalar("tcProcBomVersionPartsWeight", StandardBasicTypes.DOUBLE);
        return query.list();
    }

    @Override
    public void deleteParts(Set<Long> partsId) {
        String sql = SQL.get("deleteP-list");
        SQLQuery query = getSession().createSQLQuery(sql);
        query.setParameterList("id", partsId.toArray(new Long[]{})).executeUpdate();
    }

    public void deleteD(Set<Long> partsId) {
        getSession().createSQLQuery(SQL.get("deleteD-list")).setParameterList("id", partsId.toArray(new Long[]{})).executeUpdate();
    }

    @Override
    public List<TcBomVersionParts> find(String id) {
        String sql = SQL.get("findP-list");
        SQLQuery query = getSession().createSQLQuery(sql);
        query.setParameter("id", Long.valueOf(id));
        query.setResultTransformer(Transformers.aliasToBean(TcBomVersionParts.class));
        query.addScalar("id", StandardBasicTypes.LONG);
        query.addScalar("tcProcBomVersoinId", StandardBasicTypes.LONG);
        query.addScalar("tcProcBomVersionPartsName", StandardBasicTypes.STRING);
        query.addScalar("tcProcBomVersionParentParts", StandardBasicTypes.LONG);
        query.addScalar("tcProcBomVersionPartsCutCode", StandardBasicTypes.STRING);
        query.addScalar("tcProcBomVersionPartsCount", StandardBasicTypes.DOUBLE);
        query.addScalar("tcProcBomVersionPartsSubsCount", StandardBasicTypes.DOUBLE);
        query.addScalar("tcProcBomVersionPartsWeight", StandardBasicTypes.DOUBLE);
        return query.list();
    }

    @Override
    public Map<String, Object> findFtc(Filter filter, Page page) {
        return this.findPageInfo(filter, page, "findFtcBom-list");
    }


    @Override
    public List<Map<String, Object>> findConsumer(String id) {
        String sql = SQL.get("findConsumer-list");
        SQLQuery query = getSession().createSQLQuery(sql);
        query.setParameter("id", id).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.list();
    }

    @Override
    public List<TcBomVersionPartsDetail> findPartsDetais(Long id) {
        String sql = SQL.get("findPartsDetais-list");
        SQLQuery query = getSession().createSQLQuery(sql);
        query.setParameter("id", id);
        query.setResultTransformer(Transformers.aliasToBean(TcBomVersionPartsDetail.class));
        query.addScalar("id", StandardBasicTypes.LONG);
        query.addScalar("tcProcBomPartsId", StandardBasicTypes.LONG);
        query.addScalar("tcFinishedProductId", StandardBasicTypes.LONG);
        query.addScalar("length", StandardBasicTypes.DOUBLE);
        query.addScalar("drawingNo", StandardBasicTypes.STRING);
        query.addScalar("levelNo", StandardBasicTypes.STRING);
        query.addScalar("rollNo", StandardBasicTypes.STRING);
        query.addScalar("tcProcBomFabricCount", StandardBasicTypes.DOUBLE);
        query.addScalar("tcTheoreticalWeigh", StandardBasicTypes.DOUBLE);
        return query.list();
    }

    @Override
    public List<TcBomVersionPartsFinishedWeightEmbryoCloth> findPartsFinishedWeightEmbryoCloths(Long id) {
        String sql = SQL.get("findPartsEmbryoCloths-list");
        SQLQuery query = getSession().createSQLQuery(sql);
        query.setParameter("id", id);
        query.setResultTransformer(Transformers.aliasToBean(TcBomVersionPartsFinishedWeightEmbryoCloth.class));
        query.addScalar("id", StandardBasicTypes.LONG);
        query.addScalar("tcProcBomPartsId", StandardBasicTypes.LONG);
        query.addScalar("materialNumber", StandardBasicTypes.STRING);
        query.addScalar("embryoClothName", StandardBasicTypes.STRING);
        query.addScalar("weight", StandardBasicTypes.DOUBLE);
        return query.list();
    }
}
