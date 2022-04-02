package com.bluebirdme.mes.platform.service.impl;

import com.bluebirdme.mes.core.annotation.AnyExceptionRollback;
import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.core.sql.SQLTemplateException;
import com.bluebirdme.mes.platform.dao.ILanguageDao;
import com.bluebirdme.mes.platform.entity.Language;
import com.bluebirdme.mes.platform.service.ILanguageService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author qianchen
 * @date 2020/06/30
 */
@Service
@AnyExceptionRollback
public class LanguageServiceImpl extends BaseServiceImpl implements ILanguageService {
    @Resource
    ILanguageDao languageDaoDao;

    @Override
    protected IBaseDao getBaseDao() {
        return languageDaoDao;
    }

    @Override
    public <T> Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
        return languageDaoDao.findPageInfo(filter, page);
    }

    @Override
    public List<Language> queryLanguageList(String languageCode) throws SQLTemplateException {
        return languageDaoDao.queryLanguageList(languageCode);
    }
}
