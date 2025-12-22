package com.example.shipmanagement.controller;

import com.example.shipmanagement.pojo.CharterRecord;
import com.example.shipmanagement.pojo.PageBean;
import com.example.shipmanagement.pojo.Result;
// ⚠️ 关键点：根据你的截图，Service实现类在 impl 包下，所以要这样导入：
import com.example.shipmanagement.service.impl.CharterServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/charter")
public class CharterController {

    @Autowired
    private CharterServiceImpl charterService;

    // 1. 租船 (POST)
    @PostMapping
    public Result charter(@RequestBody Map<String, Integer> params) {
        Integer shipId = params.get("shipId");
        if (shipId == null) return Result.error("缺少shipId参数");

        try {
            // 这里调用我们在 Service 里改名后的 charterShip 方法
            charterService.charterShip(shipId);
            return Result.success();
        } catch (Exception e) {
            // 捕获“库存不足”或“已租出”等异常并返回给前端
            return Result.error(e.getMessage());
        }
    }

    // 2. 还船 (PATCH)
    @PatchMapping("/return")
    public Result returnShip(@RequestParam Integer id) {
        try {
            charterService.returnShip(id);
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    // 3. 查询租赁记录列表 (GET)
    @GetMapping
    public Result<PageBean<CharterRecord>> list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String status
    ) {
        PageBean<CharterRecord> pb = charterService.list(pageNum, pageSize, status);
        return Result.success(pb);
    }
}