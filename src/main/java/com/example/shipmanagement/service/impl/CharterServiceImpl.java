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
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");

        CharterRecord record = new CharterRecord();
        record.setShipId(shipId);
        record.setUserId(userId);
        charterRecordMapper.add(record);

        shipMapper.updateState(shipId, "Chartered");
    }

    // 2. 还船
    public void returnShip(Integer recordId) {
        charterRecordMapper.returnShip(recordId);
        CharterRecord record = charterRecordMapper.findById(recordId);
        if (record != null) {
            shipMapper.updateState(record.getShipId(), "Available");
        }
    }

    // 3. 获取列表 (个人)
    public List<CharterRecord> list(String status) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        return charterRecordMapper.list(userId, status);
    }

    // ✅✅✅ 新增：全局搜索 (不限用户)
    public List<CharterRecord> search(String keyword) {
        return charterRecordMapper.search(keyword);
    }
}