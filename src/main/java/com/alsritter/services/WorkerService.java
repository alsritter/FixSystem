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
import java.util.regex.Pattern;

@Service
@Slf4j
public class WorkerService {
    @Resource
    public WorkerMapper workerMapper;
    private OrdersService ordersService;

    @Resource
    StringRedisTemplate stringTemplate;


    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setOrdersService(OrdersService ordersService) {
        this.ordersService = ordersService;
    }

    public Worker loginWorker(String workId, String password) {
        Worker worker = workerMapper.loginWorker(workId, password);
        if (worker == null) {
            throw new BizException(CommonEnum.NOT_FOUND);
        }
        return worker;
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

    @Transactional
    public int addOrderNumber(String workId) {
        int j = 0;
        try {
            j = workerMapper.addOrderNumber(workId);
        } catch (RuntimeException e) {
            throw new MyDBError("添加订单错误", e);
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
        return workerMapper.getLeisureWorkerList();
    }

    @Transactional
    public int selectLeisureWorker(String adminId, String workId, long fixTableId) {
        // 先判断当前工人是否处于工作状态
        Worker worker = workerMapper.getWorker(workId);

        if (worker == null) {
            throw new BizException(CommonEnum.NOT_FOUND.getResultCode(), "找不到该工人！");
        }

        // if (worker.getState() == 1) {
        //     throw new BizException(CommonEnum.FORBIDDEN.getResultCode(), "当前工人已经在工作了，请不要再压榨他了");
        // }

        // 再判断当前选定的订单是否已经在处理了或者是历史订单
        Orders order = ordersService.getOrder(fixTableId);
        if (order.getState() != 0) {
            throw new BizException(CommonEnum.FORBIDDEN.getResultCode(), "当前订单已经被处理，或者这是历史订单");
        }

        int j = 0;
        try {
            // 这个只是用来修改工人状态的，并非修改订单的状态
            setState(workId, 1);
            j = ordersService.setOrderWorker(adminId, workId, fixTableId);
        } catch (RuntimeException e) {
            throw new MyDBError("指定工作错误：", e);
        }
        return j;
    }

    // 注册成功需要更新一下 redis 的数据
    // 别忘了要配置事务
    @Transactional
    public int signUpStudent(Worker worker) {
        //验证工号格式是否正确(只能是数字和 "-")
        if (!Pattern.compile("^-?\\d+(\\.\\d+)?$").matcher(worker.getId()).matches()) {
            throw new BizException(CommonEnum.BAD_REQUEST.getResultCode(), "工号格式错误");
        }

        //检验是否已经有这个工人
        if (userService.isExistRedis(worker.getId())) {
            throw new BizException(CommonEnum.FORBIDDEN.getResultCode(), "工人已经存在");
        }

        //验证手机号码正确性
        String regex = "^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0,3,5-8])|(18[0-9])|166|198|199|(147))\\d{8}$";
        if (!Pattern.compile(regex).matcher(worker.getPhone()).matches()) {
            throw new BizException(CommonEnum.BAD_REQUEST.getResultCode(), "手机号错误");
        }

        int i = 0;
        try {
            i = workerMapper.signUpStudent(worker);
        } catch (RuntimeException e) {
            throw new MyDBError("可能是插入重复的数据(例如主键冲突)了", e);
        }
        // 插入数据后再更新 redis
        if (i != 0) {
            stringTemplate.opsForSet().add(ConstantKit.USER_ID_LIST, worker.getId());
        }
        return i;
    }

    public Worker getWorkerHome(String workId) {
        Worker worker = workerMapper.getWorkerHome(workId);
        if (worker == null) {
            throw new BizException(CommonEnum.NOT_FOUND);
        }
        return worker;
    }

    public List<Worker> getWorkerList() {
        return workerMapper.getWorkerList();
    }

    @Transactional
    public int setGrade(String workId, double newAvgGrade) {
        int j = 0;
        try {
            j = workerMapper.setGrade(workId, newAvgGrade);
        } catch (RuntimeException e) {
            throw new MyDBError("修改新分数错误", e);
        }
        return j;
    }

    public List<Worker> searchWorker(String id, String name, String phone) {
        if (id == null && name == null && phone == null) {
            throw new BizException(CommonEnum.BAD_REQUEST.getResultCode(), "关键字不能全部为空！");
        }
        return workerMapper.searchWorker(id, name, phone);
    }

    public int getCount() {
        return workerMapper.getCount();
    }
}




