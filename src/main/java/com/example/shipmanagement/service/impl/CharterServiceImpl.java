package com.example.shipmanagement.service.impl;

import com.example.shipmanagement.mapper.CharterRecordMapper;
import com.example.shipmanagement.mapper.ShipMapper;
import com.example.shipmanagement.pojo.CharterRecord;
import com.example.shipmanagement.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CharterServiceImpl {

    @Autowired
    private CharterRecordMapper charterRecordMapper;

    @Autowired
    private ShipMapper shipMapper;

    // 1. 租船
    public void charterShip(Integer shipId) {
        // 1. 获取当前登录用户 ID
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");

        // 2. 生成租赁记录
        CharterRecord record = new CharterRecord();
        record.setShipId(shipId);
        record.setUserId(userId);
        charterRecordMapper.add(record);

        // 3. 更新船舶状态为 "Chartered" (已租出)
        // 确保你的 ShipMapper 里有 updateState 方法
        shipMapper.updateState(shipId, "Chartered");
    }

    // 2. 还船
    public void returnShip(Integer recordId) {
        // 1. 更新记录状态为 "Returned"
        charterRecordMapper.returnShip(recordId);

        // 2. 查出这艘船的 ID，把它的状态改回 "Available" (空闲)
        CharterRecord record = charterRecordMapper.findById(recordId);
        if (record != null) {
            shipMapper.updateState(record.getShipId(), "Available");
        }
    }

    // 3. ✅✅✅ 之前漏掉的方法：获取列表
    public List<CharterRecord> list(String status) {
        // 获取当前用户 ID，只查这个人的记录
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");

        // 调用 Mapper 查库
        return charterRecordMapper.list(userId, status);
    }
}