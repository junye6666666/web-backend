package com.example.shipmanagement.service.impl;

import com.example.shipmanagement.mapper.ShipMapper;
import com.example.shipmanagement.pojo.PageBean;
import com.example.shipmanagement.pojo.Ship;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShipServiceImpl {

    // ✅✅✅ 你之前的代码里绝对少了下面这两行！！必须加上！！✅✅✅
    @Autowired
    private ShipMapper shipMapper;

    // 分页查询列表
    public PageBean<Ship> list(Integer pageNum, Integer pageSize, Integer categoryId, String state, String name) {
        // 1. 开启分页
        PageHelper.startPage(pageNum, pageSize);

        // 2. 执行查询
        // 这里的参数顺序 (name, categoryId, state) 必须和 Mapper 接口完全一致
        List<Ship> list = shipMapper.list(name, categoryId, state);

        // 3. 强转 Page 得到总条数
        Page<Ship> p = (Page<Ship>) list;

        // 4. 封装结果返回
        return new PageBean<>(p.getTotal(), p.getResult());
    }

    // 新增船舶
    public void add(Ship ship) {
        ship.setCreateTime(LocalDateTime.now());
        ship.setUpdateTime(LocalDateTime.now());
        if (ship.getState() == null) {
            ship.setState("Available");
        }
        shipMapper.add(ship);
    }

    // 修改船舶
    public void update(Ship ship) {
        ship.setUpdateTime(LocalDateTime.now());
        shipMapper.update(ship);
    }

    // 删除船舶
    public void delete(Integer id) {
        shipMapper.delete(id);
    }

    // 根据ID查询详情
    public Ship findById(Integer id) {
        return shipMapper.findById(id);
    }
}