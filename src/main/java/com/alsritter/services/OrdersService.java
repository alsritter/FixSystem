package com.alsritter.services;

import com.alsritter.mappers.OrderMapper;
import com.alsritter.pojo.Orders;
import com.alsritter.utils.BizException;
import com.alsritter.utils.CommonEnum;
import com.alsritter.utils.MyDBError;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

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

    public List<Orders> getAllOrders() {
        return orderMapper.getAllOrders();
    }

    public Orders getOrder(long fixTableId){
        return orderMapper.getOrder(fixTableId);
    }

    /**
     * <b>
     * 用来查询当前学生的所有正在处理的订单
     * </b>
     * <br>
     *
     * @return : com.alsritter.pojo.Orders
     * @param studentId : 学生的 id
     */
    public List<Orders> getOrdersOfStudent(String studentId){
        return orderMapper.getOrdersOfStudent(studentId);
    }

    /**
     * <b>
     * 用来查询当前工人的所有正在处理的订单
     * </b>
     * <br>
     *
     * @return : com.alsritter.pojo.Orders
     * @param workerId : 工人的 id
     */
    public List<Orders> getOrdersOfWorker(String workerId){
        return orderMapper.getOrdersOfWorker(workerId);
    }

    /**
     * <b>
     * 检查当前订单是否由该工人进行处理的，
     * 如果能用该工人的 id 和该订单号找到
     * 数据表示该订单是由该工人处理
     *
     * 同时检查当前订单是否处于正在进行的状态（isOngoing）
     * </b>
     * <br>
     *
     * @param workerId : 工人的id
     * @param fixTableId : 单号
     */
    public void isExist(String workerId,long fixTableId){
        // 先检查是否是当前工人处理的单
        Orders exist = orderMapper.isExist(workerId, fixTableId);
        if (exist == null) {
            throw new BizException(CommonEnum.FORBIDDEN.getResultCode(),"不是当前工人的订单");
        }

        // 再判断当前订单是否是正在处理的 1
        if (exist.getState() != 1) {
            throw new BizException(CommonEnum.FORBIDDEN.getResultCode(),"当前订单未接单或已处理");
        }
    }

    /**
     * <b>
     * 和上面的那个作用差不多
     *
     * 检查当前订单是否当前学生发起的，
     * 如果能用该学生的 id 和该订单号找到
     * 数据表示该订单是由该工人处理
     *
     * 同时检查当前订单是否处于已经是 2 状态，且 Massage 为 null
     * 注意：是 Null 而不是 “空”
     * </b>
     * <br>
     *
     * @param studentId : 学生的id
     * @param fixTableId : 单号
     */
    public void isExistStudent(String studentId, long fixTableId){
        // 先检查是否是当前学生的订单
        Orders exist = orderMapper.isExistStudent(studentId, fixTableId);
        if (exist == null) {
            throw new BizException(CommonEnum.FORBIDDEN.getResultCode(),"不是当前学生的订单");
        }

        // 再判断当前订单是否是已处理的 2
        if (exist.getState() != 2) {
            throw new BizException(CommonEnum.FORBIDDEN.getResultCode(), "当前订单未已处理");
        }

        // 最后判断当前订单没有评价过
        if (exist.getMassage() != null) {
            throw new BizException(CommonEnum.FORBIDDEN.getResultCode(), "当前订单已评价");
        }
    }

    /**
     * <b>
     * 这个是给工人评价的
     * </b>
     * <br>
     *
     * @return : int
     * @param fixTableId :
     * @param resultDetails :
     */
    @Transactional
    public int endOrder(long fixTableId, String resultDetails){
        int i = 0;
        try {
            i = orderMapper.endOrder(fixTableId,resultDetails);
        } catch (RuntimeException e) {
            throw new MyDBError("修改数据错误：", e);
        }
        return i;
    }

    /**
     * <b>
     * 这个是给学生评价的
     * </b>
     * <br>
     *
     * @return : int
     * @param fixTableId :
     * @param massage :
     * @param grade :
     */
    @Transactional
    public int endOrder(long fixTableId, String massage, Integer grade){
        int i = 0;
        try {
            i = orderMapper.endOrderStudent(fixTableId, massage, grade);
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
        int i = 0;
        try {
            i = orderMapper.createOrder(studentId, contacts, phone, faultClass, address, faultDetails);
        } catch (RuntimeException e) {
            throw new MyDBError("创建数据错误", e);
        }
        return i;
    }




}
