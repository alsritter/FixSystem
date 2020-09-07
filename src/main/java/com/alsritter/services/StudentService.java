package com.alsritter.services;

import com.alsritter.mappers.StudentMapper;
import com.alsritter.pojo.Student;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author alsritter
 * @version 1.0
 **/
@Service
@Slf4j
public class StudentService {
    @Resource
    public StudentMapper studentMapper;

    public Student loginStudent(String studentId, String password) {
        return studentMapper.loginStudent(studentId, password);
    }

    public Student getStudent(String studentId) {
        return studentMapper.getStudent(studentId);
    }

    public int signUpStudent(String studentId, String name, String password, String phone, String gender) {
        return studentMapper.signUpStudent(studentId,name,password,phone,gender);
    }
}
