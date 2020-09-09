package com.alsritter.services;

import com.alsritter.mappers.AdminMapper;
import com.alsritter.pojo.Admin;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 *
 * 这个 Service 专门用来处理登陆请求
 *
 * @author alsritter
 * @version 1.0
 **/
@Slf4j
@Service
public class AdminService {

    @Resource
    public AdminMapper adminMapper;

    public Admin adminLogin(String workId, String password) {
        // 先查询数据
        Admin user = adminMapper.getAdmin(workId, password);
        log.info("user:" + user);
        return user;
    }
}
