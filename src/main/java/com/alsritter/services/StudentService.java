package com.alsritter.services;

import com.alsritter.mappers.StudentMapper;
import com.alsritter.pojo.Student;
import com.alsritter.utils.ConstantKit;
import com.alsritter.utils.MyDBError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Resource
    StringRedisTemplate stringTemplate;

    public Student loginStudent(String studentId, String password) {
        return studentMapper.loginStudent(studentId, password);
    }

    public Student getStudent(String studentId) {
        return studentMapper.getStudent(studentId);
    }


    // 注册成功需要更新一下 redis 的数据
    // 别忘了要配置事务
    @Transactional
    public int signUpStudent(String studentId, String name, String password, String phone, String gender) {
        int i = 0;
        try {
            i = studentMapper.signUpStudent(studentId, name, password, phone, gender);
        } catch (RuntimeException e) {
            throw new MyDBError("可能是插入重复的数据(例如主键冲突)了", e);
        }
        // 插入数据后再更新 redis
        if (i != 0) {
            stringTemplate.opsForSet().add(ConstantKit.USER_ID_LIST, studentId);
        }
        return i;
    }

    @Transactional
    public int updateStudent(String studentId, String name, String phone) {
        int i = 0;
        try {
            i = studentMapper.updateStudent(studentId, name, phone);
        } catch (RuntimeException e) {
            throw new MyDBError("修改数据错误", e);
        }
        return i;
    }



}

