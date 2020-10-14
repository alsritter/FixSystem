package com.alsritter.mappers;

import com.alsritter.pojo.Equipment;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface EquipmentMapper {
    int addEquipment(
            String ename,
            String eclass,
            Integer egrade,
            Integer eweightGrade,
            String eworkerId,
            String usingUnit,
            String etype,
            String url,
            String address,
            Equipment equipment);

    int updateEquipment(
            int id,
            String ename,
            String eclass,
            Integer egrade,
            Integer eweightGrade,
            String eworkerId,
            String usingUnit,
            String etype,
            String address);

    @Results(id = "equipment", value = {
            @Result(column = "eweight_grade", property = "eweightGrade"),
            @Result(column = "using_unit", property = "usingUnit"),
            @Result(column = "update_date", property = "updateDate")
    })
    @Select("select * from equipment_tb;")
    List<Equipment> getEquipmentList();

    @ResultMap("equipment")
    @Select("select * from equipment_tb where id=#{id};")
    Equipment getEquipment(int id);

    @Update("update equipment_tb set state=#{state} where id=#{id};")
    int setState(int id, int state);

    @Select("select eclass from equipment_tb group by eclass;")
    List<String> getEquipmentClass();
}
