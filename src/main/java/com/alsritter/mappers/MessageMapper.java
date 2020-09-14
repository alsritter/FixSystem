package com.alsritter.mappers;

import com.alsritter.pojo.Message;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface MessageMapper {
    @Result(column = "massage_id", property = "massageId")
    @Result(column = "work_id", property = "adminId")
    @Result(column = "create_date", property = "createDate")
    @Result(column = "massage", property = "messageStr")
    @Select("select * from Massage_TB")
    List<Message> getMessageList();


    @Insert("insert into Massage_TB(create_date, work_id, massage) VALUE(now(),#{adminId},#{massage})")
    int pushMassage(String adminId,String massage);
}
