# springboot-aop-study

> 该项目作为对 Spring AOP 特性的学习。包含详细的代码注释，对理解 spring 对 bean 的初始化有一定帮助，主要加深对于 aop 的理解。

实现了  [@Before](src\main\java\com\example\aopstudy\annotation\Before.java) 、 [@After](src\main\java\com\example\aopstudy\annotation\After.java) 、 [@Pointcut](src\main\java\com\example\aopstudy\annotation\Pointcut.java) 、 [@Aspect](src\main\java\com\example\aopstudy\annotation\Aspect.java)  等注解功能，目前切点只能标记某个包，然后可以使用对应的注解织入代码。

#### 实现接口 

**BeanFactoryPostProcessor([CustomizedBeanFactoryPostProcessor.java](src\main\java\com\example\aopstudy\processor\CustomizedBeanFactoryPostProcessor.java) ):**

1. BeanFactoryPostProcessor 会先扫描所有 BeanDefinition ，并对其中的切面(类)在第 2 步进行再次扫描。
2. 对于切面类扫描出其通知(方法)以及切点并封装记录。
3. 将切点(目前实现的是某个包中的类)及其需要织入的通知(也就是 2 中封装的内容)放入 HashMap<Class,List>，等待拦截器调用

**BeanPostProcessor([CustomizedBeanPostProcessor.java](src\main\java\com\example\aopstudy\processor\CustomizedBeanPostProcessor.java)) :**

4. 直接使用 CGLLIB 进行代理，创建被代理类(也就是目标类)的代理类，然后 setCallback([CustomizedProxyInterceptor.java](src\main\java\com\example\aopstudy\processor\CustomizedProxyInterceptor.java))，在此过程需要传入 3 中对应封装的 List 。

**MethodInterceptor([CustomizedProxyInterceptor.java](src\main\java\com\example\aopstudy\processor\CustomizedProxyInterceptor.java)):**

5. 使用反射执行 @Before 和 @Around 类注解对应方法
6. 使用反射执行原方法
7. 使用反射执行 @After 和 @Around 类注解对应方法

### 运行实验
启动 [AopStudyApplication.java](src%2Fmain%2Fjava%2Fcom%2Fexample%2Faopstudy%2FAopStudyApplication.java) 后访问  localhost:8080/test ，控制台会输出对应的通知方法调用提示