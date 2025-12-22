package com.example.shipmanagement.mapper;

import com.example.shipmanagement.pojo.Ship;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface ShipMapper {

    // 列表查询 (必须加 @Param)
    List<Ship> list(@Param("name") String name,
                    @Param("categoryId") Integer categoryId,
                    @Param("state") String state);

    // 根据ID查询
    Ship findById(Integer id);

    // 新增
    void add(Ship ship);

    // 修改
    void update(Ship ship);

    // 修改状态 (必须加 @Param)
    void updateState(@Param("id") Integer id, @Param("state") String state);

    // 删除 (注意方法名叫 delete)
    void delete(Integer id);
}