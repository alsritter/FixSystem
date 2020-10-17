package com.alsritter.mappers;

import com.alsritter.pojo.Equipment;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

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
    @Select("select ename,\n" +
            "       address,\n" +
            "       state,\n" +
            "       id,\n" +
            "       eclass,\n" +
            "       egrade,\n" +
            "       eweight,\n" +
            "       update_date,\n" +
            "       round(eweight_grade * ((6 - egrade) / 5),0) as 'eweight_grade',\n" +
            "           eworker,\n" +
            "       using_unit,\n" +
            "       etype,\n" +
            "       url,\n" +
            "       address\n" +
            "from equipment_tb;")
    List<Equipment> getEquipmentList();

    @ResultMap("equipment")
    @Select("select * from equipment_tb where id=#{id};")
    Equipment getEquipment(int id);

    @Update("update equipment_tb set state=#{state} where id=#{id};")
    int setState(int id, int state);

    @Select("select eclass from equipment_tb group by eclass;")
    List<String> getEquipmentClass();

    @Update("update equipment_tb set update_date=now(),eweight=0 where id=#{id};")
    int updateDate(int id);

    List<Equipment> searchEquipment(String usingUnit, String ename, String etype, String eclass, Integer egrade, Integer state);

    @Select("select count(*) as 'count' from equipment_tb;")
    int getEquipmentNumber();

    @Select("select count(*) as count,state from equipment_tb group by state;")
    List<Map<String, Object>> getEquipmentStateRatio();

    @Select("select count(*) as count,egrade from equipment_tb group by equipment_tb.egrade;")
    List<Map<String, Object>> getEquipmentGradeRatio();

    @Select("select count(*) as count,eclass from equipment_tb group by equipment_tb.eclass;")
    List<Map<String, Object>> getEquipmentClassRatio();
}
