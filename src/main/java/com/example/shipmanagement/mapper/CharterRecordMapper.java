package com.example.shipmanagement.mapper;

import com.example.shipmanagement.pojo.CharterRecord;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface CharterRecordMapper {

    @Insert("INSERT INTO charter_record(ship_id, user_id, charter_time, status) " +
            "VALUES(#{shipId}, #{userId}, now(), 'Active')")
    void add(CharterRecord record);

    @Update("UPDATE charter_record SET return_time=now(), status='Returned' WHERE id=#{id}")
    void returnShip(Integer id);

    @Select("SELECT * FROM charter_record WHERE id = #{id}")
    CharterRecord findById(Integer id);

    // ✅ 必须有这个 list 方法
    @Select("<script>" +
            "SELECT c.*, s.name as shipName, u.nickname as userName " +
            "FROM charter_record c " +
            "LEFT JOIN ships s ON c.ship_id = s.id " +
            "LEFT JOIN users u ON c.user_id = u.id " +
            "<where>" +
            "c.user_id = #{userId}" +
            "<if test='status != null'> AND c.status = #{status} </if>" +
            "</where>" +
            "ORDER BY c.charter_time DESC" +
            "</script>")
    List<CharterRecord> list(@Param("userId") Integer userId, @Param("status") String status);
    // ✅✅✅ 新增：统计进行中的订单 (Active)
    @Select("SELECT COUNT(*) FROM charter_record WHERE status = 'Active'")
    Integer countActive();
}