package com.example.shipmanagement.controller;

import com.example.shipmanagement.mapper.CharterRecordMapper;
import com.example.shipmanagement.mapper.MaintenanceMapper;
import com.example.shipmanagement.mapper.ShipMapper;
import com.example.shipmanagement.pojo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private ShipMapper shipMapper;

    @Autowired
    private CharterRecordMapper charterMapper;

    @Autowired
    private MaintenanceMapper maintenanceMapper;

    @GetMapping("/stats")
    public Result<Map<String, Object>> getStats() {
        Map<String, Object> map = new HashMap<>();

        // 1. 基础数字卡片
        Integer totalShips = shipMapper.countAll();
        Integer availableShips = shipMapper.countByState("Available");
        Integer charteredShips = shipMapper.countByState("Chartered");
        Integer maintenanceShips = shipMapper.countByState("Maintenance");

        // 2. 业务进行中数字
        Integer activeCharters = charterMapper.countActive();
        Integer pendingMaintenance = maintenanceMapper.countPending();

        map.put("totalShips", totalShips);
        map.put("availableShips", availableShips);
        map.put("charteredShips", charteredShips);
        map.put("maintenanceShips", maintenanceShips);
        map.put("activeCharters", activeCharters);
        map.put("pendingMaintenance", pendingMaintenance);

        return Result.success(map);
    }
}