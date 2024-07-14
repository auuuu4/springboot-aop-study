package com.example.aopstudy.holder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created with IntelliJ IDEA.
 * @Author: HuLiang
 * @Date: 2024/07/13/21:40
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProxyBeanHolder {
    /**
     * 切面类
     */
    private String className;
    /**
     * 通知 (Advise) 方法名
     */
    private String methodName;
    /**
     * 通知的注解名，代表通知的织入时机
     */
    private String annotationName;
}
