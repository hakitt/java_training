package matcher;

import org.springframework.stereotype.Component;

@Component
public class AggOrder {
    private double quantity;
    private double price;

    public AggOrder(double quantity, double price){
        this.quantity=quantity;
        this.price=price;
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
}