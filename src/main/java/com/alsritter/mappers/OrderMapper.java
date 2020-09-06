package com.alsritter.mappers;

import com.alsritter.pojo.Orders;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface OrderMapper {

    @Result(column = "fix_table_id", property = "fixTableId")
    @Result(column = "student_id", property = "studentId")
    @Result(column = "create_time", property = "createdTime")
    @Result(column = "end_time", property = "endTime")
    @Result(column = "fault_class", property = "faultClass")
    @Result(column = "fault_detail", property = "faultDetail")
    @Result(column = "work_id", property = "workId")
    @Result(column = "admin_work_id", property = "adminWorkId")
    @Result(column = "result_details", property = "resultDetails")
    @Select("select * from ORDERS_TB ;")
    List<Orders> getAllOrders();

    @Result(column = "fix_table_id", property = "fixTableId")
    @Result(column = "student_id", property = "studentId")
    @Result(column = "create_time", property = "createdTime")
    @Result(column = "end_time", property = "endTime")
    @Result(column = "fault_class", property = "faultClass")
    @Result(column = "fault_detail", property = "faultDetail")
    @Result(column = "work_id", property = "workId")
    @Result(column = "admin_work_id", property = "adminWorkId")
    @Result(column = "result_details", property = "resultDetails")
    @Select("select * from ORDERS_TB where fix_table_id=#{fixTableId};")
    Orders getOrder(long fixTableId);
}
