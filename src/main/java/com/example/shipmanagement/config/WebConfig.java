package com.example.shipmanagement.config;

import com.example.shipmanagement.interceptors.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                // ✅✅✅ 关键点：这里必须排除 /auth/captcha
                .excludePathPatterns(
                        "/auth/login",
                        "/auth/register",
                        "/auth/captcha",  // <--- 确保这一行存在！
                        "/files/**"
                );
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 如果你有文件上传回显配置，保留它；没有就不用管
        registry.addResourceHandler("/files/**")
                .addResourceLocations("file:D:/upload/");
    }
}