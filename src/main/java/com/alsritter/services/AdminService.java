package com.alsritter.services;

import com.alsritter.mappers.AdminMapper;
import com.alsritter.pojo.Admin;
import com.alsritter.pojo.User;
import com.alsritter.utils.BizException;
import com.alsritter.utils.CommonEnum;
import com.alsritter.utils.ConstantKit;
import com.alsritter.utils.Md5TokenGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

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

}
