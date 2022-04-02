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
    <T> void empty(Class<T> var1) throws Exception;

    void empty(String var1);

    void save(Object... var1);

    <T> void saveList(List<T> var1);

    void updateList(List<Object> var1);

    void merge(Object... var1);

    void update(Object... var1);

    void update2(Object... var1) throws Exception;

    void delete(Object... var1);

    <T> void delete(Class<T> var1, Serializable... var2);

    <T> void delete(Class var1, boolean var2, List<T> var3) throws Exception;

    <T> List<T> findAll(Class<T> var1);

    <T> T findById(Class<T> var1, Serializable var2);

    /**
     * @deprecated
     */
    @Deprecated
    <T> boolean isExist(Class<T> var1, Map<String, Object> var2);

    /**
     * @deprecated
     */
    @Deprecated
    <T> boolean isExist(Class<T> var1, Map<String, Object> var2, Long var3) throws Exception;

    <T> boolean isExist(Class<T> var1, Map<String, Object> var2, boolean var3);

    <T> boolean isExist(Class<T> var1, Map<String, Object> var2, Long var3, boolean var4) throws Exception;

    <T> Map<String, Object> findPageInfo(Filter var1, Page var2) throws Exception;

    <T> T findUniqueByMap(Class<T> var1, Map<String, Object> var2);

    <T> List<T> findListByMap(Class<T> var1, Map<String, Object> var2);

    <T> List<Map<String, Object>> findListMapByMap(String var1, Map<String, Object> var2);

    <T> void fakeDelete(Class<T> var1, Serializable... var2);

    <T> void fakeDelete(Class<T> var1, String var2);

    <T> void delete(Class<T> var1, String var2);

    <T> void delete(Class<T> var1, Map<String, Object> var2);

    <T> boolean has(Class<T> var1, Map<String, Object> var2, Long var3);

    <T> boolean has(Class<T> var1, Map<String, Object> var2);

    <T> List<T> find(Class<T> var1, String var2, Object var3);

    <T> T findOne(Class<T> var1, String var2, Object var3);
}
