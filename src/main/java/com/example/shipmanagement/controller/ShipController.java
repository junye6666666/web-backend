package com.example.shipmanagement.controller;

import com.example.shipmanagement.pojo.PageBean;
import com.example.shipmanagement.pojo.Result;
import com.example.shipmanagement.pojo.Ship;
import com.example.shipmanagement.service.impl.ShipServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ships")
public class ShipController {

    @Autowired
    private ShipServiceImpl shipService;

    /**
     * 获取船舶列表 (带搜索和分页)
     * 前端传参示例: /ships?pageNum=1&pageSize=5&name=泰坦尼克&state=Available
     */
    @GetMapping
    public Result<PageBean<Ship>> list(
            Integer pageNum,
            Integer pageSize,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) String name
    ) {
        // ✅ 关键点：这里的参数顺序必须和 ShipServiceImpl.list 方法定义的顺序完全一致！
        // 顺序是: pageNum, pageSize, categoryId, state, name
        PageBean<Ship> pb = shipService.list(pageNum, pageSize, categoryId, state, name);
        return Result.success(pb);
    }

    /**
     * 新增船舶
     * 前端发送 POST 请求，JSON 数据放在 Body 里
     */
    @PostMapping
    public Result add(@RequestBody @Validated Ship ship) {
        shipService.add(ship);
        return Result.success();
    }

    /**
     * 修改船舶
     * 前端发送 PUT 请求
     */
    @PutMapping
    public Result update(@RequestBody @Validated Ship ship) {
        shipService.update(ship);
        return Result.success();
    }

    /**
     * 删除船舶
     * 前端发送 DELETE 请求: /ships?id=1
     */
    @DeleteMapping
    public Result delete(Integer id) {
        shipService.delete(id);
        return Result.success();
    }

    /**
     * 根据ID获取详情 (可选，用于编辑回显等)
     */
    @GetMapping("/detail")
    public Result<Ship> detail(Integer id) {
        Ship ship = shipService.findById(id);
        return Result.success(ship);
    }
}
