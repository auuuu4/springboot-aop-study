package com.example.aopstudy.controller;

import com.example.aopstudy.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created with IntelliJ IDEA.
 * @Author: HuLiang
 * @Date: 2024/07/14/16:14
 * @Description:
 */
@RestController
public class TestController {
    @Autowired
    TestService service;
    @GetMapping("/test")
    public void test(){
        service.doTest();
    }
}
