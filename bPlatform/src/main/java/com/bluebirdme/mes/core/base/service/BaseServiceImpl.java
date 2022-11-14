package com.bluebirdme.mes.core.base.service;

import com.bluebirdme.mes.core.annotation.AnyExceptionRollback;
import com.bluebirdme.mes.core.base.LanguageProvider;
import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author qianchen
 * @date 2020/05/21
 */
@Service
@AnyExceptionRollback
public abstract class BaseServiceImpl extends LanguageProvider implements IBaseService {
    protected abstract IBaseDao getBaseDao();

    @Override
    public <T> void empty(final Class<T> clazz) throws Exception {
        getBaseDao().empty(clazz);
    }

    @Override
    public void empty(final String tableName) {
        getBaseDao().empty(tableName);
    }

    @Override
    public void save(final Object... object) {
        getBaseDao().save(object);
    }

    @Deprecated
    @Override
    public void merge(final Object... object) {
        getBaseDao().merge(object);
    }

    @Override
    public void update(final Object... object) {
        getBaseDao().update(object);
    }

    @Override
    public void update2(final Object... object) throws Exception {
        getBaseDao().update2(object);
    }

    @Override
    public <T> void delete(final Class<T> clazz, final String ids) {
        getBaseDao().delete(clazz, ids);
    }

    @Override
    public void delete(final Object... object) {
        final SpelExpressionParser p = new SpelExpressionParser();
        getBaseDao().delete(object);
    }

    @Override
    public <T> void delete(final Class<T> clazz, final Serializable... ids) {
        getBaseDao().delete(clazz, ids);
    }

    @Override
    public <T> List<T> findAll(final Class<T> clazz) {
        return getBaseDao().findAll(clazz);
    }

    @Override
    public <T> T findById(final Class<T> clazz, final Serializable id) {
        return getBaseDao().findById(clazz, id);
    }

    @Override
    public <T> boolean isExist(final Class<T> clazz, final Map<String, Object> map) {
        return getBaseDao().isExist(clazz, map);
    }

    @Override
    public <T> boolean isExist(final Class<T> clazz, final Map<String, Object> map, final Long id) throws Exception {
        return getBaseDao().isExist(clazz, map, id);
    }

    @Override
    public <T> boolean isExist(final Class<T> clazz, final Map<String, Object> map, final boolean isAndCondition) {
        return getBaseDao().isExist(clazz, map, isAndCondition);
    }

    @Override
    public <T> boolean isExist(final Class<T> clazz, final Map<String, Object> map, final Long id, final boolean isAndCondition) throws Exception {
        return getBaseDao().isExist(clazz, map, id, isAndCondition);
    }
    
    @Override
    public Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
        return getBaseDao().findPageInfo(filter, page);
    }

    @Override
    public <T> T findUniqueByMap(final Class<T> clazz, final Map<String, Object> map) {
        return getBaseDao().findUniqueByMap(clazz, map);
    }

    @Override
    public <T> List<T> findListByMap(final Class<T> clazz, final Map<String, Object> map) {
        return getBaseDao().findListByMap(clazz, map);
    }

    public List<Map<String, Object>> findListMapByMap(final String sql, final Map<String, Object> map) {
        return getBaseDao().findListMapByMap(sql, map);
    }

    @Override
    public <T> void delete(final Class clazz, final boolean inheritBaseEntity, final List<T> list) throws Exception {
        getBaseDao().delete(clazz, inheritBaseEntity, list);
    }

    @Override
    public <T> void fakeDelete(final Class<T> clazz, final Serializable... ids) {
        getBaseDao().fakeDelete(clazz, ids);
    }

    @Override
    public <T> void fakeDelete(final Class<T> clazz, final String ids) {
        getBaseDao().fakeDelete(clazz, ids);
    }

    @Override
    public <T> void saveList(final List<T> list) {
        getBaseDao().saveList(list);
    }

    @Override
    public void updateList(final List<Object> list) {
        getBaseDao().update(list);
    }

    @Override
    public <T> void delete(final Class<T> clazz, final Map<String, Object> condition) {
        getBaseDao().delete(clazz, condition);
    }

    @Override
    public <T> boolean has(final Class<T> clazz, final Map<String, Object> map, final Long id) {
        return getBaseDao().has(clazz, map, id);
    }

    @Override
    public <T> boolean has(final Class<T> clazz, final Map<String, Object> map) {
        return getBaseDao().has(clazz, map);
    }

    @Override
    public <T> List<T> find(final Class<T> clazz, final String column, final Object value) {
        return getBaseDao().find(clazz, column, value);
    }

    @Override
    public <T> T findOne(final Class<T> clazz, final String column, final Object value) {
        return getBaseDao().findOne(clazz, column, value);
    }
}
