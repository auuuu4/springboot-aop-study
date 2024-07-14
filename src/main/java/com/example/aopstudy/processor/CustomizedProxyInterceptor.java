package com.example.aopstudy.processor;

import com.example.aopstudy.holder.ProxyBeanHolder;
import com.example.aopstudy.util.AspectUtil;
import com.example.aopstudy.util.JacksonUtil;
import lombok.SneakyThrows;

import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.http.codec.json.Jackson2JsonEncoder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * @Author: HuLiang
 * @Date: 2024/07/14/10:53
 * @Description:
 */
public class CustomizedProxyInterceptor implements MethodInterceptor {
    List<ProxyBeanHolder> holderList = null;

    public CustomizedProxyInterceptor(List<ProxyBeanHolder> holderList) {
        this.holderList = holderList;
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        System.out.println("使用代理对象----CustomizedProxyInterceptor----"+obj.getClass().getName());
        System.out.println("通知列表有----CustomizedProxyInterceptor----"+ JacksonUtil.toJsonString(holderList));
        for(ProxyBeanHolder holder:holderList){
            String anno = holder.getAnnotationName();

            if(anno.equals(AspectUtil.AROUND) || anno.equals(AspectUtil.BEFORE)){
                doProxy(holder);
            }
        }
        System.out.println(obj.getClass().getName()+" 调用了 "+method.getName()+" 方法，参数是 "+ Arrays.toString(Arrays.stream(args).toArray()));
        System.out.println("proxy.getSignature().getName(): "+proxy.getSignature().getName());
        System.out.println("proxy.getSignature().getDescriptor(): "+proxy.getSignature().getDescriptor());
        proxy.invokeSuper(obj,args);
        for(ProxyBeanHolder holder:holderList){
            String anno = holder.getAnnotationName();
            if(anno.equals(AspectUtil.AFTER) || anno.equals(AspectUtil.AROUND)){
                doProxy(holder);
            }
        }
        System.out.println("结束调用"+method.getName());
        return obj;

    }
    @SneakyThrows
    private void doProxy(ProxyBeanHolder holder){
        System.out.println("调用通知方法----CustomizedProxyInterceptor----"+holder.getMethodName());

        Class<?> clazz = Class.forName(holder.getClassName());
        Method mt = clazz.getMethod(holder.getMethodName());
        mt.invoke(clazz.getDeclaredConstructor().newInstance());

    }
}
