package com.alsritter.controller;

import com.alsritter.pojo.Student;
import com.alsritter.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试项目启动
 *
 * @author alsritter
 * @version 1.0
 **/

@RestController
@RequestMapping("/hello")
public class HelloController {

    private StudentService studentService;

    @Autowired
    public void setStudentService(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/temp")
    public Student sayHello() {
        return studentService.getStudent();
    }
}
