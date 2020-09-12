package com.alsritter.mappers;

import com.alsritter.pojo.Tool;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface ToolMapper {


    @Result(column = "tool_id", property = "toolId")
    @Result(column = "tool_name", property = "toolName")
    @Result(column = "tool_count", property = "toolCount")
    @Select("select * from Tool_TB")
    List<Tool> getToolList();

    @Update("update Tool_TB set tool_count = #{toolCount} where tool_id= #{toolId}")
    int updateTool(int toolId,int toolCount);

    @Insert("insert into TOOL_TB (tool_name, tool_count) VALUE (#{toolName},#{toolCount})")
    int createTool(String toolName, int toolCount);

    @Insert("delete from TOOL_TB where tool_id = #{toolId}")
    int deleteTool(int toolId);
}
