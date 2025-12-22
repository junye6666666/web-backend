package com.example.shipmanagement.controller;

import com.example.shipmanagement.pojo.Result;
import com.example.shipmanagement.pojo.User;
import com.example.shipmanagement.service.UserServiceImpl;
import org.hibernate.validator.constraints.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user")
@Validated
public class UserController {

    @Autowired private UserServiceImpl userService;

    // 1. 获取用户信息
    @GetMapping("/me")
    public Result<User> userInfo() {
        User user = userService.getUserInfo();
        return Result.success(user);
    }

    // 2. 更新基本信息 (昵称、邮箱)
    @PutMapping
    public Result update(@RequestBody @Validated User user) {
        userService.update(user);
        return Result.success();
    }

    // 3. 更新头像 (PATCH queryString)
    @PatchMapping("/avatar")
    public Result updateAvatar(@RequestParam @URL String avatarUrl) {
        userService.updateAvatar(avatarUrl);
        return Result.success();
    }

    // 4. 更新密码 (PATCH JSON)
    @PatchMapping("/password")
    public Result updatePwd(@RequestBody Map<String, String> params) {
        // 校验参数长度 (5-16位)
        String oldPwd = params.get("old_pwd");
        String newPwd = params.get("new_pwd");
        String rePwd = params.get("re_pwd");

        if (!StringUtils.hasLength(oldPwd) || !StringUtils.hasLength(newPwd) || !StringUtils.hasLength(rePwd)) {
            return Result.error("缺少必要的密码参数");
        }

        try {
            userService.updatePwd(oldPwd, newPwd, rePwd);
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}