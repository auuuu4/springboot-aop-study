package com.example.aopstudy.service;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created with IntelliJ IDEA.
 * @Author: HuLiang
 * @Date: 2024/07/14/12:49
 * @Description:
 */
@Service
public class TestService {


    public void doTest(){
        System.out.println("TestService do test.");
    }
}
