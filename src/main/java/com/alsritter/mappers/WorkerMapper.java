package com.alsritter.mappers;

import com.alsritter.pojo.Worker;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface WorkerMapper {


    //工人登录
    @Results(id = "worker", value = {
            @Result(column = "WORK_ID", property = "id"),
            @Result(column = "ORDERS_NUMBER", property = "ordersNumber"),
            @Result(column = "JOIN_DATE", property = "joinDate"),
            @Result(column = "AVG_GRADE", property = "avgGrade")
    })
    @Select("select * from WORKER_TB WHERE WORK_ID=#{workId} and PASSWORD=#{password}")
    Worker loginWorker(String workId, String password);

    @Update("update  WORKER_TB set NAME=#{name}, PHONE=#{phone} WHERE WORK_ID=#{workId}")
    int updateWorker(String workId, String name, String phone);

    // 用来把工人 id 更新到 redis
    @Select("select work_id from WORKER_TB")
    List<String> getWorkerIdList();

    @Update("update  WORKER_TB set state=#{i} WHERE WORK_ID=#{workId}")
    int setState(String workId, int i);

    @ResultMap("worker")
    @Select("select * from WORKER_TB  WHERE WORK_ID=#{workId}")
    Worker getWorker(String workId);

    @ResultMap("worker")
    @Select("select * from WORKER_TB  WHERE state = 0")
    List<Worker> getLeisureWorkerList();

    @Insert("insert into WORKER_TB (WORK_ID, NAME, GENDER, PASSWORD, PHONE, JOIN_DATE, DETAILS, AVG_GRADE, ORDERS_NUMBER) " +
            "VALUES (#{workId},#{name},#{gender},#{password},#{phone}, NOW(),#{details}, 10, 0)")
    int signUpStudent(String workId, String name, String password, String phone, String gender, String details);

    @ResultMap("worker")
    @Select("select ORDERS_NUMBER from WORKER_TB")
    Worker getWorkerHome(String workId);

    @ResultMap("worker")
    @Select("select * from WORKER_TB")
    List<Worker> getWorkerList();

    @Update("update  WORKER_TB set AVG_GRADE=#{newAvgGrade} WHERE WORK_ID=#{workId}")
    int setGrade(String workId, double newAvgGrade);

    @Update("update  WORKER_TB set ORDERS_NUMBER=(ORDERS_NUMBER+1) WHERE WORK_ID=#{workId}")
    int addOrderNumber(String workId);
}
