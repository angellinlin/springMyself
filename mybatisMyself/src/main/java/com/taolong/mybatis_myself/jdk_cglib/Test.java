package com.taolong.mybatis_myself.jdk_cglib;

/**
 * @author zhouguilong6
 * @date 2025/3/6 18:25
 */
public class Test {
    public static void main(String[] args) {
        // 1.创建对象
        FoodServiceImpl foodService = new FoodServiceImpl();
        // 2.创建代理对象
        JDKProxyFactory proxy = new JDKProxyFactory(foodService);
        // 3.调用代理对象的增强方法,得到增强后的对象
        FoodService createProxy = (FoodService) proxy.createProxy();
        createProxy.makeChicken();
    }
}
