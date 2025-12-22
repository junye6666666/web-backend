package com.example.shipmanagement.mapper;

import com.example.shipmanagement.pojo.CharterRecord;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface CharterRecordMapper {

    // 1. 新增租赁记录
    @Insert("INSERT INTO charter_record(ship_id, user_id, charter_time, status) " +
            "VALUES(#{shipId}, #{userId}, now(), 'Active')")
    void add(CharterRecord record);

    // 2. 归还操作
    @Update("UPDATE charter_record SET return_time=now(), status='Returned' WHERE id=#{id}")
    void returnShip(Integer id);

    // 3. 查询单条
    @Select("SELECT * FROM charter_record WHERE id = #{id}")
    CharterRecord findById(Integer id);

    // 4. 分页列表查询
    @Select("<script>" +
            "SELECT c.*, s.name as shipName, u.nickname as userName " +
            "FROM charter_record c " +
            "LEFT JOIN ships s ON c.ship_id = s.id " +
            "LEFT JOIN users u ON c.user_id = u.id " +
            "<where>" +
            "<if test='userId != null'> AND c.user_id = #{userId} </if>" +
            "<if test='status != null'> AND c.status = #{status} </if>" +
            "</where>" +
            "ORDER BY c.charter_time DESC" +
            "</script>")
    List<CharterRecord> list(Integer userId, String status);
}