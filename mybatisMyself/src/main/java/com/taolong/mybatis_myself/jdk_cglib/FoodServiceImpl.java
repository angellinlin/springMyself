package com.taolong.mybatis_myself.jdk_cglib;

/**
 * @author zhouguilong6
 * @date 2025/3/6 18:19
 */
//代理类，实现定义的接口
public class FoodServiceImpl implements FoodService {
    @Override
    public void makeNoodle() {
        System.out.println("make noodle");
    }

    @Override
    public void makeChicken() {
        System.out.println("make Chicken");
    }
}