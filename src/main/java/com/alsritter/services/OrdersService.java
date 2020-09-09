package com.alsritter.services;

import com.alsritter.mappers.OrderMapper;
import com.alsritter.pojo.Orders;
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

    public Orders getOrders(long fixTableId){
        return orderMapper.getOrder(fixTableId);
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
            throw new MyDBError("修改数据错误", e);
        }
        return i;
    }
}
