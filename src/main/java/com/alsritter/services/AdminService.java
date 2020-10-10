package com.alsritter.services;

import com.alsritter.mappers.AdminMapper;
import com.alsritter.pojo.Admin;
import com.alsritter.pojo.User;
import com.alsritter.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
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
        if (user == null) {
            throw new BizException(CommonEnum.NOT_FOUND);
        }
        return user;
    }

    public Admin getSelf(String workId){
        Admin user = adminMapper.getAdminSelf(workId);
        if (user == null) {
            throw new BizException(CommonEnum.NOT_FOUND);
        }
        return user;
    }

    @Transactional
    public int updateUser(Admin admin){
        int i = 0;
        try {
            i = adminMapper.updateUser(admin);
        } catch (RuntimeException e) {
            throw new MyDBError("更新管理员的数据出现问题", e);
        }
        return i;
    }

    public String getName(String workId) {
        return getSelf(workId).getName();
    }
}
