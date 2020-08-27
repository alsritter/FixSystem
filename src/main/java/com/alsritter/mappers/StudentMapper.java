package com.alsritter.mappers;

import com.alsritter.pojo.Student;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Select;

public interface StudentMapper {
    // 如果只有一个映射可以直接使用 @Result 而不是 @Results
    @Result(column = "student_id", property = "studentId")
    @Select("select * from student_tb where name='alsritter'")
    Student getStudent();
}
