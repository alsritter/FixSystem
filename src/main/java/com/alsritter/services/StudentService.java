package com.alsritter.services;

import com.alsritter.mappers.StudentMapper;
import com.alsritter.pojo.Student;
import com.alsritter.utils.ConstantKit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Set;

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

    // 判断是否存在 redis 中
    public boolean isExistRedis(String studentId) {
        Set<String> members = stringTemplate.opsForSet().members(ConstantKit.STUDENT_ID_LIST);
        for (String s : members) {
            if (s.equals(studentId)) {
                return true;
            }
        }
        return false;
    }

    // 注册成功需要更新一下 redis 的数据
    // 别忘了要配置事务
    @Transactional
    public int signUpStudent(String studentId, String name, String password, String phone, String gender) {
        int i = studentMapper.signUpStudent(studentId, name, password, phone, gender);
        // 插入数据后再更新 redis
        if (i != 0) {
            stringTemplate.opsForSet().add(ConstantKit.STUDENT_ID_LIST, studentId);
        }
        return i;
    }

    public int updateStudent(String studentId, String name, String phone){
        return studentMapper.updateStudent(studentId, name, phone);
    }
}

