package com.example.java_proj;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

@Component
public interface CompletedOrdersRepository extends CrudRepository<CompletedOrders, Integer> {

}
