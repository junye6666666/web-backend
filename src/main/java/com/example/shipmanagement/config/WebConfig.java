package com.example.shipmanagement.config;

import com.example.shipmanagement.interceptors.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired // ✅ 关键点：让 Spring 把生成好的拦截器注入进来
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // ❌ 绝对不要写: registry.addInterceptor(new LoginInterceptor())
        // ❌ 如果你写了 new，拦截器里的 @Autowired jwtUtil 就会失效！

        // ✅ 正确写法：使用注入进来的变量
        registry.addInterceptor(loginInterceptor)
                .excludePathPatterns("/auth/login", "/auth/register");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5173")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}