package com.alsritter.services;

import com.alsritter.mappers.WorkerMapper;
import com.alsritter.pojo.Worker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class WorkerService {
    @Resource
    public WorkerMapper WorkerMapper;

    public Worker loginWorker(String workId, String password) {
        return WorkerMapper.loginWorker(workId, password);
    }

}




