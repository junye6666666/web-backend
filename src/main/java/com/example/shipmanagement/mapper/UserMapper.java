package com.example.shipmanagement.mapper;

import com.example.shipmanagement.pojo.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {
    // 根据用户名查询（登录注册用）
    @Select("SELECT * FROM users WHERE username = #{username}")
    User findByUserName(String username);

    // 添加用户（注册用）
    @Insert("INSERT INTO users(username, password, create_time, update_time) " +
            "VALUES(#{username}, #{password}, now(), now())")
    void add(String username, String password);

    // 【新增】根据ID查询用户（获取个人信息用）
    @Select("SELECT * FROM users WHERE id = #{id}")
    User findById(Integer id);

    // 【新增】更新基本信息（昵称、邮箱）
    @Update("UPDATE users SET nickname=#{nickname}, email=#{email}, update_time=now() WHERE id=#{id}")
    void update(User user);

    // 【新增】更新头像
    @Update("UPDATE users SET user_pic=#{avatarUrl}, update_time=now() WHERE id=#{id}")
    void updateAvatar(String avatarUrl, Integer id);

    // 【新增】更新密码
    @Update("UPDATE users SET password=#{newPwd}, update_time=now() WHERE id=#{id}")
    void updatePwd(String newPwd, Integer id);
}