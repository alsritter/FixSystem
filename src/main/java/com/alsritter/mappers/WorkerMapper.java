package com.alsritter.mappers;

import com.alsritter.pojo.Worker;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface WorkerMapper {


    //工人登录
    @Result(column = "WORK_ID",property = "workId")
    @Result(column = "ORDERS_NUMBER",property = "ordersNumber")
    @Result(column = "JOIN_DATE", property = "joinDate")
    @Result(column = "AVG_GRADE", property = "avgGrade")
    @Select("select * from WORKER_TB WHERE WORK_ID=#{workId} and PASSWORD=#{password}")
    Worker loginWorker(String workId,String password);

    // //工人主页
    // @Result(column = "WORK_ID",property = "workId")
    // @Select("select * from WORK_TB WHERE WORK_ID=#{workId} and ORDERS_NUMBER=#{ordersNumber}")
    // Worker getWorker(String workId);

    @Update("update  WORK_TB set NAME=#{name}, PHONE=#{phone} WHERE WORK_ID=#{workId}")
    int updateWorker(String workId,String name,String phone);

    // 用来把管理员 id 更新到 redis
    @Select("select work_id from WORKER_TB")
    List<String> getWorkerIdList();
}
