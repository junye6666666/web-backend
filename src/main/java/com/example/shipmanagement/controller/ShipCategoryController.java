package com.example.shipmanagement.controller;

import com.example.shipmanagement.pojo.Result;
import com.example.shipmanagement.pojo.ShipCategory;
import com.example.shipmanagement.service.ShipCategoryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ship-category")
public class ShipCategoryController {

    @Autowired private ShipCategoryServiceImpl shipCategoryService;

    // 1. 新增分类
    @PostMapping
    public Result add(@RequestBody @Validated ShipCategory category) {
        shipCategoryService.add(category);
        return Result.success();
    }

    // 2. 分类列表
    @GetMapping
    public Result<List<ShipCategory>> list() {
        List<ShipCategory> list = shipCategoryService.list();
        return Result.success(list);
    }

    // 3. 分类详情
    @GetMapping("/detail")
    public Result<ShipCategory> detail(Integer id) {
        ShipCategory category = shipCategoryService.findById(id);
        return Result.success(category);
    }

    // 4. 更新分类
    @PutMapping
    public Result update(@RequestBody @Validated ShipCategory category) {
        shipCategoryService.update(category);
        return Result.success();
    }

    // 5. 删除分类
    @DeleteMapping
    public Result delete(Integer id) {
        shipCategoryService.delete(id);
        return Result.success();
    }
}