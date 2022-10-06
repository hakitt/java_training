package com.example.java_proj;

import matcher.MatchOrders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class matcherTests {
    private MatchOrders matcher;
    private ArrayList<Orders> emptyOrderArray = new ArrayList<>();
    private Account account;
    private ArrayList<CompletedOrders> completedOrdersArrayList = new ArrayList<>();

    @BeforeEach
    void setUp(){
        account = new Account(0,"","","");
        account.setAccountNumber(1);
        matcher = new MatchOrders();
    }

    @Test
    @DisplayName("Check adding single order to empty matcher")
    void testFirstOrder(){
        Orders order = new Orders(10,10,"Buy",1,1, "pending");
        matcher.matchOrder(emptyOrderArray,order,completedOrdersArrayList);
        assertEquals(emptyOrderArray.get(0),order);
    }

    @Test
    @DisplayName("Check adding two orders to empty matcher")
    void testTwoOrders(){
        Orders order1 = new Orders(10,10,"Buy",1,1, "pending");
        Orders order2 = new Orders(11,11,"Sell",1,1, "pending");
        matcher.matchOrder(emptyOrderArray,order1,completedOrdersArrayList);
        matcher.matchOrder(emptyOrderArray,order2,completedOrdersArrayList);
        assertEquals(emptyOrderArray.size(),2);
    }

    @Test
    @DisplayName("Test simple match")
    void testEqualMatch(){
        Orders order1 = new Orders(10,10,"Buy",1,1, "pending");
        Orders order2 = new Orders(10,10,"Sell",1,1, "pending");
        matcher.matchOrder(emptyOrderArray,order1,completedOrdersArrayList);
        matcher.matchOrder(emptyOrderArray,order2,completedOrdersArrayList);
        assertEquals(0,emptyOrderArray.size());
        assertEquals(2,completedOrdersArrayList.size());
    }

    @Test
    @DisplayName("Test simple match with varying quantities")
    void testMatch2(){
        Orders order1 = new Orders(10,10,"Buy",1,1, "pending");
        Orders order2 = new Orders(12,5,"Sell",1,1, "pending");
        matcher.matchOrder(emptyOrderArray,order1,completedOrdersArrayList);
        matcher.matchOrder(emptyOrderArray,order2,completedOrdersArrayList);
        assertEquals(1,emptyOrderArray.size());
        assertEquals(2,completedOrdersArrayList.size());
    }

    @Test
    @DisplayName("Test match with varying quantities 2")
    void testMatch3(){
        Orders order1 = new Orders(10,10,"Buy",1,1, "pending");
        Orders order2 = new Orders(8,5,"Sell",1,1, "pending");
        matcher.matchOrder(emptyOrderArray,order1,completedOrdersArrayList);
        matcher.matchOrder(emptyOrderArray,order2,completedOrdersArrayList);
        assertEquals(emptyOrderArray.size(),1);
        assertEquals(completedOrdersArrayList.size(),2);
    }

    @Test
    @DisplayName("Test adding orders that don't match")
    void testNoMatch(){
        Orders order1 = new Orders(10,10,"Buy",1,1, "pending");
        Orders order2 = new Orders(10,20,"Sell",1,1, "pending");
        matcher.matchOrder(emptyOrderArray,order1,completedOrdersArrayList);
        matcher.matchOrder(emptyOrderArray,order2,completedOrdersArrayList);
        assertEquals(emptyOrderArray.size(),2);
        assertEquals(completedOrdersArrayList.size(),0);
    }

    @Test
    @DisplayName("Test transaction")
    void testTransaction(){
        Orders order1 = new Orders(10,10,"Buy",1,1, "pending");
        Orders order2 = new Orders(20,10,"Buy",1,1, "pending");
        Orders order3 = new Orders(20,10,"Sell",1,1, "pending");
        matcher.matchOrder(emptyOrderArray,order1,completedOrdersArrayList);
        matcher.matchOrder(emptyOrderArray,order2,completedOrdersArrayList);
        matcher.matchOrder(emptyOrderArray,order3,completedOrdersArrayList);
        assertEquals(4,completedOrdersArrayList.size());
        assertEquals(1,emptyOrderArray.size());
    }
}
