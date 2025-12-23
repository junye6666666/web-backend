package com.example.shipmanagement.controller;

import com.example.shipmanagement.pojo.CharterRecord;
import com.example.shipmanagement.pojo.Result;
import com.example.shipmanagement.service.impl.CharterServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/charter")
public class CharterController {

    @Autowired
    private CharterServiceImpl charterService;

    // 1. 租船
    @PostMapping
    public Result charterShip(Integer shipId) {
        charterService.charterShip(shipId);
        return Result.success();
    }

    // 2. 还船
    @PostMapping("/return")
    public Result returnShip(Integer recordId) {
        charterService.returnShip(recordId);
        return Result.success();
    }

    // ✅✅✅ 补上这个：获取租赁记录列表
    @GetMapping
    public Result<List<CharterRecord>> list(@RequestParam(required = false) String status) {
        List<CharterRecord> list = charterService.list(status);
        return Result.success(list);
    }
}