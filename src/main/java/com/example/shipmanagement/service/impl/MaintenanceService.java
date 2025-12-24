package com.example.shipmanagement.service.impl;

import com.example.shipmanagement.mapper.MaintenanceMapper;
import com.example.shipmanagement.mapper.ShipMapper;
import com.example.shipmanagement.pojo.MaintenanceRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 直接写成类，不写接口了，简单粗暴
 */
@Service
public class MaintenanceService {

    @Autowired
    private MaintenanceMapper maintenanceMapper;

    @Autowired
    private ShipMapper shipMapper;

    public List<MaintenanceRecord> list() {
        return maintenanceMapper.list();
    }

    // 开启事务，保证两步操作同时成功
    @Transactional
    public void add(MaintenanceRecord record) {
        // 1. 设置默认状态为进行中
        record.setStatus("Pending");
        // 2. 保存记录
        maintenanceMapper.add(record);
        // 3. 自动将船舶状态改为 'Maintenance'
        shipMapper.updateState(record.getShipId(), "Maintenance");
    }

    @Transactional
    public void complete(Integer id) {
        // 1. 获取记录信息以拿到 shipId
        MaintenanceRecord record = maintenanceMapper.findById(id);
        if(record == null) return;

        // 2. 更新维修记录为 Completed
        maintenanceMapper.complete(id);

        // 3. 自动将船舶状态改回 'Available'
        shipMapper.updateState(record.getShipId(), "Available");
    }
}