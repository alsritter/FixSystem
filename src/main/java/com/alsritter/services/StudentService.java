package com.alsritter.services;

import com.alsritter.mappers.StudentMapper;
import com.alsritter.pojo.Student;
import com.alsritter.utils.BizException;
import com.alsritter.utils.CommonEnum;
import com.alsritter.utils.ConstantKit;
import com.alsritter.utils.MyDBError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.regex.Pattern;

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

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public Student loginStudent(String studentId, String password) {
        Student student = studentMapper.loginStudent(studentId, password);
        if (student == null) {
            throw new BizException(CommonEnum.NOT_FOUND);
        }
        return student;
    }

    public Student getStudent(String studentId) {
        Student student = studentMapper.getStudent(studentId);
        if (student == null) {
            throw new BizException(CommonEnum.NOT_FOUND);
        }
        return student;
    }


    // 注册成功需要更新一下 redis 的数据
    // 别忘了要配置事务
    @Transactional
    public int signUpStudent(String studentId, String name, String password, String phone, String gender) {

        //验证学号是否正确(只能是数字和 "-")
        if (!Pattern.compile("^-?\\d+(\\.\\d+)?$").matcher(studentId).matches()) {
            throw new BizException(CommonEnum.BAD_REQUEST.getResultCode(), "学号格式错误");
        }

        //检验是否已经有这个学生了
        if (userService.isExistRedis(studentId)) {
            throw new BizException(CommonEnum.FORBIDDEN.getResultCode(), "学生已经存在");
        }


        //验证手机号码正确性
        String regex = "^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0,3,5-8])|(18[0-9])|166|198|199|(147))\\d{8}$";
        if (!Pattern.compile(regex).matcher(phone).matches()) {
            throw new BizException(CommonEnum.BAD_REQUEST.getResultCode(), "手机号错误");
        }

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

