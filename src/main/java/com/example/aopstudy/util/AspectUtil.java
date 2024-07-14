package com.example.aopstudy.util;

import com.example.aopstudy.annotation.*;
import com.example.aopstudy.holder.ProxyBeanHolder;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
/**
 * Created with IntelliJ IDEA.
 * @Author: HuLiang
 * @Date: 2024/07/13/21:38
 * @Description:
 */
public class AspectUtil {
    public static final String ASPECT = Aspect.class.getName();
    public static final String POINTCUT = Pointcut.class.getName();
    public static final String BEFORE = Before.class.getName();
    public static final String AFTER = After.class.getName();

    public static final String AROUND = Around.class.getName();

    /**
     * < 需要增强的类，增强的切面、通知、织入时机>
     */
    public static volatile Map<String, List<ProxyBeanHolder>> clazzProxyBeanHolder = new ConcurrentHashMap<>();
}
