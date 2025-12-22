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
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@Validated // 开启参数校验功能
public class AuthController {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 注册接口
     * 参数提交方式：x-www-form-urlencoded
     */
    @PostMapping("/register")
    public Result register(@Pattern(regexp = "^\\S{5,16}$") String username,
                           @Pattern(regexp = "^\\S{5,16}$") String password) {

        // 1. 查询用户是否已存在
        User u = userService.findByUserName(username);
        if (u == null) {
            // 2. 没有占用，进行注册
            // 注意：这里传入的是明文密码，加密逻辑在 UserService.register 方法里完成
            userService.register(username, password);
            return Result.success();
        } else {
            // 3. 已占用，返回错误信息
            return Result.error("用户名已被占用");
        }
    }

    /**
     * 登录接口
     * 参数提交方式：x-www-form-urlencoded
     */
    @PostMapping("/login")
    public Result<String> login(@Pattern(regexp = "^\\S{5,16}$") String username,
                                @Pattern(regexp = "^\\S{5,16}$") String password) {

        // 1. 根据用户名查询用户
        User loginUser = userService.findByUserName(username);

        // 2. 判断用户是否存在
        if (loginUser == null) {
            return Result.error("用户名错误");
        }

        // 3. 校验密码（核心修改点）
        // 逻辑：将用户输入的明文密码加密，然后跟数据库里存的密文进行比对
        String inputPwdEncrypted = Md5Util.getMD5String(password);

        if (inputPwdEncrypted.equals(loginUser.getPassword())) {
            // 4. 密码正确，生成令牌
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", loginUser.getId());
            claims.put("username", loginUser.getUsername());
            String token = jwtUtil.genToken(claims);

            return Result.success(token);
        } else {
            // 5. 密码错误
            return Result.error("密码错误");
        }
    }
}