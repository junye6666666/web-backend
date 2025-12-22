package com.example.shipmanagement.mapper;

import com.example.shipmanagement.pojo.ShipCategory;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface ShipCategoryMapper {
    @Insert("INSERT INTO ship_category(category_name, category_alias, create_user, create_time, update_time) " +
            "VALUES(#{categoryName}, #{categoryAlias}, #{createUser}, now(), now())")
    void add(ShipCategory category);

    @Select("SELECT * FROM ship_category WHERE create_user = #{userId}")
    List<ShipCategory> list(Integer userId);

    @Select("SELECT * FROM ship_category WHERE id = #{id}")
    ShipCategory findById(Integer id);

    @Update("UPDATE ship_category SET category_name=#{categoryName}, category_alias=#{categoryAlias}, update_time=now() WHERE id=#{id}")
    void update(ShipCategory category);

    @Delete("DELETE FROM ship_category WHERE id=#{id}")
    void delete(Integer id);
}