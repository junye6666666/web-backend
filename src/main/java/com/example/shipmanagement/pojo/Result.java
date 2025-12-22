package com.example.shipmanagement.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {
    private Integer code; // 0-成功, 1-失败
    private String message;
    private T data;

    // ✅ 修复：加上 <E>，让这个方法能返回 Result<String>, Result<User> 等任何类型
    public static <E> Result<E> success(E data) {
        return new Result<>(0, "操作成功", data);
    }

    // ✅ 修复：加上 <E>，允许根据上下文推断类型
    // 例如：方法需要返回 Result<String> 时，这里会自动变成 Result<String>
    public static <E> Result<E> success() {
        return new Result<>(0, "操作成功", null);
    }

    // ✅ 修复：加上 <E>，解决 "Provided: Result<Object>, Required: Result<String>" 的报错
    public static <E> Result<E> error(String message) {
        return new Result<>(1, message, null);
    }
}