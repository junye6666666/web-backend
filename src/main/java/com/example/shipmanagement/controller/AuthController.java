package com.example.shipmanagement.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import com.example.shipmanagement.pojo.Result;
import com.example.shipmanagement.pojo.User;
import com.example.shipmanagement.service.UserServiceImpl;
import com.example.shipmanagement.utils.JwtUtil;
import com.example.shipmanagement.utils.Md5Util;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/auth")
@Validated
public class AuthController {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private JwtUtil jwtUtil;

    // 简单模拟 Redis：用来存验证码 (UUID -> Code)
    // 生产环境建议用 Redis 设置过期时间，这里为了演示方便直接用内存 Map
    private static final Map<String, String> CAPTCHA_STORE = new ConcurrentHashMap<>();

    /**
     * 注册
     */
    @PostMapping("/register")
    public Result<Object> register(@Pattern(regexp = "^\\S{5,16}$") @RequestParam String username,
                                   @Pattern(regexp = "^\\S{5,16}$") @RequestParam String password) {
        User u = userService.findByUserName(username);
        if (u == null) {
            userService.register(username, password);
            return Result.success();
        } else {
            return Result.error("用户名已被占用");
        }
    }

    /**
     * ✅✅✅ 新增：获取图形验证码接口
     * 不需要参数，返回 { uuid: "...", img: "base64..." }
     */
    @GetMapping("/captcha")
    public Result<Map<String, String>> getCaptcha() {
        // 1. 生成验证码图片 (宽100, 高40, 4位字符, 20条干扰线)
        LineCaptcha captcha = CaptchaUtil.createLineCaptcha(100, 40, 4, 20);

        // 2. 生成一个唯一 ID (UUID)
        String uuid = UUID.randomUUID().toString(true);
        String code = captcha.getCode(); // 验证码的真实值 (比如 "AB12")

        // 3. 存入 Store (后续登录时要对比)
        CAPTCHA_STORE.put(uuid, code);

        // 4. 返回 base64 图片和 uuid 给前端
        Map<String, String> map = new HashMap<>();
        map.put("uuid", uuid);
        map.put("img", captcha.getImageBase64Data()); // base64图片数据

        return Result.success(map);
    }

    /**
     * ✅✅✅ 修改：登录接口
     * 增加了 uuid 和 code 参数用于校验验证码
     */
    @PostMapping("/login")
    public Result<String> login(@Pattern(regexp = "^\\S{5,16}$") @RequestParam String username,
                                @Pattern(regexp = "^\\S{5,16}$") @RequestParam String password,
                                @RequestParam(required = false) String uuid, // 允许为空是防止前端还没改好报错，实际逻辑会拦
                                @RequestParam(required = false) String code) {

        // --- 1. 校验验证码 ---
        if (StrUtil.isBlank(uuid) || StrUtil.isBlank(code)) {
            return Result.error("请输入验证码");
        }

        // 从 Store 里取正确答案
        String correctCode = CAPTCHA_STORE.get(uuid);
        if (correctCode == null) {
            return Result.error("验证码已失效，请点击图片刷新");
        }

        // 忽略大小写比对 (比如用户输入 ab12 也能匹配 AB12)
        if (!correctCode.equalsIgnoreCase(code)) {
            return Result.error("验证码错误");
        }

        // 验证通过后，立即移除这个验证码 (防止重复使用)
        CAPTCHA_STORE.remove(uuid);

        // --- 2. 校验用户名密码 (原有逻辑) ---
        User loginUser = userService.findByUserName(username);

        if (loginUser == null) {
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