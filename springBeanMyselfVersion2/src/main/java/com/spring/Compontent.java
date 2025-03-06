package com.spring;

import java.lang.annotation.*;

/**
 * @author Administrator
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Compontent {
    String value() default "";
}
