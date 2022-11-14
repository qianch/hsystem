package com.bluebirdme.mes.core.base.service;

import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import org.springframework.cache.annotation.Cacheable;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author qianchen
 * @date 2020/05/21
 */

public interface IBaseService {
    <T> void empty(final Class<T> p0) throws Exception;

    void empty(final String p0);

    void save(final Object... p0);

    <T> void saveList(final List<T> p0);

    void merge(final Object... p0);

    void update(final Object... p0);

    void updateList(final List<Object> p0);

    <T> void delete(final Class<T> p0, final String p1);

    void update2(final Object... p0) throws Exception;

    void delete(final Object... p0);

    <T> void delete(final Class<T> p0, final Map<String, Object> p1);

    <T> void delete(final Class<T> p0, final Serializable... p1);

    <T> List<T> findAll(final Class<T> p0);

    @Cacheable(value = {"core"}, key = "#id")
    <T> T findById(final Class<T> p0, final Serializable p1);

    @Deprecated
    <T> boolean isExist(final Class<T> p0, final Map<String, Object> p1);

    @Deprecated
    <T> boolean isExist(final Class<T> p0, final Map<String, Object> p1, final Long p2) throws Exception;

    Map<String, Object> findPageInfo(final Filter p0, final Page p1) throws Exception;

    <T> T findUniqueByMap(final Class<T> p0, final Map<String, Object> p1);

    <T> List<T> findListByMap(final Class<T> p0, final Map<String, Object> p1);

    <T> void delete(final Class p0, final boolean p1, final List<T> p2) throws Exception;

    <T> void fakeDelete(final Class<T> p0, final Serializable... p1);

    <T> void fakeDelete(final Class<T> p0, final String p1);

    <T> boolean isExist(final Class<T> p0, final Map<String, Object> p1, final boolean p2);

    <T> boolean isExist(final Class<T> p0, final Map<String, Object> p1, final Long p2, final boolean p3) throws Exception;

    <T> boolean has(final Class<T> p0, final Map<String, Object> p1, final Long p2);

    <T> boolean has(final Class<T> p0, final Map<String, Object> p1);

    <T> List<T> find(final Class<T> p0, final String p1, final Object p2);

    <T> T findOne(final Class<T> p0, final String p1, final Object p2);
}
