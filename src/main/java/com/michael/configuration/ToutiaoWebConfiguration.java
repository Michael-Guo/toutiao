package com.michael.configuration;

import com.michael.interceptor.LoginRequiredInterceptor;
import com.michael.interceptor.PassportInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by GWC on 2016/7/8.
 */
@Component
public class ToutiaoWebConfiguration extends WebMvcConfigurerAdapter{
    @Autowired
    PassportInterceptor passportInterceptor;

    @Autowired
    LoginRequiredInterceptor loginRequiredInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(passportInterceptor);//先拦截看看用户是谁，这个是全局的，所有页面都要拦截
        registry.addInterceptor(loginRequiredInterceptor).addPathPatterns("/setting*");//再看看用户是否符合页面访问要求，只处理与setting有关的页面
        super.addInterceptors(registry);
    }

}
