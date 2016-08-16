package com.michael.aspect;


//import org.apache.log4j.spi.LoggerFactory;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by GWC on 2016/7/4.
 */
@Aspect
@Component
public class LogAspect {
    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    //*为通配符，可以筛选哪些函数执行的时候执行该方法
    @Before("execution(* com.michael.controller.IndexController.*(..))")
    public void beforeMethod(JoinPoint joinPoint) {
        StringBuilder sb = new StringBuilder();
        for (java.lang.Object arg : joinPoint.getArgs()) {
            sb.append("arg:" + arg.toString() + "|");
        }
        logger.info("before method:" + sb.toString());
    }

    @After("execution(* com.michael.controller.IndexController.*(..))")
    public void afterMethod(JoinPoint joinPoint) {
        logger.info("after method:");
    }
}
