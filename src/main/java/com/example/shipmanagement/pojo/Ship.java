package com.example.shipmanagement.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ship {
    private Integer id;
    private String name;
    private Integer categoryId; // 分类ID
    private String manufacturer;
    private String imoNum;
    private String state;
    private String imageUrl;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    // ✅✅✅ 必须加上这个字段，才能接收连表查询出来的 "category_name"
    private String categoryName;
}