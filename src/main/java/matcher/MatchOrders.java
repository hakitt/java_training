package matcher;

import com.example.java_proj.CompletedOrders;
import com.example.java_proj.OrderService;
import com.example.java_proj.Orders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class MatchOrders {
    public MatchOrders(){
    }
    private boolean sorted = false;
    private boolean partialOrder = false;

    public ArrayList matchOrder(ArrayList<Orders> ordersList, Orders order, ArrayList<CompletedOrders> completedOrdersList) {
        sorted = false;
        partialOrder = false;
        for (int i = 0; i < ordersList.size(); i++) {
            if (order.getOrderType().equals("Buy") && ordersList.get(i).getOrderType().equals("Sell") && order.getPrice() >= ordersList.get(i).getPrice() && !sorted && (ordersList.get(i).getOrderStatus().equals("pending")||ordersList.get(i).getOrderStatus().equals("partial"))) {
                if (order.getQuantity() == ordersList.get(i).getQuantity()) {
                    order.setOrderStatus("complete");
                    ordersList.get(i).setOrderStatus("complete");
                    order.setPrice(ordersList.get(i).getPrice());
                    completedOrdersList.add(ordersToCompletedOrder(order));
                    completedOrdersList.add(ordersToCompletedOrder(ordersList.get(i)));
                    //reduceAggOrders(ordersList.get(i).getPrice(), ordersList.get(i).getQuantity(), ordersList.get(i).getOrderType());
                    ordersList.remove(i);
                    sorted = true;
                    break;
                } else if (order.getQuantity() > ordersList.get(i).getQuantity()) {
                    ordersList.get(i).setOrderStatus("complete");
                    Orders partialCompletedOrder = new Orders(ordersList.get(i).getQuantity(), ordersList.get(i).getPrice(), "Buy", order.getAccountNumber(),order.getOrderNumber(),"partial");
                    order.setQuantity(order.getQuantity() - ordersList.get(i).getQuantity());
                    completedOrdersList.add(ordersToCompletedOrder(ordersList.get(i)));
                    completedOrdersList.add(ordersToCompletedOrder(partialCompletedOrder));
                    //reduceAggOrders(ordersList.get(i).getPrice(), ordersList.get(i).getQuantity(), ordersList.get(i).getOrderType());
                    ordersList.remove(i);
                    ordersList.add(order);
                    partialOrder = true;
                    break;
                } else if (order.getQuantity() < ordersList.get(i).getQuantity()) {
                    order.setOrderStatus("complete");
                    Orders partialCompletedOrder = new Orders(order.getQuantity(), ordersList.get(i).getPrice(), "Sell", ordersList.get(i).getAccountNumber(),ordersList.get(i).getOrderNumber(),"partial");
                    order.setPrice(ordersList.get(i).getPrice());
                    completedOrdersList.add(ordersToCompletedOrder(order));
                    completedOrdersList.add(ordersToCompletedOrder(partialCompletedOrder));
                    //reduceAggOrders(ordersList.get(i).getPrice(), order.getQuantity(), ordersList.get(i).getOrderType());
                    ordersList.get(i).setQuantity(ordersList.get(i).getQuantity() - order.getQuantity());
                    sorted = true;
                    break;
                }
            }
            else if (order.getOrderType().equals("Sell") && ordersList.get(i).getOrderType().equals("Buy") && order.getPrice() <= ordersList.get(i).getPrice() && !sorted && (ordersList.get(i).getOrderStatus().equals("pending")||ordersList.get(i).getOrderStatus().equals("partial"))) {
                if (order.getQuantity() == ordersList.get(i).getQuantity()) {
                    order.setOrderStatus("complete");
                    ordersList.get(i).setOrderStatus("complete");
                    order.setPrice(ordersList.get(i).getPrice());
                    completedOrdersList.add(ordersToCompletedOrder(order));
                    completedOrdersList.add(ordersToCompletedOrder(ordersList.get(i)));
                    //reduceAggOrders(ordersList.get(i).getPrice(), ordersList.get(i).getQuantity(), ordersList.get(i).getOrderType());
                    ordersList.remove(i);
                    sorted = true;
                    break;
                } else if (order.getQuantity() > ordersList.get(i).getQuantity()) {
                    ordersList.get(i).setOrderStatus("complete");
                    Orders partialCompletedOrder = new Orders(ordersList.get(i).getQuantity(), ordersList.get(i).getPrice(), "Sell", order.getAccountNumber(),order.getOrderNumber(),"partial");
                    order.setQuantity(order.getQuantity() - ordersList.get(i).getQuantity());
                    completedOrdersList.add(ordersToCompletedOrder(ordersList.get(i)));
                    completedOrdersList.add(ordersToCompletedOrder(partialCompletedOrder));
                    //reduceAggOrders(ordersList.get(i).getPrice(), ordersList.get(i).getQuantity(), ordersList.get(i).getOrderType());
                    ordersList.remove(i);
                    partialOrder = true;
                    break;
                } else if (order.getQuantity() < ordersList.get(i).getQuantity()) {
                    order.setOrderStatus("complete");
                    Orders partialCompletedOrder = new Orders(order.getQuantity(), ordersList.get(i).getPrice(), "Buy", ordersList.get(i).getAccountNumber(),ordersList.get(i).getOrderNumber(),"partial");
                    order.setPrice(ordersList.get(i).getPrice());
                    completedOrdersList.add(ordersToCompletedOrder(order));
                    completedOrdersList.add(ordersToCompletedOrder(partialCompletedOrder));
//                    reduceAggOrders(ordersList.get(i).getPrice(), order.getQuantity(), ordersList.get(i).getOrderType());
                    ordersList.get(i).setQuantity(ordersList.get(i).getQuantity() - order.getQuantity());
                    sorted = true;
                    break;
                }
            }
        }
        if (!sorted & !partialOrder){
//            orderBook.getOrders().add(order);
//            aggregateOrder(order);
            ordersList.add(order);
        }
        else if (partialOrder){
            matchOrder(ordersList, order, completedOrdersList);
        }
        return ordersList;
    }

//    private void aggregateOrder(Orders order){
//        boolean orderAggregated = false;
//        AggOrder aggOrder = new AggOrder(order.getQuantity(),order.getPrice());
//        if (order.getOrderType().equals("Buy")){
//            for (int i=0;i<orderBook.getAggBuyOrders().size();i++) {
//                if (orderBook.getAggBuyOrders().get(i).getPrice()==order.getPrice()){
//                    double newQuantity = order.getQuantity() + orderBook.getAggBuyOrders().get(i).getQuantity();
//                    orderBook.getAggBuyOrders().get(i).setQuantity(newQuantity);
//                    orderAggregated = true;
//                }
//            }
//        }
//        else if (order.getOrderType().equals("Sell")){
//            for (int i=0;i<orderBook.getAggSellOrders().size();i++) {
//                if (orderBook.getAggSellOrders().get(i).getPrice()==order.getPrice()){
//                    double newQuantity = order.getQuantity() + orderBook.getAggSellOrders().get(i).getQuantity();
//                    orderBook.getAggSellOrders().get(i).setQuantity(newQuantity);
//                    orderAggregated = true;
//                }
//            }
//        }
//        if (!orderAggregated && order.getOrderType().equals("Buy")){
//            orderBook.getAggBuyOrders().add(aggOrder);
//        }
//        else if (!orderAggregated && order.getOrderType().equals("Sell")){
//            orderBook.getAggSellOrders().add(aggOrder);
//        }
//    }

//    private void reduceAggOrders(double price, double quantity, String action){
//        if (action.equals("Buy")){
//            for (int i=0;i<orderBook.getAggBuyOrders().size();i++) {
//                if (price == orderBook.getAggBuyOrders().get(i).getPrice()) {
//                    double newQuantity = orderBook.getAggBuyOrders().get(i).getQuantity() - quantity;
//                    if (quantity>0) {
//                        orderBook.getAggBuyOrders().get(i).setQuantity(newQuantity);
//                    }
//                    else if (quantity==0){
//                        orderBook.getAggBuyOrders().remove(i);
//                    }
//                }
//            }
//        }
//        else if (action.equals("Sell")){
//            for (int i=0;i<orderBook.getAggSellOrders().size();i++) {
//                if (price == orderBook.getAggSellOrders().get(i).getPrice()) {
//                    double newQuantity = orderBook.getAggSellOrders().get(i).getQuantity() - quantity;
//                    if (quantity>0) {
//                        orderBook.getAggSellOrders().get(i).setQuantity(newQuantity);
//                    }
//                    else if (quantity==0){
//                        orderBook.getAggSellOrders().remove(i);
//                    }
//                }
//            }
//        }
//    }
    public CompletedOrders ordersToCompletedOrder(Orders order){
        CompletedOrders completedOrders = new CompletedOrders(order.getQuantity(),order.getPrice(),order.getOrderType(),order.getAccountNumber(),order.getOrderNumber(),order.getOrderStatus());
        return completedOrders;
    }
    public Orders completeOrderToOrder(CompletedOrders order){
        Orders orders = new Orders(order.getQuantity(),order.getPrice(),order.getOrderType(),order.getAccountNumber(),order.getOrderNumber(),order.getOrderStatus());
        return orders;
    }
}
