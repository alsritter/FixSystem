package com.alsritter.mappers;

import com.alsritter.pojo.Orders;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

public interface OrderMapper {

    @Results(id = "order", value = {
            @Result(column = "fix_table_id", property = "fixTableId"),
            @Result(column = "student_id", property = "studentId"),
            @Result(column = "create_time", property = "createdTime"),
            @Result(column = "end_time", property = "endTime"),
            @Result(column = "fault_class", property = "faultClass"),
            @Result(column = "fault_detail", property = "faultDetail"),
            @Result(column = "work_id", property = "workId"),
            @Result(column = "admin_work_id", property = "adminWorkId"),
            @Result(column = "result_details", property = "resultDetails")
    })
    @Select("select * from ORDERS_TB ;")
    List<Orders> getAllOrders();

    // 可以使用 @ResultMap 注解来复用上面的映射
    @ResultMap("order")
    @Select("select * from ORDERS_TB where student_id = #{id} and (state in (0,1) or (state = 2 and massage is null))")
    List<Orders> getOrdersOfStudent(String id);

    @ResultMap("order")
    @Select("select * from ORDERS_TB where work_id = #{id} and state = 1;")
    List<Orders> getOrdersOfWorker(String id);

    @ResultMap("order")
    @Select("select * from ORDERS_TB where fix_table_id=#{fixTableId};")
    Orders getOrder(long fixTableId);

    @ResultMap("order")
    @Select("select * from ORDERS_TB where fix_table_id=#{fixTableId} and work_id=#{workerId};")
    Orders isExist(String workerId, long fixTableId);

    @ResultMap("order")
    @Select("select * from ORDERS_TB where fix_table_id=#{fixTableId} and student_id=#{studentId};")
    Orders isExistStudent(String studentId, long fixTableId);

    @Insert("insert into ORDERS_TB (student_id, contacts, create_time, address, phone, fault_class, fault_detail)\n" +
            "values (#{studentId},#{contacts}, now(), #{address}, #{phone}, #{faultClass}, #{faultDetails})")
    int createOrder(String studentId, String contacts, String phone, String faultClass, String address, String faultDetails);


    @Update("update ORDERS_TB set " +
            "result_details= #{resultDetails}, state=2 , end_time = now() " +
            "where fix_table_id= #{fixTableId}")
    int endOrder(long fixTableId, String resultDetails);

    @Update("update ORDERS_TB set massage=#{massage}, grade=#{grade} where fix_table_id=#{fixTableId}")
    int endOrderStudent(long fixTableId, String massage, double grade);

    @Delete("delete from ORDERS_TB where fix_table_id = #{fixTableId}")
    int deleteOrder(long fixTableId);

    @Update("update ORDERS_TB set work_id=#{workId} fix_table_id=#{fixTableId}")
    int setOrderWorker(String workId, long fixTableId);

    @ResultMap("order")
    @Select("select * from ORDERS_TB where state = 2 and student_id = #{studentId};")
    List<Orders> getStudentHistoryList(String studentId);

    @ResultMap("order")
    @Select("select * from ORDERS_TB where fix_table_id=#{fixTableId}")
    Orders getHistoryDetail(long fixTableId);

    @ResultMap("order")
    @Select("select * from ORDERS_TB where state = 2 and work_id = #{workId};")
    List<Orders> getWorkerHistoryList(String workId);

}
