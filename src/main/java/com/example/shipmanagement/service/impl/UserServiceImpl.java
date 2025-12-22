package com.example.shipmanagement.service;

import com.example.shipmanagement.mapper.UserMapper;
import com.example.shipmanagement.pojo.User;
import com.example.shipmanagement.utils.Md5Util;
import com.example.shipmanagement.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class UserServiceImpl {

    @Autowired private UserMapper userMapper;

    // ... (之前的 findByUserName 和 register 方法保留) ...

    public User findByUserName(String username) {
        return userMapper.findByUserName(username);
    }

    public void register(String username, String password) {
        String md5String = Md5Util.getMD5String(password);
        userMapper.add(username, md5String);
    }

    // --- ↓↓↓ 新增的方法 ↓↓↓ ---

    /**
     * 获取当前登录用户信息
     */
    public User getUserInfo() {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer id = (Integer) map.get("id");
        return userMapper.findById(id);
    }

    /**
     * 更新用户基本信息
     */
    public void update(User user) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer id = (Integer) map.get("id");

        user.setId(id); // 强制设置ID为当前登录用户，防止篡改他人数据
        user.setUpdateTime(LocalDateTime.now());

        userMapper.update(user);
    }

    /**
     * 更新头像
     */
    public void updateAvatar(String avatarUrl) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer id = (Integer) map.get("id");
        userMapper.updateAvatar(avatarUrl, id);
    }

    /**
     * 更新密码
     * @param oldPwd 原密码
     * @param newPwd 新密码
     * @param rePwd  确认新密码
     */
    public void updatePwd(String oldPwd, String newPwd, String rePwd) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer id = (Integer) map.get("id");

        // 1. 校验参数完整性
        if (!StringUtils.hasLength(oldPwd) || !StringUtils.hasLength(newPwd) || !StringUtils.hasLength(rePwd)) {
            throw new RuntimeException("参数缺失");
        }

        // 2. 校验原密码是否正确
        User loginUser = userMapper.findById(id);
        if (!loginUser.getPassword().equals(Md5Util.getMD5String(oldPwd))) {
            throw new RuntimeException("原密码填写不正确");
        }

        // 3. 校验两次新密码是否一致
        if (!newPwd.equals(rePwd)) {
            throw new RuntimeException("两次填写的新密码不一致");
        }

        // 4. 更新密码 (记得加密!)
        userMapper.updatePwd(Md5Util.getMD5String(newPwd), id);
    }
}