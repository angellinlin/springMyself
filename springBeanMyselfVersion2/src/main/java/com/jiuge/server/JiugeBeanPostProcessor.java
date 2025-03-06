package com.jiuge.server;

import com.spring.BeanPostProcessor;
import com.spring.Compontent;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author Administrator
 */
@Compontent
public class JiugeBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(String name, Object bean) {

        for(Field field: bean.getClass().getDeclaredFields()){
            if(field.isAnnotationPresent(JiugeValue.class)){
                field.setAccessible(true);

                try {
                    field.set(bean,field.getAnnotation(JiugeValue.class).value());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(String beanName, Object instance) {
        if ("orderServer".equals(beanName)) {
            System.out.println("postProcessAfterInitialization");

            Object proxy = Proxy.newProxyInstance(JiugeBeanPostProcessor.class.getClassLoader(), instance.getClass().getInterfaces(), new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    System.out.println("切面逻辑");
                    return method.invoke(instance, args);
                }
            });
            return proxy;
        }
        return instance;
    }
}
