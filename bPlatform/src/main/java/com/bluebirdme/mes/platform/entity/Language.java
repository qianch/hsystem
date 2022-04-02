package com.bluebirdme.mes.platform.entity;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author qianchen
 * @date 2020/06/30
 */
@Desc("国际化配置")
@Entity
@Table(name = "Platform_Language")
public class Language extends BaseEntity {
    @Desc("标识")
    @Column(name = "languageCode", nullable = false)
    private String languageCode;

    @Desc("中文")
    @Column(name = "chinese", nullable = false)
    private String chinese;

    @Desc("英文")
    @Column(name = "english", nullable = false)
    private String english;

    @Desc("阿拉伯文")
    @Column(name = "arabic", nullable = false)
    private String arabic;

    @Desc("土耳其文")
    @Column(name = "turkey", nullable = false)
    private String turkey;

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setChinese(String chinese) {
        this.chinese = chinese;
    }

    public String getChinese() {
        return chinese;
    }

    public void setEnglish(String english) {
        this.english = english;
    }

    public String getEnglish() {
        return english;
    }

    public void setArabic(String arabic) {
        this.arabic = arabic;
    }

    public String getArabic() {
        return arabic;
    }

    public String getTurkey() {
        return turkey;
    }

    public void setTurkey(String turkey) {
        this.turkey = turkey;
    }
}
