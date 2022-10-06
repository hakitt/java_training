package com.example.java_proj;

import java.util.ArrayList;
import java.util.Optional;

public interface OrderService {

    Orders addOrder(Orders order);

    ArrayList<Orders> fetchOrderList();

    Orders updateOrder(Orders order);

    void deleteOrderByID(Orders order);

    Optional<Orders> findByID(int id);
}
