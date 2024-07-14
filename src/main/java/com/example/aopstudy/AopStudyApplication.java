package com.example.aopstudy;

import com.example.aopstudy.selector.EnableAspectAutoProxy;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages ={"com.example.aopstudy"})
@EnableAspectAutoProxy
public class AopStudyApplication {

    public static void main(String[] args) {
        SpringApplication.run(AopStudyApplication.class, args);
    }

}
