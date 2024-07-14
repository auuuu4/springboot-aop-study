package com.example.aopstudy.selector;


import com.example.aopstudy.processor.CustomizedBeanFactoryPostProcessor;
import com.example.aopstudy.processor.CustomizedBeanPostProcessor;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * Created with IntelliJ IDEA.
 * @Author: HuLiang
 * @Date: 2024/07/13/21:22
 * @Description:
 */
public class CustomizedImportSelector implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return new String[]{CustomizedBeanPostProcessor.class.getName(), CustomizedBeanFactoryPostProcessor.class.getName()};
    }
}
