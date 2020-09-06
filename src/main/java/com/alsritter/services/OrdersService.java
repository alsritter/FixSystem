package com.alsritter.services;

import com.alsritter.mappers.OrderMapper;
import com.alsritter.pojo.Orders;
import org.springframework.stereotype.Service;

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
}
