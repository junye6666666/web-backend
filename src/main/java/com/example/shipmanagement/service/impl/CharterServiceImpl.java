package com.example.shipmanagement.service.impl; // ✅ 注意这里加了 .impl

import com.example.shipmanagement.mapper.CharterRecordMapper;
import com.example.shipmanagement.mapper.ShipMapper;
import com.example.shipmanagement.pojo.CharterRecord;
import com.example.shipmanagement.pojo.PageBean;
import com.example.shipmanagement.pojo.Ship;
import com.example.shipmanagement.utils.ThreadLocalUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class CharterServiceImpl {

    @Autowired
    private CharterRecordMapper charterRecordMapper;

    @Autowired
    private ShipMapper shipMapper;

    @Transactional
    public void charterShip(Integer shipId) {
        Ship ship = shipMapper.findById(shipId);
        if (ship == null) throw new RuntimeException("船舶不存在");
        if (!"Available".equals(ship.getState())) {
            throw new RuntimeException("该船舶已被租用或维修中");
        }

        shipMapper.updateState(shipId, "Chartered");

        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");

        CharterRecord record = new CharterRecord();
        record.setShipId(shipId);
        record.setUserId(userId);

        charterRecordMapper.add(record);
    }

    @Transactional
    public void returnShip(Integer recordId) {
        CharterRecord record = charterRecordMapper.findById(recordId);
        if (record == null) throw new RuntimeException("记录不存在");
        if ("Returned".equals(record.getStatus())) {
            throw new RuntimeException("该订单已归还");
        }

        charterRecordMapper.returnShip(recordId);
        shipMapper.updateState(record.getShipId(), "Available");
    }

    public PageBean<CharterRecord> list(Integer pageNum, Integer pageSize, String status) {
        PageHelper.startPage(pageNum, pageSize);
        List<CharterRecord> list = charterRecordMapper.list(null, status);
        Page<CharterRecord> p = (Page<CharterRecord>) list;
        return new PageBean<>(p.getTotal(), p.getResult());
    }
}