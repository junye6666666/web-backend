package com.example.shipmanagement.service;

import com.example.shipmanagement.mapper.ShipCategoryMapper;
import com.example.shipmanagement.pojo.ShipCategory;
import com.example.shipmanagement.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class ShipCategoryServiceImpl {

    @Autowired private ShipCategoryMapper shipCategoryMapper;

    // 新增
    public void add(ShipCategory category) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        category.setCreateUser(userId);
        shipCategoryMapper.add(category);
    }

    // 列表
    public List<ShipCategory> list() {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        return shipCategoryMapper.list(userId);
    }

    // 详情
    public ShipCategory findById(Integer id) {
        return shipCategoryMapper.findById(id);
    }

    // 更新
    public void update(ShipCategory category) {
        category.setUpdateTime(LocalDateTime.now());
        shipCategoryMapper.update(category);
    }

    // 删除
    public void delete(Integer id) {
        shipCategoryMapper.delete(id);
    }
}