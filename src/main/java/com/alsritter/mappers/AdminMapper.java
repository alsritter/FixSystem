package com.alsritter.mappers;

import com.alsritter.pojo.Admin;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Select;

public interface AdminMapper {

    @Result(column = "work_id", property = "workId")
    @Result(column = "join_date", property = "joinDate")
    @Select("select * from ADMIN_TB where work_id=#{workId} and password=#{password}")
    Admin getAdmin(String workId, String password);

    
}
