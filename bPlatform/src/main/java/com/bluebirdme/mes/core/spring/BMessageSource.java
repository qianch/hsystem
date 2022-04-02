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
    private final static Map<String, String> chineseMap = Maps.newHashMap();
    private final static Map<String, String> englishMap = Maps.newHashMap();
    private final static Map<String, String> arabicMap = Maps.newHashMap();
    private final static Map<String, String> turkeyMap = Maps.newHashMap();

    public BMessageSource() {
    }


    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        List<Language> languages = languageService.findAll(Language.class);
        chineseMap.clear();
        englishMap.clear();
        arabicMap.clear();
        for (Language language : languages) {
            chineseMap.put(language.getLanguageCode(), language.getChinese());
            englishMap.put(language.getLanguageCode(), language.getEnglish());
            arabicMap.put(language.getLanguageCode(), language.getArabic());
            turkeyMap.put(language.getLanguageCode(), language.getTurkey());
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
        if (languageTag.equalsIgnoreCase("zh-CN")) {
            localeText = chineseMap.get(code);
        }

        if (languageTag.equalsIgnoreCase("en-US")) {
            localeText = englishMap.get(code);
        }

        if (languageTag.equalsIgnoreCase("ar-EG")) {
            localeText = arabicMap.get(code);
        }

        if (languageTag.equalsIgnoreCase("tr-TR")) {
            localeText = turkeyMap.get(code);
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
