package com.example.java_proj;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Data
@NoArgsConstructor
@Builder
public class Orders {
    @Positive(message = "Quantity must be greater than 0")
    private double quantity;
    @Positive(message = "Price must be greater than 0")
    private double price;
    @NotNull(message = "Action must be 'Buy' or 'Sell'")
    @NotEmpty
    private String orderType;
    private int accountNumber;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int orderNumber;
    @NotNull
    private String orderStatus;

    public Orders(double quantity, double price, String orderType, int accountNumber, int orderNumber, String orderStatus){
        this.quantity = quantity;
        this.price = price;
        this.orderType = orderType;
        this.accountNumber = accountNumber;
        this.orderNumber = orderNumber;
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public double getQuantity() {
        return quantity;
    }

    public String getOrderType() {
        return orderType;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }
}
