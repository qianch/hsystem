package com.bluebirdme.mes.core.base.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.ParseException;
import org.springframework.util.StringUtils;

import java.beans.PropertyEditorSupport;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author qianchen
 * @date 2020/05/21
 */
public class DateEditor extends PropertyEditorSupport {
    private static final Logger logger = LoggerFactory.getLogger(DateEditor.class);
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final DateFormat TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private DateFormat dateFormat;
    private boolean allowEmpty = true;

    public DateEditor() {
    }

    public DateEditor(DateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }

    public DateEditor(DateFormat dateFormat, boolean allowEmpty) {
        this.dateFormat = dateFormat;
        this.allowEmpty = allowEmpty;
    }

    public void setAsText(String text) throws IllegalArgumentException {
        if (allowEmpty && !StringUtils.hasText(text)) {
            this.setValue(null);
        } else {
            try {
                if (dateFormat != null) {
                    setValue(this.dateFormat.parse(text));
                } else if (text.contains(":")) {
                    setValue(TIME_FORMAT.parse(text));
                } else {
                    setValue(DATE_FORMAT.parse(text));
                }
            } catch (ParseException parseException) {
                throw new IllegalArgumentException("不能格式化日期: " + parseException.getMessage(), parseException);
            } catch (java.text.ParseException parseException) {
                logger.error(parseException.getLocalizedMessage(), parseException);
            }
        }
    }

    public String getAsText() {
        Date value = (Date) this.getValue();
        DateFormat dateFormat = this.dateFormat;
        if (dateFormat == null) {
            dateFormat = TIME_FORMAT;
        }
        return value != null ? dateFormat.format(value) : "";
    }
}
