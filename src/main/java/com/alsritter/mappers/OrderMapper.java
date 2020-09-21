package com.alsritter.mappers;

import com.alsritter.pojo.Orders;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

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
            @Result(column = "result_details", property = "resultDetails"),
            @Result(column = "massage", property = "message")
    })
    @Select("select * from ORDERS_TB ;")
    List<Orders> getAllOrders();

    // 可以使用 @ResultMap 注解来复用上面的映射
    @ResultMap("order")
    @Select("select * from ORDERS_TB where student_id = #{id} and state in (0,1)")
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

    @Update("update ORDERS_TB set massage=#{message}, grade=#{grade} where fix_table_id=#{fixTableId}")
    int endOrderStudent(long fixTableId, String message, double grade);

    @Delete("delete from ORDERS_TB where fix_table_id = #{fixTableId}")
    int deleteOrder(long fixTableId);

    @Update("update ORDERS_TB set work_id=#{workId}, admin_work_id=#{adminId}, state = 1 where fix_table_id=#{fixTableId}")
    int setOrderWorker(String adminId , String workId, long fixTableId);

    @ResultMap("order")
    @Select("select * from ORDERS_TB where state = 2 and student_id = #{studentId};")
    List<Orders> getStudentHistoryList(String studentId);

    @ResultMap("order")
    @Select("select * from ORDERS_TB where state = 2 and work_id = #{workId};")
    List<Orders> getWorkerHistoryList(String workId);

    @ResultMap("order")
    @Select("select * from ORDERS_TB where to_days(end_time) = to_days(now()) and  work_id = #{workId}")
    List<Orders> getTodayOrdersList(String workId);

    @Select("select COUNT(*) as ordersNumber, state from ORDERS_TB group by state")
    List<Map<String, Object>> getOrderClassNumber();

    @Select("select COUNT(*) as number from ORDERS_TB")
    int getOrderNumber();

    // 参考自：https://blog.csdn.net/cangchen/article/details/44978531
    @Select("SELECT end_time as date , grade\n" +
            "FROM ORDERS_TB\n" +
            "where DATE_SUB(CURDATE(), INTERVAL 30 DAY) <=date(end_time) \n" +
            "  and work_id=#{workId}")
    List<Map<String,Object>> getToMonthOrdersInWorker(String workId);

    @Select("SELECT end_time as date , grade FROM ORDERS_TB where DATE_SUB(CURDATE(), INTERVAL 30 DAY) <=date(end_time)")
    List<Map<String,Object>> getToMonthOrders();

    @Select("select fault_class as typeName, COUNT(fault_class) as number\n" +
            "from ORDERS_TB\n" +
            "where work_id = #{workId} and state = 2\n" +
            "group by fault_class;")
    List<Map<String,Object>> getFaultClassCount(String workId);

    @ResultMap("order")
    @Select("select * from ORDERS_TB\n" +
            "where fault_detail like #{word} or\n" +
            "      fault_class like #{word} or\n" +
            "      massage like #{word} or\n" +
            "      result_details like #{word} or\n" +
            "      contacts like #{word};")
    List<Orders> searchOrder(String word);

}
