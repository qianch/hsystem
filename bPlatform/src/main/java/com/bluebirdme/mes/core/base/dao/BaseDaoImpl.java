package com.bluebirdme.mes.core.base.dao;

import com.bluebirdme.mes.core.base.LanguageProvider;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.constant.RuntimeVariable;
import com.bluebirdme.mes.core.sql.SQL;
import com.bluebirdme.mes.core.sql.SQLCompatible;
import com.bluebirdme.mes.core.sql.SQLTemplateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.xdemo.superutil.j2se.ArrayUtils;
import org.xdemo.superutil.j2se.ReflectUtils;
import org.xdemo.superutil.j2se.StringUtils;

import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;
import java.util.Map.Entry;

/**
 * @author qianchen
 * @date 2020/05/21
 */
public abstract class BaseDaoImpl extends LanguageProvider implements IBaseDao {
    public BaseDaoImpl() {
    }

    public abstract Session getSession();

    @Override
    public <T> void empty(Class<T> clazz) throws Exception {
        Table table = clazz.getAnnotation(Table.class);
        if (table != null) {
            empty(table.name());
        } else {
            throw new Exception("找不到" + clazz.getName() + "对应的数据库映射");
        }
    }

    @Override
    public void empty(String tableName) {
        this.getSession().createSQLQuery("delete from " + tableName + " where 1=1").executeUpdate();
    }

    @Override
    public void save(Object... object) {
        for (Object o : object) {
            getSession().save(o);
        }
        //缓存写入数据库,一个事务中有一个失败，全部回滚
        getSession().flush();
    }

    @Override
    public void merge(Object... object) {
        for (Object o : object) {
            getSession().merge(o);
        }
        getSession().flush();
    }

    @Override
    public void update(Object... object) {
        for (Object o : object) {
            getSession().update(o);
        }
        getSession().flush();
    }

    @Override
    public void update2(Object... object) throws Exception {
        for (Object o : object) {
            Class<?> clazz = o.getClass();
            StringBuilder builder = new StringBuilder("update " + clazz.getAnnotation(Table.class).name() + " set ");
            List<Field> fields = ReflectUtils.getFields(clazz, true);
            Iterator<Field> iterator = fields.iterator();

            while (iterator.hasNext()) {
                Field field = iterator.next();
                field.setAccessible(true);
                if (field.getAnnotation(Transient.class) == null && field.get(o) != null && !field.getName().equalsIgnoreCase("id")) {
                    builder.append(!builder.toString().contains("=") ? "" : ",").append(" ").append(field.getName()).append("=:").append(field.getName());
                }
            }

            builder.append(" where id=:id");
            SQLQuery query = this.getSession().createSQLQuery(builder.toString());

            iterator = fields.iterator();
            while (iterator.hasNext()) {
                Field field = iterator.next();
                field.setAccessible(true);
                if (field.getAnnotation(Transient.class) == null && field.get(o) != null) {
                    query.setParameter(field.getName(), field.get(o));
                }
            }
            query.executeUpdate();
        }
    }

    @Override
    public void delete(Object... object) {
        for (Object o : object) {
            getSession().delete(o);
        }
        getSession().flush();
    }

    @Override
    public <T> void delete(Class<T> clazz, Serializable... ids) {
        Query query = this.getSession().createQuery("delete From " + clazz.getSimpleName() + " where id=:id");
        for (Serializable s : ids) {
            query.setParameter("id", s);
            query.executeUpdate();
        }
    }

    @Override
    public <T> void delete(Class<T> clazz, String ids) {
        String[] ids_temp = ids.split(",");
        Serializable[] ids_target = new Serializable[ids_temp.length];
        for (int i = 0; i < ids_temp.length; ++i) {
            ids_target[i] = Long.parseLong(ids_temp[i]);
        }
        delete(clazz, ids_target);
    }

    @Override
    public <T> void fakeDelete(Class<T> clazz, Serializable... ids) {
        Query query = this.getSession().createQuery("update " + clazz.getSimpleName() + " set isDeleted = 1 where id in (:id)");
        query.setParameterList("id", ids);
        query.executeUpdate();
    }

    @Override
    public <T> void fakeDelete(Class<T> clazz, String ids) {
        String[] ids_ = ids.split(",");
        Serializable[] _ids = new Serializable[ids.split(",").length];
        for (int i = 0; i < _ids.length; ++i) {
            _ids[i] = Long.parseLong(ids_[i]);
        }
        fakeDelete(clazz, _ids);
    }

    @Override
    public <T> List<T> findAll(Class<T> clazz) {
        Query query = this.getSession().createQuery("From " + clazz.getSimpleName());
        return query.list();
    }

    @Override
    public <T> T findById(Class<T> clazz, Serializable id) {
        T t = getSession().get(clazz, id);
        if (t != null) {
            getSession().evict(t);
        }
        return t;
    }

    /**
     * @deprecated
     */
    @Override
    @Deprecated
    public <T> boolean isExist(Class<T> clazz, Map<String, Object> map) {
        StringBuilder builder = new StringBuilder("From " + clazz.getSimpleName() + " where 1=1 ");
        try {
            builder.append(clazz.getField("isDeleted") == null ? "" : " and isDeleted<>1");
        } catch (Exception ignored) {
        }

        Set<Entry<String, Object>> set = map.entrySet();
        Iterator<Entry<String, Object>> iterator = set.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            Entry<String, Object> entry = iterator.next();
            builder.append(i++ == 0 ? " and " : " or ").append(entry.getKey()).append("=:").append(entry.getKey());
        }
        Query query = this.getSession().createQuery(builder.toString());

        iterator = set.iterator();
        while (iterator.hasNext()) {
            Entry<String, Object> entry = iterator.next();
            query.setParameter(entry.getKey(), entry.getValue());
        }
        List<Object[]> list = query.list();
        return list != null && list.size() != 0;
    }

    @Override
    public <T> boolean has(Class<T> clazz, Map<String, Object> map) {
        StringBuilder builder = new StringBuilder("From " + clazz.getSimpleName() + " where 1=1 ");
        try {
            builder.append(clazz.getField("isDeleted") == null ? "" : " and isDeleted<>1");
        } catch (Exception ignored) {
        }

        Set<Entry<String, Object>> set = map.entrySet();
        Iterator<Entry<String, Object>> iterator = set.iterator();
        while (iterator.hasNext()) {
            Entry<String, Object> entry = iterator.next();
            if (entry.getValue() == null) {
                builder.append(" and " + entry.getKey() + " is null");
            } else if (entry.getValue().getClass().isArray()) {
                builder.append(" and " + entry.getKey() + " in :" + entry.getKey());
            } else {
                builder.append(" and " + entry.getKey() + "=:" + entry.getKey());
            }
        }
        Query query = this.getSession().createQuery(builder.toString());

        iterator = set.iterator();
        while (iterator.hasNext()) {
            Entry<String, Object> entry = iterator.next();
            if (entry.getValue() != null) {
                if (entry.getValue().getClass().isArray()) {
                    query.setParameterList(entry.getKey(), (Object[]) entry.getValue());
                } else {
                    query.setParameter(entry.getKey(), entry.getValue());
                }
            }
        }

        List<Object[]> list = query.list();
        return list != null && list.size() != 0;
    }

    @Override
    public <T> boolean has(Class<T> clazz, Map<String, Object> map, Long id) {
        StringBuilder builder = new StringBuilder("From " + clazz.getSimpleName() + " where 1=1 ");
        try {
            builder.append(" and isDeleted<>1");
        } catch (Exception ignored) {
        }

        Set<Entry<String, Object>> set = map.entrySet();
        Iterator<Entry<String, Object>> iterator = set.iterator();
        while (iterator.hasNext()) {
            Entry<String, Object> entry = iterator.next();
            if (entry.getValue() == null) {
                builder.append(" and ").append(entry.getKey()).append(" is null");
            } else if (entry.getValue().getClass().isArray()) {
                builder.append(" and ").append(entry.getKey()).append(" in :").append(entry.getKey());
            } else {
                builder.append(" and ").append(entry.getKey()).append("=:").append(entry.getKey());
            }
        }
        builder.append(" and id<>:id");
        Query query = getSession().createQuery(builder.toString());

        iterator = set.iterator();
        while (iterator.hasNext()) {
            Entry<String, Object> entry = iterator.next();
            if (entry.getValue() != null) {
                if (entry.getValue().getClass().isArray()) {
                    query.setParameterList(entry.getKey(), (Object[]) entry.getValue());
                } else {
                    query.setParameter(entry.getKey(), entry.getValue());
                }
            }
        }

        query.setParameter("id", id);
        List<Object[]> list = query.list();
        return list != null && list.size() != 0;
    }

    @Override
    public <T> boolean isExist(Class<T> clazz, Map<String, Object> map, boolean isAndCondition) {
        StringBuilder builder = new StringBuilder("From " + clazz.getSimpleName() + " where 1=1 ");
        try {
            builder.append(" and isDeleted<>1");
        } catch (Exception ignored) {

        }

        Set<Entry<String, Object>> set = map.entrySet();
        Iterator<Entry<String, Object>> iterator = set.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            Entry<String, Object> entry = iterator.next();
            builder.append(i++ == 0 ? " and " : (isAndCondition ? " and " : " or "));
            builder.append(" ").append(entry.getKey()).append("=:").append(entry.getKey());
        }
        Query query = getSession().createQuery(builder.toString());

        iterator = set.iterator();
        while (iterator.hasNext()) {
            Entry<String, Object> entry = iterator.next();
            query.setParameter(entry.getKey(), entry.getValue());
        }

        List<Object[]> list = query.list();
        return list != null && list.size() != 0;
    }

    @Override
    public <T> boolean isExist(Class<T> clazz, Map<String, Object> map, Long id) throws Exception {
        if (id == null) {
            throw new Exception("id不可为空");
        }
        StringBuilder builder = new StringBuilder("From " + clazz.getSimpleName() + " where 1=1 ");
        try {
            builder.append(clazz.getField("isDeleted") == null ? "" : " and isDeleted<>1");
        } catch (Exception ignored) {
        }

        Set<Entry<String, Object>> set = map.entrySet();
        Iterator<Entry<String, Object>> iterator = set.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            Entry<String, Object> entry = iterator.next();
            builder.append(i++ == 0 ? " and " : " or ").append(entry.getKey()).append("=:").append(entry.getKey());
        }

        builder.append(" and id<>:id");
        Query query = this.getSession().createQuery(builder.toString());

        iterator = set.iterator();
        while (iterator.hasNext()) {
            Entry<String, Object> entry = iterator.next();
            query.setParameter(entry.getKey(), entry.getValue());
        }
        query.setParameter("id", id);
        List<Object[]> list = query.list();
        return list != null && list.size() != 0;
    }

    @Override
    public <T> boolean isExist(Class<T> clazz, Map<String, Object> map, Long id, boolean isAndCondition) throws Exception {
        if (id == null) {
            throw new Exception("id不可为空");
        }

        StringBuilder builder = new StringBuilder("From " + clazz.getSimpleName() + " where 1=1 ");
        try {
            builder.append(clazz.getField("isDeleted") == null ? "" : " and isDeleted<>1");
        } catch (Exception ignored) {
        }

        Set<Entry<String, Object>> set = map.entrySet();
        Iterator<Entry<String, Object>> iterator = set.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            Entry<String, Object> entry = iterator.next();
            builder.append(i++ == 0 ? " and " : (isAndCondition ? " and " : " or "));
            builder.append(" ").append(entry.getKey()).append("=:").append(entry.getKey());
        }
        builder.append(" and id<>:id");
        Query query = this.getSession().createQuery(builder.toString());

        iterator = set.iterator();
        while (iterator.hasNext()) {
            Entry<String, Object> entry = iterator.next();
            query.setParameter(entry.getKey(), entry.getValue());
        }
        query.setParameter("id", id);
        List<Object[]> list = query.list();
        return list != null && list.size() != 0;
    }

    public Map<String, Object> findPageInfo(Filter filter, Page page, String sqlId) {
        Map<String, Object> map = new HashMap<>();
        filter.clear();
        try {
            List<Map<String, Object>> list = this.findPageData(filter, page, sqlId);
            map.put("rows", list);
            map.put("total", page.getAll() == 0 ? this.findPageDataTotal(filter, sqlId) : (long) (list == null ? 0 : list.size()));
        } catch (SQLTemplateException templateException) {
            templateException.printStackTrace();
        }
        return map;
    }

    public List<Map<String, Object>> findPageData(Filter filter, Page page, String sqlId) throws SQLTemplateException {
        SQLQuery query = this.getSession().createSQLQuery(SQL.get(filter.getFilter(), sqlId));
        Iterator<Entry<String, String>> iterator = filter.getFilter().entrySet().iterator();
        while (iterator.hasNext()) {
            Entry entry = iterator.next();
            if (!ArrayUtils.contains(SQL.IGNORE_KEYS, entry.getKey())) {
                if (((String) entry.getValue()).startsWith("in:")) {
                    query.setParameterList((String) entry.getKey(), ((String) entry.getValue()).substring(3).split(","));
                } else if (((String) entry.getValue()).startsWith("like:")) {
                    query.setParameter((String) entry.getKey(), "%" + ((String) entry.getValue()).substring(5) + "%");
                } else {
                    query.setParameter((String) entry.getKey(), entry.getValue());
                }
            }
        }

        if (page.getAll() == 0) {
            query.setFirstResult((page.getPage() - 1) * page.getRows());
            query.setMaxResults(page.getRows());
        }

        query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.list();
    }

    public Long findPageDataTotal(Filter filter, String sqlId) throws SQLTemplateException {
        SQLQuery query = this.getSession().createSQLQuery("select count(*) from (" + SQL.get(filter.getFilter(), sqlId) + " )" + SQLCompatible.getSubQueryAsStatement(RuntimeVariable.DBTYPE));
        Iterator<Entry<String, String>> iterator = filter.getFilter().entrySet().iterator();
        while (iterator.hasNext()) {
            Entry entry = iterator.next();
            if (!ArrayUtils.contains(SQL.IGNORE_KEYS, entry.getKey())) {
                if (((String) entry.getValue()).startsWith("in:")) {
                    query.setParameterList((String) entry.getKey(), ((String) entry.getValue()).substring(3).split(","));
                } else if (((String) entry.getValue()).startsWith("like:")) {
                    query.setParameter((String) entry.getKey(), "%" + ((String) entry.getValue()).substring(5) + "%");
                } else {
                    query.setParameter((String) entry.getKey(), entry.getValue());
                }
            }
        }
        return Long.parseLong(query.uniqueResult().toString());
    }

    /**
     * @deprecated
     */
    @Deprecated
    public <T> List<T> findPageData(String sql, Filter filter, Page page) {
        SQLQuery query = getSession().createSQLQuery(sql);
        Map<String, String> map = filter.getFilter();
        for (Entry entry : map.entrySet()) {
            if (!StringUtils.isBlank((String) entry.getValue())) {
                query.setParameter((String) entry.getKey(), entry.getValue());
            }
        }

        if (page.getAll() == 0) {
            query.setFirstResult((page.getPage() - 1) * page.getRows());
            query.setMaxResults(page.getRows());
        }
        query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.list();
    }

    public <T> List<T> find(Class clazz, String sql, Filter filter, Page page) {
        SQLQuery query = getSession().createSQLQuery(sql);
        Map<String, String> map = filter.getFilter();
        for (Entry entry : map.entrySet()) {
            if (!StringUtils.isBlank((String) entry.getValue())) {
                query.setParameter((String) entry.getKey(), entry.getValue());
            }
        }

        if (page.getAll() == 0) {
            query.setFirstResult((page.getPage() - 1) * page.getRows());
            query.setMaxResults(page.getRows());
        }

        query.setResultTransformer(Transformers.aliasToBean(clazz));
        return query.list();
    }

    public Long findPageDataTotal(String sql, Filter filter) {
        sql = "select count(*) from (" + sql + ")  x";
        SQLQuery query = this.getSession().createSQLQuery(sql);
        Map<String, String> map = filter.getFilter();
        for (Entry entry : map.entrySet()) {
            if (!StringUtils.isBlank((String) entry.getValue())) {
                query.setParameter((String) entry.getKey(), entry.getValue());
            }
        }
        return Long.parseLong(query.uniqueResult().toString());
    }

    @Override
    public <T> T findUniqueByMap(Class<T> clazz, Map<String, Object> map) {
        StringBuilder builder = new StringBuilder("From " + clazz.getSimpleName() + " where 1=1 ");
        Iterator<Entry<String, Object>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry<String, Object> entry = iterator.next();
            if (entry.getValue() == null) {
                builder.append(" and ").append(entry.getKey()).append(" is null");
            } else if (entry.getValue().getClass().isArray()) {
                builder.append(" and ").append(entry.getKey()).append(" in :").append(entry.getKey());
            } else {
                builder.append(" and ").append(entry.getKey()).append("=:").append(entry.getKey());
            }
        }
        Query query = this.getSession().createQuery(builder.toString());
        iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry<String, Object> entry = iterator.next();
            if (entry.getValue() == null) {
                builder.append(" ");
            } else if (entry.getValue().getClass().isArray()) {
                query.setParameterList(entry.getKey(), (Object[]) entry.getValue());
            } else {
                query.setParameter(entry.getKey(), entry.getValue());
            }
        }
        return (T) query.uniqueResult();
    }

    @Override
    public <T> List<T> findListByMap(Class<T> clazz, Map<String, Object> map) {
        StringBuilder builder = new StringBuilder("From " + clazz.getSimpleName() + " where 1=1 ");
        Iterator<Entry<String, Object>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry<String, Object> entry = iterator.next();
            if (entry.getValue() == null) {
                builder.append(" and ").append(entry.getKey()).append(" is null");
            } else if (entry.getValue().getClass().isArray()) {
                builder.append(" and ").append(entry.getKey()).append(" in :").append(entry.getKey());
            } else {
                builder.append(" and ").append(entry.getKey()).append("=:").append(entry.getKey());
            }
        }
        Query query = this.getSession().createQuery(builder.toString());

        iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry<String, Object> entry = iterator.next();
            if (entry.getValue() == null) {
                builder.append(" ");
            } else if (entry.getValue().getClass().isArray()) {
                query.setParameterList(entry.getKey(), (Object[]) entry.getValue());
            } else {
                query.setParameter(entry.getKey(), entry.getValue());
            }
        }
        return query.list();
    }

    @Override
    public <T> List<Map<String, Object>> findListMapByMap(String sql, Map<String, Object> map) {
        SQLQuery query = this.getSession().createSQLQuery(sql);
        for (Entry entry : map.entrySet()) {
            query.setParameter(entry.getKey().toString(), entry.getValue());
        }
        query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.list();
    }

    @Override
    public abstract <T> Map<String, Object> findPageInfo(Filter var1, Page var2) throws Exception;

    @Override
    public <T> void delete(Class clazz, boolean inheritBaseEntity, List<T> list) throws Exception {
        if (list != null && list.size() != 0) {
            Long[] ids = new Long[list.size()];
            List<Field> fields = ReflectUtils.getFields(clazz, true);
            boolean idExist = false;
            for (int i = 0; i < list.size(); ++i) {
                T t = list.get(i);
                for (Field field : fields) {
                    field.setAccessible(true);
                    if (field.getName().equalsIgnoreCase("id")) {
                        ids[i] = (Long) field.get(t);
                        idExist = true;
                    }
                }
            }

            if (!idExist) {
                throw new Exception(clazz.getName() + "未找到id属性");
            } else {
                this.delete(clazz, ids);
            }
        }
    }

    @Override
    public <T> void saveList(List<T> list) {
        for (Object o : list) {
            getSession().save(o);
        }
        //缓存写入数据库,一个事务中有一个失败，全部回滚
        getSession().flush();
    }

    @Override
    public void updateList(List<Object> list) {
        for (Object o : list) {
            getSession().update(o);
        }
        getSession().flush();
    }

    @Override
    public <T> void delete(Class<T> clazz, Map<String, Object> condition) {
        StringBuilder builder = new StringBuilder("delete From " + clazz.getSimpleName() + " where 1=1 ");
        Iterator<Entry<String, Object>> iterator = condition.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry<String, Object> entry = iterator.next();
            builder.append(" and ").append(entry.getKey()).append("=:").append(entry.getKey());
        }
        Query query = this.getSession().createQuery(builder.toString());
        iterator = condition.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry<String, Object> entry = iterator.next();
            query.setParameter(entry.getKey(), entry.getValue());
        }
        query.executeUpdate();
    }

    @Override
    public <T> List<T> find(Class<T> clazz, String column, Object value) {
        String hql = "From " + clazz.getSimpleName() + " where " + column + "=:" + column;
        return this.getSession().createQuery(hql).setParameter(column, value).list();
    }

    @Override
    public <T> T findOne(Class<T> clazz, String column, Object value) {
        String hql = "From " + clazz.getSimpleName() + " where " + column + "=:" + column;
        return (T) this.getSession().createQuery(hql).setParameter(column, value).uniqueResult();
    }
}
