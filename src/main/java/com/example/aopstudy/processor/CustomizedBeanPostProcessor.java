package com.example.aopstudy.processor;

import com.example.aopstudy.util.AspectUtil;
import lombok.SneakyThrows;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cglib.proxy.Enhancer;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Created with IntelliJ IDEA.
 * @Author: HuLiang
 * @Date: 2024/07/14/10:41
 * @Description:
 */
public class CustomizedBeanPostProcessor implements BeanPostProcessor {
    @SneakyThrows
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> clazz = bean.getClass();
        String className = clazz.getName();
        int ix = className.indexOf("$$");
        className = className.substring(0,ix > 0 ? ix : className.length());
        Object object = bean;
        System.out.println("初始化对象----CustomizedBeanPostProcessor----"+className);

        if(AspectUtil.clazzProxyBeanHolder.containsKey(className)){
            System.out.println("创建代理对象----CustomizedBeanPostProcessor----"+className);

            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(clazz);
            enhancer.setCallback(new CustomizedProxyInterceptor(AspectUtil.clazzProxyBeanHolder.get(className)));
            object = enhancer.create();

            Field[] fields = clazz.getDeclaredFields();
            for(Field field:fields){
                if(Modifier.isFinal(field.getModifiers()))
                    continue;
                field.setAccessible(true);
                field.set(object,field.get(bean));
            }
        }
        return object;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
