package com.example.shipmanagement.controller;

import com.example.shipmanagement.pojo.MaintenanceRecord;
import com.example.shipmanagement.pojo.Result;
import com.example.shipmanagement.service.impl.MaintenanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/maintenance")
public class MaintenanceController {

    @Autowired
    private MaintenanceService maintenanceService;

    @GetMapping
    public Result<List<MaintenanceRecord>> list() {
        return Result.success(maintenanceService.list());
    }

    @PostMapping
    public Result add(@RequestBody MaintenanceRecord record) {
        maintenanceService.add(record);
        return Result.success();
    }

    @PutMapping("/{id}/complete")
    public Result complete(@PathVariable Integer id) {
        maintenanceService.complete(id);
        return Result.success();
    }
}