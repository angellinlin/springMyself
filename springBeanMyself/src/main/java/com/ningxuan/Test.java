package com.ningxuan;

import com.spring.NingxuanApplicationContext;

/**
 * @Author ningxuan
 * @Date 2022/8/21 22:15
 */
public class Test {

    public static void main(String[] args) {

        // 创建非懒加载的单例bean
        NingxuanApplicationContext context = new NingxuanApplicationContext(AppConfig.class);
        System.out.println("================================================");
        // 懒加载的bean在这里加载
//        JuejinService juejinService = (JuejinService) context.getBean("juejinService");
        System.out.println(context.getBean("juejinService"));
        System.out.println(context.getBean("juejinService"));
        System.out.println(context.getBean("juejinService"));
//        juejinService.test();
    }

}
