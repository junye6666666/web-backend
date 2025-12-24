package com.example.shipmanagement.mapper;

import com.example.shipmanagement.pojo.MaintenanceRecord;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MaintenanceMapper {

    // ✅✅✅ 修复核心：表名改成 ships
    @Select("SELECT m.*, s.name as shipName FROM maintenance_record m LEFT JOIN ships s ON m.ship_id = s.id ORDER BY m.create_time DESC")
    List<MaintenanceRecord> list();

    // 新增记录
    @Insert("INSERT INTO maintenance_record(ship_id, description, start_date, end_date, cost, status, create_time, update_time) " +
            "VALUES(#{shipId}, #{description}, #{startDate}, #{endDate}, #{cost}, #{status}, now(), now())")
    void add(MaintenanceRecord record);

    // 完成维修
    @Update("UPDATE maintenance_record SET status = 'Completed', end_date = now(), update_time = now() WHERE id = #{id}")
    void complete(Integer id);

    // 根据ID查记录
    @Select("SELECT * FROM maintenance_record WHERE id = #{id}")
    MaintenanceRecord findById(Integer id);
    // ✅✅✅ 新增：统计进行中的维修 (Pending)
    @Select("SELECT COUNT(*) FROM maintenance_record WHERE status = 'Pending'")
    Integer countPending();
}