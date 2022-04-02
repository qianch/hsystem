package com.bluebirdme.mes.core.valid;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

/**
 * @author qianchen
 * @date 2020/05/21
 */
public class ValidatorFactory {
    private Map<Class<? extends Annotation>, Class<?>> validators;
    private Map<Class<? extends Annotation>, Object> validatorObjects = new HashMap();

    public ValidatorFactory() {
    }

    public Map<Class<? extends Annotation>, Class<?>> getValidators() {
        return this.validators;
    }

    public void setValidators(Map<Class<? extends Annotation>, Class<?>> validators) {
        this.validators = validators;
    }

    public Validator getValidator(Class<? extends Annotation> clazz, Class<?> targetClass) throws InstantiationException, IllegalAccessException, ValidException {
        Validator v;
        if (this.validatorObjects.get(clazz) != null) {
            v = (Validator) this.validatorObjects.get(clazz);
        } else {
            v = (Validator) ((Class) this.getValidators().get(clazz)).newInstance();
            this.validatorObjects.put(clazz, v);
        }

        Class<?>[] cs = v.supportClass();
        if (cs != null && cs.length != 0) {
            boolean support = false;
            int length = cs.length;

            for (Class<?> _clazz : cs) {
                if (targetClass.equals(_clazz)) {
                    support = true;
                }
            }

            if (!support) {
                throw new ValidException("不支持在类" + targetClass.getSimpleName() + "上调用注解@" + clazz.getSimpleName());
            } else {
                return v;
            }
        } else {
            return v;
        }
    }
}
