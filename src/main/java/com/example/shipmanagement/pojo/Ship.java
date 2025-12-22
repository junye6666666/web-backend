package com.example.shipmanagement.pojo;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Ship {
    private Integer id;
    private String name;
    private String manufacturer; // 对应 author
    private String imoNum;       // 对应 isbn
    private Integer categoryId;
    private String imageUrl;     // 对应 cover_img
    private String state;        // Available, Chartered
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}