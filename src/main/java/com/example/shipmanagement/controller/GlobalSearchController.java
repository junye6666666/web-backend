package com.example.shipmanagement.controller;

import com.example.shipmanagement.pojo.PageBean;
import com.example.shipmanagement.pojo.Result;
import com.example.shipmanagement.pojo.Ship;
import com.example.shipmanagement.service.impl.CharterServiceImpl;
import com.example.shipmanagement.service.impl.MaintenanceService;
import com.example.shipmanagement.service.impl.ShipServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/search")
public class GlobalSearchController {

    @Autowired
    private ShipServiceImpl shipService;

    @Autowired
    private CharterServiceImpl charterService;

    @Autowired
    private MaintenanceService maintenanceService;

    @GetMapping("/global")
    public Result<Map<String, Object>> search(@RequestParam String keyword) {
        Map<String, Object> map = new HashMap<>();

        // 1. 搜索船舶 (复用现有的分页查询接口，查前100条匹配项)
        PageBean<Ship> shipResult = shipService.list(1, 100, null, null, keyword);
        map.put("ships", shipResult.getItems());

        // 2. 搜索租赁记录 (调用新写的搜索方法)
        map.put("charters", charterService.search(keyword));

        // 3. 搜索维修记录 (调用新写的搜索方法)
        map.put("maintenances", maintenanceService.search(keyword));

        return Result.success(map);
    }
}