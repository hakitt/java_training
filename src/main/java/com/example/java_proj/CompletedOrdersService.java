package com.example.java_proj;

import java.util.ArrayList;

public interface CompletedOrdersService {

    CompletedOrders addCompletedOrder(CompletedOrders completedOrders);

    ArrayList<CompletedOrders> fetchCompletedOrdersList();

    CompletedOrders updateCompletedOrder(CompletedOrders completedOrders);
}
