package com.bluebirdme.mes.platform.dao;

import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.sql.SQLTemplateException;
import com.bluebirdme.mes.platform.entity.Menu;

import java.util.List;
import java.util.Map;

/**
 * @author qianchen
 * @date 2020/05/21
 */

public interface IMenuDao extends IBaseDao {
    @Override
    <T> Map<String, Object> findPageInfo(final Filter filter, final Page page) throws Exception;

    void batchUpdateMenuLevel(final List<Menu> p0);

    void batchDelete(final List<Menu> p0);

    List<Menu> myMenu(final Long p0) throws SQLTemplateException;

    String getMenuCode(final Long p0);

    List<Menu> m();
}
