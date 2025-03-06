package com.jiuge.server;


import com.spring.BeanNameAware;
import com.spring.Compontent;
import com.spring.InitializingBean;
import com.spring.Scope;

@Compontent("userServer")
@Scope
public class UserServer implements InitializingBean, BeanNameAware {


    @JiugeValue("helloWorld")
    private String helloWorld;


    private String beanName;

    public void test(){
        System.out.println("hello userServer " + helloWorld + " beanName = " +beanName);
    }

    @Override
    public void afterPropertiesSet() {
        System.out.println("我在后面输出");
    }

    @Override
    public void setBeanName(String name) {
        this.beanName = name;
    }
}
