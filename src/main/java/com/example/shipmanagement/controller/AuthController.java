package com.example.shipmanagement.controller;

import com.example.shipmanagement.pojo.Result;
import com.example.shipmanagement.pojo.User;
import com.example.shipmanagement.service.UserServiceImpl;
import com.example.shipmanagement.utils.JwtUtil;
import com.example.shipmanagement.utils.Md5Util;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@Validated
public class AuthController {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 注册
     * 不需要返回数据，所以泛型可以用 <Object> 或者干脆不写
     */
    @PostMapping("/register")
    public Result<Object> register(@Pattern(regexp = "^\\S{5,16}$") @RequestParam String username,
                                   @Pattern(regexp = "^\\S{5,16}$") @RequestParam String password) {
        User u = userService.findByUserName(username);
        if (u == null) {
            userService.register(username, password);
            return Result.success(); // 现在的 Result.java 会自动推断为 Result<Object>
        } else {
            return Result.error("用户名已被占用");
        }
    }

    /**
     * 登录
     * 成功时返回 Token (String)，所以必须是 Result<String>
     */
    @PostMapping("/login")
    public Result<String> login(@Pattern(regexp = "^\\S{5,16}$") @RequestParam String username,
                                @Pattern(regexp = "^\\S{5,16}$") @RequestParam String password) {

        User loginUser = userService.findByUserName(username);

        if (loginUser == null) {
            // ✅ 之前报错的地方：现在 Result.error() 会自动根据方法返回类型变成 Result<String>
            return Result.error("用户名错误");
        }

        String inputPwdEncrypted = Md5Util.getMD5String(password);

        if (loginUser.getPassword() != null && loginUser.getPassword().equals(inputPwdEncrypted)) {
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", loginUser.getId());
            claims.put("username", loginUser.getUsername());
            String token = jwtUtil.genToken(claims);

            return Result.success(token);
        } else {
            return Result.error("密码错误");
        }
    }
}