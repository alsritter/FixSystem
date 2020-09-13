package com.alsritter;

import com.alsritter.mappers.AdminMapper;
import com.alsritter.mappers.StudentMapper;
import com.alsritter.mappers.WorkerMapper;
import com.alsritter.utils.ConstantKit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

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

    @Resource
    public WorkerMapper workerMapper;

    @Resource
    public AdminMapper adminMapper;

    @Override
    public void run(String... args) throws Exception {
        log.info("LoadRedisRunner 开始执行");
        log.info("开始加载数据到 redis");
        List<String> studentIdList = studentMapper.getStudentIdList();
        List<String> workerIdList = workerMapper.getWorkerIdList();
        List<String> adminWorkerIdList = adminMapper.getWorkerIdList();


        // 保存到 set 集合上去，避免元素冲突
        studentIdList.forEach(o -> {
            stringTemplate.opsForSet().add(ConstantKit.USER_ID_LIST, o);
        });

        workerIdList.forEach(o -> {
            stringTemplate.opsForSet().add(ConstantKit.USER_ID_LIST, o);
        });

        adminWorkerIdList.forEach(o -> {
            stringTemplate.opsForSet().add(ConstantKit.USER_ID_LIST, o);
        });
    }
}
