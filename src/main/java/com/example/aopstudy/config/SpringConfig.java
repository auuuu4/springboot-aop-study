package com.example.aopstudy.config;


import com.example.aopstudy.selector.EnableAspectAutoProxy;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created with IntelliJ IDEA.
 * @Author: HuLiang
 * @Date: 2024/07/14/12:58
 * @Description:
 */
@Configuration
@EnableAspectAutoProxy
@ComponentScan("m2on.study")
@Import({LogbackConfig.class})
public class SpringConfig {
}
