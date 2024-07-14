package com.example.aopstudy.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created with IntelliJ IDEA.
 * @Author: HuLiang
 * @Date: 2024/07/13/21:18
 * @Description:
 */

@Retention(RetentionPolicy.RUNTIME)
public @interface Aspect {
    String value() default "";

}
