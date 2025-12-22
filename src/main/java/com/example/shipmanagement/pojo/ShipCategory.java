package com.example.shipmanagement.pojo;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ShipCategory {
    private Integer id;
    private String categoryName;
    private String categoryAlias;
    private Integer createUser;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}