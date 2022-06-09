package com.bluebirdme.mes.core.spring;

import com.bluebirdme.mes.platform.entity.Language;
import com.bluebirdme.mes.platform.service.ILanguageService;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.context.MessageSource;
import org.springframework.context.support.AbstractMessageSource;
import org.xdemo.superutil.j2se.StringUtils;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author qianchen
 * @date 2020/06/30
 */
public class BMessageSource extends AbstractMessageSource implements MessageSource, BeanClassLoaderAware {
    @Resource
    ILanguageService languageService;

    /**
     * 存放所有国际化数据
     */
    private final static Map<String, String> CHINESE_MAP = Maps.newHashMap();
    private final static Map<String, String> ENGLISH_MAP = Maps.newHashMap();
    private final static Map<String, String> ARABIC_MAP = Maps.newHashMap();
    private final static Map<String, String> TURKEY_MAP = Maps.newHashMap();

    public BMessageSource() {
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        List<Language> languages = languageService.findAll(Language.class);
        CHINESE_MAP.clear();
        ENGLISH_MAP.clear();
        ARABIC_MAP.clear();
        TURKEY_MAP.clear();
        for (Language language : languages) {
            CHINESE_MAP.put(language.getLanguageCode(), language.getChinese());
            ENGLISH_MAP.put(language.getLanguageCode(), language.getEnglish());
            ARABIC_MAP.put(language.getLanguageCode(), language.getArabic());
            TURKEY_MAP.put(language.getLanguageCode(), language.getTurkey());
        }
    }

    @Override
    protected MessageFormat resolveCode(String code, Locale locale) {
        String msg = getText(code, locale);
        MessageFormat result = createMessageFormat(msg, locale);
        return result;
    }

    private String getText(String code, Locale locale) {
        String languageTag = locale.toLanguageTag();
        String localeText = "";
        if ("zh-CN".equalsIgnoreCase(languageTag)) {
            localeText = CHINESE_MAP.get(code);
        }

        if ("en-US".equalsIgnoreCase(languageTag)) {
            localeText = ENGLISH_MAP.get(code);
        }

        if ("ar-EG".equalsIgnoreCase(languageTag)) {
            localeText = ARABIC_MAP.get(code);
        }

        if ("tr-TR".equalsIgnoreCase(languageTag)) {
            localeText = TURKEY_MAP.get(code);
        }

        if (StringUtils.isEmpty(localeText)) {
            if (getParentMessageSource() != null) {
                localeText = getParentMessageSource().getMessage(code, null, code, locale);
                return localeText;
            }
        } else {
            return localeText;
        }

        return code;
    }
}
