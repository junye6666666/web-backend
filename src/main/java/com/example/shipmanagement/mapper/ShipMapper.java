package com.example.shipmanagement.mapper;

import com.example.shipmanagement.pojo.Ship;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface ShipMapper {

    // 1. 列表查询 (使用 <script> 标签实现动态 SQL)
    // 注意：原来 XML 里的 SQL 直接搬过来，套上 <script> 标签即可
    @Select("<script>" +
            "SELECT s.*, c.category_name " +
            "FROM ships s " +
            "LEFT JOIN ship_category c ON s.category_id = c.id " +
            "<where>" +
            "  <if test='name != null and name != \"\"'>" +
            "    AND s.name LIKE concat('%', #{name}, '%')" +
            "  </if>" +
            "  <if test='categoryId != null'>" +
            "    AND s.category_id = #{categoryId}" +
            "  </if>" +
            "  <if test='state != null and state != \"\"'>" +
            "    AND s.state = #{state}" +
            "  </if>" +
            "</where>" +
            "ORDER BY s.update_time DESC" +
            "</script>")
    List<Ship> list(@Param("name") String name,
                    @Param("categoryId") Integer categoryId,
                    @Param("state") String state);

    // 2. 根据ID查询
    @Select("SELECT * FROM ships WHERE id = #{id}")
    Ship findById(Integer id);

    // 3. 新增
    @Insert("INSERT INTO ships(name, category_id, manufacturer, imo_num, state, image_url, create_time, update_time) " +
            "VALUES (#{name}, #{categoryId}, #{manufacturer}, #{imoNum}, #{state}, #{imageUrl}, #{createTime}, #{updateTime})")
    void add(Ship ship);

    // 4. 修改
    @Update("UPDATE ships SET " +
            "name = #{name}, " +
            "category_id = #{categoryId}, " +
            "manufacturer = #{manufacturer}, " +
            "imo_num = #{imoNum}, " +
            "state = #{state}, " +
            "image_url = #{imageUrl}, " +
            "update_time = #{updateTime} " +
            "WHERE id = #{id}")
    void update(Ship ship);

    // 5. 修改状态
    @Update("UPDATE ships SET state = #{state}, update_time = now() WHERE id = #{id}")
    void updateState(@Param("id") Integer id, @Param("state") String state);

    // 6. 删除
    @Delete("DELETE FROM ships WHERE id = #{id}")
    void delete(Integer id);
// ✅✅✅ 这里才是放 @Select 统计代码的正确位置！✅✅✅

    @Select("SELECT COUNT(*) FROM ships")
    Integer countAll();

    @Select("SELECT COUNT(*) FROM ships WHERE state = #{state}")
    Integer countByState(String state);
}