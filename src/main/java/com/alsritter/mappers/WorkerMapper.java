package com.alsritter.mappers;

import com.alsritter.pojo.Worker;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface WorkerMapper {


    //工人登录
    @Result(column = "WORK_ID",property = "workId")
    @Select("select * from WORKER_TB WHERE WORK_ID=#{workId} and PASSWORD=#{password}")
    Worker loginWorker(String workId,String password);
    //工人主页
    @Result(column = "WORK_ID",property = "workId")
    @Select("select * from WORK_TB WHERE WORK_ID=#{workId} and ORDERS_NUMBER=#{ordersNumber}")
    Worker getWorker(String workId);


    @Update("update  WORK_TB set NAME=#{name} PHONE=#{phone} WHERE WORK_ID=#{workId}")
    int updateWorker(String workId,String name,String phone);




}
