package com.alsritter.mappers;

import com.alsritter.pojo.Tool;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface ToolMapper {


    @Result(column = "tool_id", property = "toolId")
    @Result(column = "tool_name", property = "toolName")
    @Result(column = "tool_count", property = "toolCount")
    @Result(column = "create_time", property = "createTime")
    @Result(column = "update_time", property = "updateTime")
    @Result(column = "update_number", property = "updateNumber")
    @Select("select * from TOOL_TB")
    List<Tool> getToolList();

    @Update("update TOOL_TB\n" +
            "set tool_count    = #{toolCount},\n" +
            "    tool_name     = #{toolName},\n" +
            "    price         = #{price},\n" +
            "    update_time   = now(),\n" +
            "    update_number = (update_number + 1)\n" +
            "where tool_id = #{toolId};")
    int updateTool(int toolId, String toolName, int toolCount, float price);

    @Insert("insert into TOOL_TB " +
            "(tool_name, tool_count, price, create_time, update_time)\n" +
            "VALUE (#{toolName}, #{toolCount}, #{price}, now(), now());")
    int createTool(String toolName, int toolCount, float price);

    @Insert("delete from TOOL_TB where tool_id = #{toolId}")
    int deleteTool(int toolId);

    @Select("select count(*) as 'count' from TOOL_TB;")
    int getToolNumber();

    @Select("select sum(price) from TOOL_TB;")
    float getToolSumPrice();
}
