package com.example.shipmanagement.pojo;

import java.time.LocalDateTime;

public class CharterRecord {
    private Integer id;
    private Integer shipId;
    private Integer userId;
    private LocalDateTime charterTime; // 对应数据库 charter_time
    private LocalDateTime returnTime;  // 对应数据库 return_time
    private String status;             // 对应数据库 status (Active/Returned)

    // 辅助字段（展示用）
    private String shipName;
    private String userName;

    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getShipId() { return shipId; }
    public void setShipId(Integer shipId) { this.shipId = shipId; }
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    public LocalDateTime getCharterTime() { return charterTime; }
    public void setCharterTime(LocalDateTime charterTime) { this.charterTime = charterTime; }
    public LocalDateTime getReturnTime() { return returnTime; }
    public void setReturnTime(LocalDateTime returnTime) { this.returnTime = returnTime; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getShipName() { return shipName; }
    public void setShipName(String shipName) { this.shipName = shipName; }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
}