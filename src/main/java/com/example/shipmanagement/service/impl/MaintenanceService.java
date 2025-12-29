package com.example.shipmanagement.service.impl;

import com.example.shipmanagement.mapper.MaintenanceMapper;
import com.example.shipmanagement.mapper.ShipMapper;
import com.example.shipmanagement.pojo.MaintenanceRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MaintenanceService {

    @Autowired
    private MaintenanceMapper maintenanceMapper;

    @Autowired
    private ShipMapper shipMapper;

    public List<MaintenanceRecord> list() {
        return maintenanceMapper.list();
    }

    @Transactional
    public void add(MaintenanceRecord record) {
        record.setStatus("Pending");
        maintenanceMapper.add(record);
        shipMapper.updateState(record.getShipId(), "Maintenance");
    }

    @Transactional
    public void complete(Integer id) {
        MaintenanceRecord record = maintenanceMapper.findById(id);
        if(record == null) return;

        maintenanceMapper.complete(id);
        shipMapper.updateState(record.getShipId(), "Available");
    }

    // ✅✅✅ 新增：搜索方法
    public List<MaintenanceRecord> search(String keyword) {
        return maintenanceMapper.search(keyword);
    }
}