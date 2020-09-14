package com.alsritter.services;

import com.alsritter.mappers.OrderMapper;
import com.alsritter.pojo.Orders;
import com.alsritter.pojo.Worker;
import com.alsritter.utils.BizException;
import com.alsritter.utils.CommonEnum;
import com.alsritter.utils.MyDBError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 专门用来处理订单相关的数据
 *
 * @author alsritter
 * @version 1.0
 **/
@Service
public class OrdersService {

    @Resource
    public OrderMapper orderMapper;
    private WorkerService workerService;
    private FaultService faultService;


    @Autowired
    public void setFaultService(FaultService faultService) {
        this.faultService = faultService;
    }

    @Autowired
    public void setWorkerService(WorkerService workerService) {
        this.workerService = workerService;
    }

    public List<Orders> getAllOrders() {
        return orderMapper.getAllOrders();
    }

    public Orders getOrder(long fixTableId) {
        Orders order = orderMapper.getOrder(fixTableId);
        if (order == null) {
            throw new BizException(CommonEnum.NOT_FOUND);
        }
        return order;
    }

    @Transactional
    public int deleteOrder(long fixTableId) {
        // 为空时抛出异常
        Orders order = getOrder(fixTableId);

        // 不能是状态为 2 的订单
        if (order.getState() == 2) {
            throw new BizException(CommonEnum.FORBIDDEN.getResultCode(), "该订单已经是历史订单了，不能删");
        }

        // 没有指定工人的订单直接删
        if (order.getWorkId() != null) {
            // 否则先更改一下工人的状态为 0（空闲）再删除
            if (workerService.setState(order.getWorkId(), 0) == 0) {
                throw new BizException(CommonEnum.INTERNAL_SERVER_ERROR.getResultCode(), "修改工人状态错误");
            }
        }
        return orderMapper.deleteOrder(fixTableId);
    }


    /**
     * <b>
     * 用来查询当前学生的所有正在处理的订单
     * </b>
     * <br>
     *
     * @param studentId : 学生的 id
     * @return : com.alsritter.pojo.Orders
     */
    public List<Orders> getOrdersOfStudent(String studentId) {
        return orderMapper.getOrdersOfStudent(studentId);
    }

    /**
     * <b>
     * 用来查询当前工人的所有正在处理的订单
     * </b>
     * <br>
     *
     * @param workerId : 工人的 id
     * @return : com.alsritter.pojo.Orders
     */
    public List<Orders> getOrdersOfWorker(String workerId) {
        return orderMapper.getOrdersOfWorker(workerId);
    }

    /**
     * <b>
     * 检查当前订单是否由该工人进行处理的，
     * 如果能用该工人的 id 和该订单号找到
     * 数据表示该订单是由该工人处理
     * <p>
     * 同时检查当前订单是否处于正在进行的状态（isOngoing）
     * </b>
     * <br>
     *
     * @param workerId   : 工人的id
     * @param fixTableId : 单号
     */
    public void isExist(String workerId, long fixTableId) {
        // 先检查是否是当前工人处理的单
        Orders exist = orderMapper.isExist(workerId, fixTableId);
        if (exist == null) {
            throw new BizException(CommonEnum.FORBIDDEN.getResultCode(), "不是当前工人的订单");
        }

        // 再判断当前订单是否是正在处理的 1
        if (exist.getState() != 1) {
            throw new BizException(CommonEnum.FORBIDDEN.getResultCode(), "当前订单未接单或已处理");
        }
    }

    /**
     * <b>
     * 和上面的那个作用差不多
     * <p>
     * 检查当前订单是否当前学生发起的，
     * 如果能用该学生的 id 和该订单号找到
     * 数据表示该订单是由该工人处理
     * <p>
     * 同时检查当前订单是否处于已经是 2 状态，且 Massage 为 null
     * 注意：是 Null 而不是 “空”
     * </b>
     * <br>
     *
     * @param studentId  : 学生的id
     * @param fixTableId : 单号
     */
    public Orders isExistStudent(String studentId, long fixTableId) {
        // 先检查是否是当前学生的订单
        Orders exist = orderMapper.isExistStudent(studentId, fixTableId);
        if (exist == null) {
            throw new BizException(CommonEnum.FORBIDDEN.getResultCode(), "不是当前学生的订单");
        }

        // 再判断当前订单是否是已处理的 2
        if (exist.getState() != 2) {
            throw new BizException(CommonEnum.FORBIDDEN.getResultCode(), "当前订单未已处理");
        }

        // 最后判断当前订单没有评价过
        if (exist.getMassage() != null) {
            throw new BizException(CommonEnum.FORBIDDEN.getResultCode(), "当前订单已评价");
        }

        return exist;
    }

    /**
     * <b>
     * 这个是给工人处理结果说明的
     * 注意！！！ 评价完成后要把工人的状态改回 0
     * </b>
     * <br>
     *
     * @param fixTableId    :
     * @param resultDetails :
     * @return : int
     */
    @Transactional
    public int endOrder(long fixTableId, String resultDetails) {
        // 先找到订单的工人
        Orders order = getOrder(fixTableId);
        if (order.getWorkId() == null) {
            throw new BizException(CommonEnum.FORBIDDEN.getResultCode(), "该订单没有指定工人！！！");
        }

        int i = 0;
        try {
            i = orderMapper.endOrder(fixTableId, resultDetails);
            workerService.setState(order.getWorkId(), 0);
        } catch (RuntimeException e) {
            throw new MyDBError("修改数据错误：", e);
        }
        return i;
    }

    /**
     * <b>
     * 这个是学生评价的结束服务，注意 这个给分需要先算出平均分
     * </b>
     * <br>
     *
     * @param fixTableId :
     * @param massage    :
     * @param grade      :
     * @return : int
     */
    @Transactional
    public int endOrder(String id, long fixTableId, String massage, Integer grade) {
        // 先检查是否是当前工人处理的订单 和 判断当前订单是否是正在处理的 2（如果不行会自动抛出错误）
        Orders orders = isExistStudent(id, fixTableId);
        // 再取得当前订单的工人
        Worker worker = workerService.getWorker(orders.getWorkId());

        double avgGrade = worker.getAvgGrade();
        double ordersNumber = worker.getOrdersNumber();

        if (ordersNumber == 0) {
            ordersNumber = 1;
        }

        double newAvgGrade = ((avgGrade * ordersNumber) + grade) / (ordersNumber + 1);

        int i = 0;
        try {
            i = orderMapper.endOrderStudent(fixTableId, massage, newAvgGrade);
        } catch (RuntimeException e) {
            throw new MyDBError("修改数据错误：", e);
        }
        return i;
    }

    @Transactional
    public int createOrder(
            String studentId,
            String faultClass,
            String address,
            String contacts,
            String phone,
            String faultDetails
    ) {
        // 检查这个错误类型是否是在数据库中的
        faultService.isExist(faultClass);
        int i = 0;
        try {
            i = orderMapper.createOrder(studentId, contacts, phone, faultClass, address, faultDetails);
        } catch (RuntimeException e) {
            throw new MyDBError("创建数据错误", e);
        }
        return i;
    }


    @Transactional
    public int setOrderWorker(String adminId, String workId, long fixTableId) {
        int i = 0;
        try {
            // 别忘了再插入管理员的 id
            i = orderMapper.setOrderWorker(adminId, workId, fixTableId);
        } catch (RuntimeException e) {
            throw new MyDBError("指定订单处理工作人员失败", e);
        }
        return i;
    }


    public List<Orders> getStudentHistoryList(String studentId) {
        List<Orders> studentHistoryList = orderMapper.getStudentHistoryList(studentId);
        if (studentHistoryList == null) {
            throw new BizException(CommonEnum.NOT_FOUND);
        }
        return studentHistoryList;
    }

    public Orders getStudentHistoryDetail(long fixTableId) {
        Orders historyDetail = orderMapper.getHistoryDetail(fixTableId);
        if (historyDetail == null) {
            throw new BizException(CommonEnum.NOT_FOUND);
        }
        return historyDetail;
    }

    public List<Orders> getWorkerHistoryList(String workId) {
        List<Orders> workerHistoryList = orderMapper.getWorkerHistoryList(workId);
        if (workerHistoryList == null) {
            throw new BizException(CommonEnum.NOT_FOUND);
        }
        return workerHistoryList;
    }

    public Orders getWorkerHistoryDetail(long fixTableId) {
        Orders historyDetail = orderMapper.getHistoryDetail(fixTableId);
        if (historyDetail == null) {
            throw new BizException(CommonEnum.NOT_FOUND);
        }
        return historyDetail;
    }

    public List<Orders> getTodayOrdersList(String workId) {
        return orderMapper.getTodayOrdersList(workId);
    }

    public List<Map<String,Object>> getFaultClassCount(String workId){
        return orderMapper.getFaultClassCount(workId);
    }

    public List<Map<String,Object>> getToMonthOrdersInWorker(String workId){
        return orderMapper.getToMonthOrdersInWorker(workId);
    }

    public List<Map<String,Object>> getToMonthOrders(){
        return orderMapper.getToMonthOrders();
    }

    public List<Map<String, Object>> getOrderClassNumber(){
        return orderMapper.getOrderClassNumber();
    }

    public int getOrderNumber(){
        return orderMapper.getOrderNumber();
    }
}
