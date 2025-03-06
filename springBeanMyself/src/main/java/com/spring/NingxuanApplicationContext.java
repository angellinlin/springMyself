package com.spring;

import java.beans.Introspector;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.*;

/**
 * @Author ningxuan
 * @Date 2022/8/22 19:02
 */
public class NingxuanApplicationContext {

    private Class appConfig;
    private Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();
    // 单例池 存储单例对象
    private Map<String, Object > singletonObjects = new HashMap<>();
    // 缓存
    private List<BeanPostProcessor> beanPostProcessorList = new ArrayList<>();

    // 创建单例bean
    public NingxuanApplicationContext(Class appConfig){
        this.appConfig = appConfig;

        /*
            扫描

            1. 获取扫描路径
            2. 取出扫描路径下的类
            3. 遍历路径下的类, 判断是否有 @Component注解
            4. 根据 Bean信息生成 BeanDefinition类, 存入 beanDefinitionMap
         */
        scan(appConfig);

        // 获取所有的单例bean
        // 遍历 beanDefinitionMap
        for (Map.Entry<String, BeanDefinition> entry : beanDefinitionMap.entrySet()) {
            String beanName = entry.getKey();
            BeanDefinition definition = entry.getValue();
            // 判断是否为单例Bean
            if (definition.getScope().equals("singleton")){
                // 创建bean
                Object bean = createBean(beanName, definition);
                singletonObjects.put(beanName, bean);
            }

        }


    }

    // 创建bean的方法
    private Object createBean(String beanName, BeanDefinition beanDefinition){
        // 创建一个Bean
        Class clazz = beanDefinition.getType();
        Object o = null;
        try {
            // 得到实例
             o = clazz.getConstructor().newInstance();

             // 依赖注入
            // 遍历属性
            for (Field field : clazz.getDeclaredFields()) {
                // 判断属性上是否存在 Autowired注解
                if (field.isAnnotationPresent(Autowired.class)) {
                    // 反射
                    field.setAccessible(true);
                    // 最简单的实现, 通过属性名字去找 Bean
                    field.set(o, getBean(field.getName()));
                }

            }
            // 初始化
            // 判断bean是否实现了 InitializingBean接口
            if (o instanceof InitializingBean){
                // 实现了则执行
                ((InitializingBean) o).afterPropertiesSet();
            }


            // 遍历BeanPostProcessorList  ==> AOP
            for (BeanPostProcessor beanPostProcessor : beanPostProcessorList) {
                // 调用初始化后
                o = beanPostProcessor.postProcessAfterInitialization(o, beanName);
            }

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return o;
    }

    private void scan(Class appConfig) {
        if (appConfig.isAnnotationPresent(ComponentScan.class)){
            // 拿出ComponentScan注解定义的扫描路径
            ComponentScan componentScan = (ComponentScan) appConfig.getAnnotation(ComponentScan.class);
            String path = componentScan.value();
            System.out.println(path);  // com.ningxuan.service

            // 把 '.' 替换为 '/'
            path = path.replace(".", "/"); // com/ningxaun/service

            ClassLoader classLoader = NingxuanApplicationContext.class.getClassLoader();
            URL resource = classLoader.getResource(path);
            // 获取文件目录
            File file = new File(resource.getFile());

            // 判断目录是否存在
            if (file.isDirectory()){
                // 遍历该目录下的文件
                for (File f : file.listFiles()) {
                    String absolutePath = f.getAbsolutePath();  // D:\project-test\demo\target\classes\com\ningxuan\service\JuejinService.class
                    System.out.println(absolutePath);

                    // 判断文件内是否有 @Component注解
                    // 获取类的class, 之后判断类中是否有@Component注解

                    // 把 '/' 替换为 '.'
                    absolutePath = absolutePath.replace("\\", ".");
                    // 获取相对地址
                    String[] split = absolutePath.split("classes.");
                    System.out.println(Arrays.toString(split));
                    String substring = split[1].substring(0, split[1].length() - 6);
                    System.out.println(substring);  // com.ningxuan.service.JuejinService
                    try {
                        Class<?> clazz = classLoader.loadClass(substring);
                        // 判断类里是否有 @Component注解
                        if (clazz.isAnnotationPresent(Component.class)){

                            // 判断类是否实现了 BeanPostProcessor接口
                            if (BeanPostProcessor.class.isAssignableFrom(clazz)) {
                                // 若实现了, 则实例化出来
                                BeanPostProcessor instance = (BeanPostProcessor) clazz.getConstructor().newInstance();
                                // 加入list
                                beanPostProcessorList.add(instance);
                            }

                            // 获取bean的名字
                            String beanName = clazz.getAnnotation(Component.class).value();
                            // 如果没有设置 beanName
                            if ("".equals(beanName)){
                                // 获取属性名字
                                beanName = Introspector.decapitalize(clazz.getSimpleName());
                            }

                            // Bean
                            BeanDefinition beanDefinition = new BeanDefinition();
                            beanDefinition.setType(clazz);
                            // 判断Bean是否为单例Bean
                            if (clazz.isAnnotationPresent(Scope.class)){
                                Scope scopeAnnotation = clazz.getAnnotation(Scope.class);
                                String value = scopeAnnotation.value();
                                // 判断Scope的类型
                                beanDefinition.setScope(value);
                            }else{
                                // 单例
                                beanDefinition.setScope("singleton");
                            }
                            // 将bean存储到 beanDefinitionMap中
                            beanDefinitionMap.put(beanName, beanDefinition);
                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public Object getBean(String beanName) {
        // 判断 BeanName是否存在于 beanDefinitionMap
        if (!beanDefinitionMap.containsKey(beanName)){
            // 若该Bean不存在与 beanDefinitionMap中, 表示传入的 beanName是错误的
            // 空指针异常
            throw new NullPointerException();
        }
        // 如果 beanName存在
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        // 判断是否为单例Bean
        if (beanDefinition.getScope().equals("singleton")){
            // 若为单例bean, 则向单例池中取值
            Object singletonBean = singletonObjects.get(beanName);
            // 依赖注入过程中获取 bean的时候 singletonBean可能为 null, 这个时候需要去存一下
            if (singletonBean == null){
                singletonBean = createBean(beanName, beanDefinition);
                singletonObjects.put(beanName, singletonBean);
            }
            return singletonBean;

        }else{
            // 原型bean
            return createBean(beanName, beanDefinition);
        }
    }
}
