package com.example.aopstudy.common;

import com.example.aopstudy.annotation.*;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 * @Author: HuLiang
 * @Date: 2024/07/14/12:49
 * @Description:
 */
@Aspect
@Component
public class AspectTest {
    @Pointcut("com.example.aopstudy.service")
    public void testMethod(){

    }
    @Before("testMethod()")
    public void beforeTestService(){
        System.out.println("----------------Before testMethod------------");
    }
    @Around("testMethod()")
    public void aroundTestService(){
        System.out.println("----------------Around testMethod------------");
    }
    @After("testMethod()")
    public void afterTestService(){
        System.out.println("----------------After testMethod------------");
    }
}
