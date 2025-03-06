package com.spring;

public interface BeanPostProcessor {

    Object postProcessBeforeInitialization(String name, Object bean);

    Object postProcessAfterInitialization(String beanName, Object instance);
}
