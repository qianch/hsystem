package com.bluebirdme.mes.platform.dao;

import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.sql.SQLTemplateException;
import com.bluebirdme.mes.platform.entity.Dict;

import java.util.List;
import java.util.Map;

/**
 * @author qianchen
 * @date 2020/05/21
 */
public interface IDictDao extends IBaseDao {
    void delete(final String p0);

    List<Map<String, Object>> queryDict(String rootcode) throws SQLTemplateException;

    List<Map<String, String>> queryDictAll() throws SQLTemplateException;

    List<Map<String, Object>> combotree(final String p0, final String p1) throws SQLTemplateException;

    List<Map<String, Object>> findByCode(final String p0);

    void update(final Dict p0);
}
