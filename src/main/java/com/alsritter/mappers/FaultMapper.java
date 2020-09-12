package com.alsritter.mappers;

import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface FaultMapper {
    @Select("select * from FAULT_TB")
    List<String> getFaultClassList();

    @Select("select * from FAULT_TB WHERE fault_class")
    String isExist(String faultName);
}
