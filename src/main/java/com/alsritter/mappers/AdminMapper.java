package com.alsritter.mappers;

import com.alsritter.pojo.Admin;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface AdminMapper {

    @Result(column = "work_id", property = "id")
    @Result(column = "join_date", property = "joinDate")
    @Select("select * from ADMIN_TB where work_id=#{workId} and password=#{password}")
    Admin getAdmin(String workId, String password);

    // 用来把管理员 id 更新到 redis
    @Select("select work_id from ADMIN_TB")
    List<String> getWorkerIdList();

    @Update("update  ADMIN_TB set phone=#{phone}, gender=#{gender} WHERE work_id=#{id}")
    int updateUser(String id,String phone, String gender);
}
