package com.example.shipmanagement.interceptors;

import com.example.shipmanagement.utils.JwtUtil;
import com.example.shipmanagement.utils.ThreadLocalUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired; // ✅ 导包
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import java.util.Map;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired // ✅ 注入非静态的 JwtUtil
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 1. 放行 OPTIONS (解决跨域误报)
        if ("OPTIONS".equals(request.getMethod())) {
            return true;
        }

        // 2. 获取并处理 Token
        String token = request.getHeader("Authorization");
        try {
//            if (token != null && token.startsWith("Bearer ")) {
//                token = token.substring(7);

                // 3. ✅ 调用对象方法 (此时 jwtUtil 不会是 null 了)
                Map<String, Object> claims = jwtUtil.parseToken(token);

                ThreadLocalUtil.set(claims);
                return true;
//            } else {
//                response.setStatus(401);
//                return false;
//            }
        } catch (Exception e) {
            // 如果 jwtUtil 注入失败，这里会捕获空指针异常，导致你看到的“登录过期”
            // 现在的写法修复了注入问题，就不会报错了
            response.setStatus(401);
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        ThreadLocalUtil.remove();
    }
}