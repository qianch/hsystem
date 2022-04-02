package com.bluebirdme.mes.platform.service;

import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.IBaseService;
import com.bluebirdme.mes.core.sql.SQLTemplateException;
import com.bluebirdme.mes.platform.entity.Menu;

import java.util.List;
import java.util.Map;

/**
 * @author qianchen
 * @date 2020/05/21
 */
public interface IMenuService extends IBaseService {
    @Override
    <T> Map<String, Object> findPageInfo(final Filter p0, final Page p1) throws Exception;

    void batchUpdateMenuLevel(final List<Menu> p0);

    void batchDelete(final List<Menu> p0);

    List<Menu> myMenu(final Long p0) throws SQLTemplateException;

    String getMenuCode(final Long p0);

    List<Menu> m();
}
