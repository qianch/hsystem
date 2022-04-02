package com.bluebirdme.mes.platform.service;

import com.bluebirdme.mes.core.base.service.IBaseService;
import com.bluebirdme.mes.core.sql.SQLTemplateException;
import com.bluebirdme.mes.platform.entity.Language;

import java.util.List;

/**
 * @author qianchen
 * @date 2020/06/30
 */
public interface ILanguageService extends IBaseService {
    public List<Language> queryLanguageList(String languageCode) throws SQLTemplateException;
}
