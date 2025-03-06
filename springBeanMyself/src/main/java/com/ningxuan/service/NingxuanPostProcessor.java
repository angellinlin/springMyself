package com.ningxuan.service;

import com.spring.BeanPostProcessor;

/**
 * @Author ningxuan
 * @Date 2022/8/22 21:14
 */
public class NingxuanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        return null;
    }
}
