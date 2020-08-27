package com.alsritter.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class StudentServiceTest {

    @Autowired
    public StudentService studentService;

    @Test
    public void test() {
        studentService.getStudent();
    }
}