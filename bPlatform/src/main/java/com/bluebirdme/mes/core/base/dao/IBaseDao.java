package com.bluebirdme.mes.core.base.dao;

import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author qianchen
 * @date 2020/05/21
 */
public interface IBaseDao {
    <T> void empty(Class<T> clazz) throws Exception;

    void empty(String tableName);

    void save(Object... objects);

    <T> void saveList(List<T> list);

    void updateList(List<Object> objects);

    void merge(Object... object);

    void update(Object... object);

    void update2(Object... object) throws Exception;

    void delete(Object... object);

    <T> void delete(Class<T> clazz, Serializable... ids);

    <T> void delete(Class clazz, boolean isAndCondition, List<T> list) throws Exception;

    <T> List<T> findAll(Class<T> clazz);

    <T> T findById(Class<T> clazz, Serializable id);

    /**
     * @deprecated
     */
    @Deprecated
    <T> boolean isExist(Class<T> clazz, Map<String, Object> condition);

    /**
     * @deprecated
     */
    @Deprecated
    <T> boolean isExist(Class<T> clazz, Map<String, Object> map, Long id) throws Exception;

    <T> boolean isExist(Class<T> clazz, Map<String, Object> map, boolean isAndCondition);

    <T> boolean isExist(Class<T> clazz, Map<String, Object> map, Long id, boolean isAndCondition) throws Exception;

    <T> Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception;

    <T> T findUniqueByMap(Class<T> clazz, Map<String, Object> condition);

    <T> List<T> findListByMap(Class<T> clazz, Map<String, Object> condition);

    List<Map<String, Object>> findListMapByMap(String sql, Map<String, Object> condition);

    <T> void fakeDelete(Class<T> clazz, Serializable... ids);

    <T> void fakeDelete(Class<T> clazz, String ids);

    <T> void delete(Class<T> clazz, String ids);

    <T> void delete(Class<T> clazz, Map<String, Object> condition);

    <T> boolean has(Class<T> clazz, Map<String, Object> condition, Long id);

    <T> boolean has(Class<T> clazz, Map<String, Object> condition);

    <T> List<T> find(Class<T> clazz, String column, Object value);

    <T> T findOne(Class<T> clazz, String column, Object value);
}
