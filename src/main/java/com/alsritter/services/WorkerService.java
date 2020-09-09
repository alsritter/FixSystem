package com.alsritter.services;

import com.alsritter.mappers.WorkerMapper;
import com.alsritter.pojo.Orders;
import com.alsritter.pojo.Worker;
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
import java.util.List;

@Service
@Slf4j
public class WorkerService {
    @Resource
    public WorkerMapper workerMapper;
    private OrdersService ordersService;

    @Resource
    StringRedisTemplate stringTemplate;

    @Autowired
    public void setOrdersService(OrdersService ordersService) {
        this.ordersService = ordersService;
    }

    public Worker loginWorker(String workId, String password) {
        return workerMapper.loginWorker(workId, password);
    }

    @Transactional
    public int setState(String workId, int i) {
        int j = 0;
        try {
            j = workerMapper.setState(workId, i);
        } catch (RuntimeException e) {
            throw new MyDBError("修改数据错误", e);
        }
        return j;
    }

    public Worker getWorker(String workId) {
        Worker worker = workerMapper.getWorker(workId);
        if (worker == null) {
            throw new BizException(CommonEnum.NOT_FOUND);
        }
        return worker;
    }

    public List<Worker> getLeisureWorkerList() {
        List<Worker> leisureWorkerList = workerMapper.getLeisureWorkerList();
        if (leisureWorkerList == null) {
            throw new BizException(CommonEnum.INTERNAL_SERVER_ERROR);
        }

        return leisureWorkerList;
    }

    @Transactional
    public int selectLeisureWorker(String workId, long fixTableId) {
        // 先判断当前工人是否处于工作状态
        Worker worker = workerMapper.getWorker(workId);
        if (worker == null) {
            throw new BizException(CommonEnum.NOT_FOUND);
        }
        if (worker.getState() == 1) {
            throw new BizException(CommonEnum.FORBIDDEN.getResultCode(),"当前工人已经在工作了，请不要再压榨他了");
        }

        // 再判断当前选定的订单是否已经在处理了或者是历史订单
        Orders order = ordersService.getOrder(fixTableId);
        if (order.getState() != 0) {
            throw new BizException(CommonEnum.FORBIDDEN.getResultCode(),"当前订单已经被处理，或者这是历史订单");
        }

        int j = 0;
        try {
            setState(workId, 1);
            j = ordersService.setOrderWorker(workId, fixTableId);
        } catch (RuntimeException e) {
            throw new MyDBError("指定工作错误：", e);
        }
        return j;
    }

    // 注册成功需要更新一下 redis 的数据
    // 别忘了要配置事务
    @Transactional
    public int signUpStudent(String workId, String name, String password, String phone, String gender, String details) {
        int i = 0;
        try {
            i = workerMapper.signUpStudent(workId, name, password, phone, gender, details);
        } catch (RuntimeException e) {
            throw new MyDBError("可能是插入重复的数据(例如主键冲突)了", e);
        }
        // 插入数据后再更新 redis
        if (i != 0) {
            stringTemplate.opsForSet().add(ConstantKit.USER_ID_LIST, workId);
        }
        return i;
    }
}




