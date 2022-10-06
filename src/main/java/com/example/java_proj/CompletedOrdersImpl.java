package com.example.java_proj;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CompletedOrdersImpl implements CompletedOrdersService{
    @Autowired
    private CompletedOrdersRepository completedOrdersRepository;
    @Override
    public CompletedOrders addCompletedOrder(CompletedOrders completedOrders) {
        return completedOrdersRepository.save(completedOrders);
    }

    @Override
    public ArrayList<CompletedOrders> fetchCompletedOrdersList() {
        return (ArrayList<CompletedOrders>) completedOrdersRepository.findAll();
    }

    @Override
    public CompletedOrders updateCompletedOrder(CompletedOrders completedOrders) {
        CompletedOrders orderDB = completedOrdersRepository.findById(completedOrders.getAccountNumber()).get();
        orderDB.setOrderStatus("complete");
        return completedOrdersRepository.save(orderDB);
    }
}
