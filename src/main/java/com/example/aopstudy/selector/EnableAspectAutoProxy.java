package com.example.aopstudy.selector;

import org.springframework.context.annotation.Import;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created with IntelliJ IDEA.
 * @Author: HuLiang
 * @Date: 2024/07/13/21:20
 * @Description:
 */
@Retention(RetentionPolicy.RUNTIME)
@Import(CustomizedImportSelector.class)
public @interface EnableAspectAutoProxy {
    String value() default "";
}

