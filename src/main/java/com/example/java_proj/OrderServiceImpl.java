package com.example.java_proj;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService{

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public Orders addOrder(Orders order) {
        return orderRepository.save(order);
    }

    @Override
    public ArrayList<Orders> fetchOrderList() {
        return (ArrayList<Orders>) orderRepository.findAll();
    }

    @Override
    public Orders updateOrder(Orders order) {
        Orders orderDB = orderRepository.findById(order.getAccountNumber()).get();
        orderDB.setQuantity(order.getQuantity());
        orderDB.setPrice(order.getPrice());
        orderDB.setOrderStatus(order.getOrderStatus());
        if (orderDB.getQuantity()==0){
            orderRepository.delete(orderDB);
            return null;
        }
        else {
            return orderRepository.save(orderDB);
        }
    }

    @Override
    public void deleteOrderByID(Orders order) {
        orderRepository.deleteById(order.getOrderNumber());
    }

    @Override
    public Optional<Orders> findByID(int id) {
        return orderRepository.findById(id);
    }
}
