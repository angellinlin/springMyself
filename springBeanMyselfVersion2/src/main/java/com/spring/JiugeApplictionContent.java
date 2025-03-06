package com.spring;


import java.beans.Introspector;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class JiugeApplictionContent {


    private ConcurrentHashMap<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();

    private ConcurrentHashMap<String, Object> singletonObjects = new ConcurrentHashMap<>();

    private List<BeanPostProcessor> beanPostProcessorList = new ArrayList<>();

    /**
     * Spring 启动流程
     *
     * @param appconfigClass
     */
    public JiugeApplictionContent(Class appconfigClass) {
        // 扫描类，扫描路径 得到BeanDefination
        scan(appconfigClass);

        // 实例化非懒加载的单例bean
        // 1.实例化
        // 2.属性填充
        // 3.Aware回调
        // 4.初始化
        // 5.添加到单例池

        instanceSingletonBean();


    }

    private void instanceSingletonBean() {
        for (String beanName : beanDefinitionMap.keySet()) {
            BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
            if (beanDefinition.scope.equals("singleton")) {
                Object bean = doCreateBean(beanName, beanDefinition);

                singletonObjects.put(beanName, bean);
            }

        }
    }

    private Object doCreateBean(String beanName, BeanDefinition beanDefinition) {
        Class beanClass = beanDefinition.getBeanClass();
        try {
            // 实例化
            Constructor declaredConstructor = beanClass.getDeclaredConstructor();
            Object instance = declaredConstructor.newInstance();

            // 填充属性
            Field[] declaredFields = beanClass.getDeclaredFields();
            for (Field field : declaredFields) {
                if (field.isAnnotationPresent(Autowired.class)) {
                    String fieldName = field.getName();

                    Object bean = getBean(fieldName);
                    field.setAccessible(true);
                    field.set(instance, bean);
                }
            }


            // Aware 回调，获取bean的名字
            if (instance instanceof BeanNameAware) {
                ((BeanNameAware) instance).setBeanName(beanName);
            }

            // 初始化前置处理器
            for (BeanPostProcessor beanPostProcessor : beanPostProcessorList) {
                instance = beanPostProcessor.postProcessBeforeInitialization(beanName, instance);
            }

            // 初始化
            if (instance instanceof InitializingBean) {
                ((InitializingBean) instance).afterPropertiesSet();
            }


            // 实现后置处理器
            for (BeanPostProcessor beanPostProcessor : beanPostProcessorList) {
                instance = beanPostProcessor.postProcessAfterInitialization(beanName, instance);
            }


            return instance;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;

    }


    private void scan(Class appconfigClass) {
        // 扫描class, 转化为BeanDefinition对象，添加到BeanDefinitionMap 中

        // 先得到包路径
        CompontentScan compontentScan = (CompontentScan) appconfigClass.getAnnotation(CompontentScan.class);
        String packagePath = compontentScan.value();

        // 得到包路径
        System.out.println("packagePath == " + packagePath);

        // 扫描包路径得到 classList
        List<Class> classList = getBeanClasses(packagePath);

        // 解析得到BeanDefinition
        for (Class clazz : classList) {
            if (clazz.isAnnotationPresent(Compontent.class)) {
                BeanDefinition beanDefinition = new BeanDefinition();
                beanDefinition.setBeanClass(clazz);

                // 要让Spring 自动生效，要么Component注解上获取 bean 的名字
                Compontent compontent = (Compontent) clazz.getAnnotation(Compontent.class);

                String beanName = compontent.value();


                if (BeanPostProcessor.class.isAssignableFrom(clazz)) {
                    try {
                        BeanPostProcessor instance = (BeanPostProcessor) clazz.getDeclaredConstructor().newInstance();
                        beanPostProcessorList.add(instance);
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                }

                if ("".equals(beanName)) {
                    beanName = Introspector.decapitalize(clazz.getSimpleName());
                }

                // 解析scope
                if (clazz.isAnnotationPresent(Scope.class)) {
                    Scope scope = (Scope) clazz.getAnnotation(Scope.class);
                    String scopeValue = scope.value();
                    // System.out.println(" 是否是单例 == " + scopeValue);
                    if ("singleton".equals(scopeValue)) {
                        beanDefinition.setScope("singleton");
                    } else {
                        beanDefinition.setScope("prototype");
                    }
                } else {
                    beanDefinition.setScope("singleton");
                    // System.out.println(" 进入多例 ");
                }
                beanDefinitionMap.put(beanName, beanDefinition);
            }
        }
    }

    private List<Class> getBeanClasses(String packagePath) {
        List<Class> beanClasses = new ArrayList<>();

        ClassLoader classLoader = JiugeApplictionContent.class.getClassLoader();
        packagePath = packagePath.replace(".", "/");

        URL resource = classLoader.getResource(packagePath);

        File file = new File(resource.getFile());
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                String absolutePath = f.getAbsolutePath();
                System.out.println(" filename == " + absolutePath);
                if (absolutePath.endsWith(".class")) {
                    String className = absolutePath.substring(absolutePath.indexOf("com"), absolutePath.indexOf(".class"));
                    System.out.println(" before className == " + className);
                    className = className.replace("\\", ".");
                    System.out.println(" after className == " + className);
                    try {
                        Class<?> clazz = classLoader.loadClass(className);
                        beanClasses.add(clazz);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }


                }

            }

        }

        return beanClasses;
    }

    public Object getBean(String beanName) {
        if (singletonObjects.containsKey(beanName)) {
            return singletonObjects.get(beanName);
        } else {
            BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
            return doCreateBean(beanName, beanDefinition);
        }
    }
}
