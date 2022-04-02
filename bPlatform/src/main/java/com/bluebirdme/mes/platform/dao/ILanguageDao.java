package com.bluebirdme.mes.platform.dao;

import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.sql.SQLTemplateException;
import com.bluebirdme.mes.platform.entity.Language;

import java.util.List;

/**
 * @author qianchen
 * @date 2020/06/30
 */
public interface ILanguageDao extends IBaseDao {
    public List<Language> queryLanguageList(String languageCode) throws SQLTemplateException;
}
