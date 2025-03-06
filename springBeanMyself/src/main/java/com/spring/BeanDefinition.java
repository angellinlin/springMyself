package com.spring;

/**
 * @Author ningxuan
 * @Date 2022/8/22 19:54
 */
public class BeanDefinition {
    // Bean的类型
    private Class type;
    // 作用域
    private String scope;
    // 是否懒加载
    private boolean isLazy;

    public void setType(Class type) {
        this.type = type;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public void setLazy(boolean lazy) {
        isLazy = lazy;
    }

    public Class getType() {
        return type;
    }

    public String getScope() {
        return scope;
    }

    public boolean isLazy() {
        return isLazy;
    }
}
