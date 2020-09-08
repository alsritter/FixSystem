package com.alsritter;

import com.alsritter.mappers.StudentMapper;
import com.alsritter.utils.ConstantKit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * 启动前先把 mysql 的数据加载到 redis
 * 当有多个钩子函数时，通过 @Order 注解
 * 来指定其加载次序
 *
 * @author alsritter
 * @version 1.0
 **/
@Component
@Slf4j
public class LoadRedisRunner implements CommandLineRunner {

    @Resource
    StringRedisTemplate stringTemplate;

    @Resource
    public StudentMapper studentMapper;

    @Override
    public void run(String... args) throws Exception {
        log.info("LoadRedisRunner 开始执行");
        // 先检查当前 redis 是否有数据，有就跳过
        Set<String> members = stringTemplate.opsForSet().members(ConstantKit.STUDENT_ID_LIST);
        if (members != null) {
            if (!members.isEmpty()){
                members.forEach(o -> log.debug("已存在数据：{}", o));
                return;
            }
        }

        log.info("开始加载数据到 redis");
        List<String> studentIdList = studentMapper.getStudentIdList();
        // 保存到 set 集合上去，避免元素冲突
        studentIdList.forEach( o ->{
            stringTemplate.opsForSet().add(ConstantKit.STUDENT_ID_LIST, o);
        });
        stringTemplate.opsForSet().members(ConstantKit.STUDENT_ID_LIST).forEach(o-> log.debug("新增数据：{}", o));
    }
}
