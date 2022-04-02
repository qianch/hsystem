package com.bluebirdme.mes.core.interceptor;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.valid.ValidException;
import com.bluebirdme.mes.core.valid.ValidatorFactory;
import com.bluebirdme.mes.core.valid.annotations.Valid;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Resource;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.MethodParameter;
import org.springframework.web.method.HandlerMethod;
/**
 * @author qianchen
 * @date 2020/05/21
 */

/**
 * @deprecated
 */
@Deprecated
public class ParameterValidation {
    @Resource
    BeanFactory beanFactory;
    @Resource
    ValidatorFactory factory;
    Map<Class<? extends Annotation>, Class<?>> validators = null;
    private static LocalVariableTableParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();

    public ParameterValidation() {
    }

    @Before("@annotation(com.bluebirdme.mes.core.valid.annotations.Valid)")
    public void beforeExec(JoinPoint joinPoint) throws IllegalArgumentException, IllegalAccessException, ValidException, InstantiationException {
        MethodSignature ms = (MethodSignature) joinPoint.getSignature();
        Method method = ms.getMethod();
        Class<? extends Object> clazz = joinPoint.getTarget().getClass();
        String beanName = clazz.getSimpleName().replace(clazz.getSimpleName().substring(0, 1), clazz.getSimpleName().substring(0, 1).toLowerCase());
        HandlerMethod handlerMethod = new HandlerMethod(beanName, this.beanFactory, method);
        if (this.validators == null) {
            this.validators = this.factory.getValidators();
        }

        Object[] args = joinPoint.getArgs();
        for (int i = 0; i < handlerMethod.getMethodParameters().length; ++i) {
            handlerMethod.getMethodParameters()[i].initParameterNameDiscovery(discoverer);
            if (handlerMethod.getMethodParameters()[i].getParameterType().getAnnotation(Valid.class) != null) {
                this.validEntity(args[i]);
            } else {
                this.validMember(handlerMethod.getMethodParameters()[i], args[i]);
            }
        }
    }

    public <T> void validEntity(T t) throws IllegalArgumentException, IllegalAccessException, InstantiationException, ValidException {
        Field[] fields = t.getClass().getDeclaredFields();
        int length = fields.length;

        for (int i = 0; i < length; ++i) {
            Field field = fields[i];
            field.setAccessible(true);
            Iterator it = this.validators.entrySet().iterator();

            while (it.hasNext()) {
                Entry<Class<? extends Annotation>, Class<?>> ent = (Entry) it.next();
                if (field.getAnnotation((Class) ent.getKey()) != null) {
                    this.factory.getValidator(ent.getKey(), field.getType()).doValid(field.get(t), field.getAnnotation(Desc.class) != null ? field.getAnnotation(Desc.class).value() : field.getName(), field.getAnnotation((Class) ent.getKey()));
                }
            }
        }
    }

    public void validMember(MethodParameter param, Object value) throws InstantiationException, IllegalAccessException, ValidException {
        Iterator it = this.validators.entrySet().iterator();

        while (it.hasNext()) {
            Entry<Class<? extends Annotation>, Class<?>> ent = (Entry) it.next();
            if (param.getParameterAnnotation((Class) ent.getKey()) != null) {
                this.factory.getValidator(ent.getKey(), param.getParameterType()).doValid(value, param.getParameterAnnotation(Desc.class) != null ? param.getParameterAnnotation(Desc.class).value() : param.getParameterName(), param.getParameterAnnotation((Class) ent.getKey()));
            }
        }
    }
}
