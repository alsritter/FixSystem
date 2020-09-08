package com.alsritter.mappers;

import com.alsritter.pojo.Student;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface StudentMapper {
    // 如果只有一个映射可以直接使用 @Result 而不是 @Results
    @Result(column = "student_id", property = "studentId")
    @Select("select * from STUDENT_TB WHERE student_id=#{studentId} and password=#{password}")
    Student loginStudent(String studentId, String password);

    @Result(column = "student_id", property = "studentId")
    @Select("select * from STUDENT_TB WHERE student_id=#{studentId}")
    Student getStudent(String studentId);

    @Result(column = "student_id", property = "studentId")
    @Insert("insert into STUDENT_TB (student_id, name, gender, password, phone) " +
            "VALUES (#{studentId},#{name},#{gender},#{password},#{phone})")
    int signUpStudent(String studentId, String name, String password, String phone, String gender);

    // 用来把学生 id 更新到 redis
    @Select("select student_id from STUDENT_TB")
    List<String> getStudentIdList();
}
