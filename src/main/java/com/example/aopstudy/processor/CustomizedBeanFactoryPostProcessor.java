package com.example.aopstudy.processor;

import com.example.aopstudy.holder.ProxyBeanHolder;
import com.example.aopstudy.util.AspectUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.core.type.AnnotationMetadata;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * @Author: HuLiang
 * @Date: 2024/07/13/22:06
 * @Description:
 */
public class CustomizedBeanFactoryPostProcessor implements BeanFactoryPostProcessor
{
    /**
     * < 切点的方法名（相当于切点1、切点2，用来区分不同的切点），需要被代理的目标对象（在这里的代码只实现了对某个包处理） >，切点是切面中的一部分，而不是目标对象的一部分
     */
    private Map<String,String> pointCutMap = new HashMap<>();
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        String[] beanDefinitionNames = beanFactory.getBeanDefinitionNames();

        for(String name:beanDefinitionNames){

            System.out.println("BeanDefinition----CustomizedBeanFactoryPostProcessor----"+name);

            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(name);
            // 寻找切面
            if(beanDefinition instanceof AnnotatedBeanDefinition){
                System.out.println("ClassName----CustomizedBeanFactoryPostProcessor----"+beanDefinition.getBeanClassName());

                AnnotationMetadata metadata = ((AnnotatedBeanDefinition) beanDefinition).getMetadata();
                Set<String> annotations = metadata.getAnnotationTypes();
                for(String anno:annotations){
                    System.out.println("AnnotationTypes----CustomizedBeanFactoryPostProcessor----"+anno);

                    if(anno.equals(AspectUtil.ASPECT))
                        doScan((GenericBeanDefinition) beanDefinition);
                }
            }
        }
    }

    private void doScan(GenericBeanDefinition beanDefinition){
        try{

            String className = beanDefinition.getBeanClassName();
            System.out.println("扫描类----CustomizedBeanFactoryPostProcessor----"+className);
            Class<?> clazz = Class.forName(className);
            // 如果是获取当前类的私有方法则需要用 getDeclareMethods()
//            Method[] methods = clazz.getDeclaredMethods();
            Method[] methods = clazz.getMethods();
            // 保存是切点的方法
            for(Method method:methods){
                System.out.println("扫描到方法----CustomizedBeanFactoryPostProcessor----"+method.getName());
                Annotation[] annotations = method.getAnnotations();
                for(Annotation annotation:annotations){
                    System.out.println("扫描到注解----CustomizedBeanFactoryPostProcessor----"+annotation.annotationType().getName());
                    String annoName = annotation.annotationType().getName();
                    if(AspectUtil.POINTCUT.equals(annoName)){
                        System.out.println("扫描到切点注解----CustomizedBeanFactoryPostProcessor----"+annotation.annotationType().getName());
                        // 这里的 value 是需要被代理的目标对象
                        String value = getAnnotationValue(annotation);
                        pointCutMap.put(method.getName()+"()",value);
                    }
                }
            }
            // 处理需要织入的通知方法
            for(Method method:methods){
                Annotation[] annotations = method.getAnnotations();
                for(Annotation annotation:annotations){
                    String annoName = annotation.annotationType().getName();
                    if(AspectUtil.BEFORE.equals(annoName) ||AspectUtil.AFTER.equals(annoName) || AspectUtil.AROUND.equals(annoName)){
                        doScan(className,method,annotation);
                    }
                }
            }
        }catch (ClassNotFoundException | URISyntaxException e){
            System.out.println(e.getMessage());
        }
    }
    private void doScan(String className,Method method,Annotation annotation) throws URISyntaxException {
        // [className 类（切面）] 的 [method（需要织入的方法，也就是通知）] 是 [annotation 类型（声明在何时织入）] 的通知
        // 这里封装的是 className 切面的 method 通知代码应该在 annotation 声明的时刻织入（调用）
        ProxyBeanHolder beanHolder = new ProxyBeanHolder(className,method.getName(),annotation.annotationType().getName());
        // 这里的 value 是需要被织入通知的切点（方法）名，这一步是通过获取通知注解的 value 值获取这个通知要织入的切点
        String pointCutMethod = getAnnotationValue(annotation);
        // 根据切点的名称获取切点注解里指定的需要代理的目标对象所在包
        System.out.println("获取到切点----CustomizedBeanFactoryPostProcessor----"+pointCutMethod);

        String packagePath = pointCutMap.get(pointCutMethod);
        System.out.println("封装切面----CustomizedBeanFactoryPostProcessor----"+beanHolder.getClassName());
        System.out.println("路径----CustomizedBeanFactoryPostProcessor----"+packagePath);

        // 如果这个通知想要织入的切点存在，那么这个切点会在注解内指明它要增强的类所在的包
        // 然后接下来就去根据包路径 packagePath 和封装的 proxyBeanHolder ，将所有 proxyBeanHolder 添加到需要增强的目标对象类的 AspectUtil.clazzProxyBeanHolder 中
        if(!packagePath.isBlank()){
            traverseDir(packagePath,beanHolder);
        }
        /**
         * 至此，对于所有被声明的注解注解过的类，以及对应的通知、织入时间采集完成
         * 一步是对对应的类创建代理对象，然后调用对象的通知方法，实现:
         * {@link m2on.study.processor.CustomizedBeanPostProcessor
         */

    }

    private void traverseDir(String packagePath,ProxyBeanHolder proxyBeanHolder) throws URISyntaxException {
        String targetPackagePath = this.getClass().getResource("/").toURI()+packagePath.replace(".","/");
        System.out.println("扫描路径----CustomizedBeanFactoryPostProcessor----"+targetPackagePath);

        File file = new File(targetPackagePath.substring(targetPackagePath.indexOf("file:/")+6));
        File[] files = file.listFiles();
        System.out.println("文件list----CustomizedBeanFactoryPostProcessor----"+files);

        if(files == null)
            return ;
        List<ProxyBeanHolder> proxyBeanHolderList = null;
        for(File f:files){
            System.out.println("扫描文件----CustomizedBeanFactoryPostProcessor----"+f.getName());

            if(f.isFile()){
                String targetClass = packagePath + "." + f.getName().replace(".class","");
                proxyBeanHolderList = AspectUtil.clazzProxyBeanHolder.get(targetClass);
                if(proxyBeanHolderList == null){
                    proxyBeanHolderList = new Vector<>();
                }
                proxyBeanHolderList.add(proxyBeanHolder);
                System.out.println("保存切面----CustomizedBeanFactoryPostProcessor----"+proxyBeanHolder.getClassName());

                AspectUtil.clazzProxyBeanHolder.put(targetClass,proxyBeanHolderList);
            }else{
                traverseDir(packagePath + "." + f.getName(),proxyBeanHolder);
            }
        }
    }
    private String getAnnotationValue(Annotation annotation){
        Method[] annoMethods = annotation.annotationType().getDeclaredMethods();
        for(Method method:annoMethods){
            if(method.getName().equals("value")){
                try {
                    return (String) method.invoke(annotation);
                } catch (IllegalAccessException | InvocationTargetException e){
                    System.out.println(e.getMessage());
                }
            }
        }
        return "";
    }

}
