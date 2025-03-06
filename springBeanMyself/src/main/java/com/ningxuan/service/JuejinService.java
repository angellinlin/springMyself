package com.ningxuan.service;

import com.spring.Autowired;
import com.spring.Component;
import com.spring.InitializingBean;

/**
 * @Author ningxuan
 * @Date 2022/8/21 22:15
 */
@Component("juejinService")
public class JuejinService implements InitializingBean {

    @Autowired
    private OrderService orderService;


    public void test(){
        System.out.println("juejinService执行");
        System.out.println(orderService);
    }

    @Override
    public void afterPropertiesSet() {
        System.out.println("初始化");
    }
}
