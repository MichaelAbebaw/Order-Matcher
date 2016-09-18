package se.minitrading;

import java.util.Date;
import java.util.UUID;

/**
 *
 * An order that abstracts both a BUY and a SELL order. We distinguish each based on the orderType.
 *
 */
public class Order implements Comparable<Order> {

    // Order operations
    enum OrderType { SELL, BUY }
    private OrderType orderType;
    private int volume;
    private int price;
    private String ticketNumber;
    Date orderTime;

    /**
     * Constructor
     * @param orderType is the type of order passed, i.e. BUY | SELL.
     * @param volume is the volume of the order.
     * @param price is the price of the order.
     */
    public Order(OrderType orderType, int volume, int price) {
        this.ticketNumber = UUID.randomUUID().toString();
        this.orderType = orderType;
        this.volume = volume;
        this.price = price;
        this.orderTime = new Date();
    }

    /**
     *
     * Orders are compared in the order of price and order time.
     * @param o the order to match against this order.
     * @return negative number if this object is less than o, positive number if it is greater, and non-zero to
     * indicate equality.
     */
    @Override
    public int compareTo(Order o) {
        if (this.price != o.price) {
            if (o.orderType == Order.OrderType.SELL) { // ascending order
                return this.price - o.price;
            } else {    // descending order
                return o.price - this.price;
            }
        }

        int diff = this.orderTime.compareTo(o.orderTime);
        if (diff != 0) {
            return diff;
        }

        // objects with same price and time are also different.
        // We can return any value other than 0 here
        return 1;
    }

    /**
     * @return a user-friendly representation of an order.
     */
    @Override
    public String toString() {
        String str = orderType == OrderType.BUY ? "BUY " : "SELL ";
        return str + volume + "@" + price;
    }

    /**
     * Returns the price of the order
     */
    public int getPrice() {
        return price;
    }

    /**
     * Returns the volume of the order
     */
    public int getVolume() {
        return volume;
    }

    /**
     * Gets the operation of the order, i.e. buy | sell
     */
    public OrderType getOrderType() {
        return orderType;
    }

    /**
     * Returns the ticket number of the order
     */
    public String getTicketNumber() {
        return ticketNumber;
    }

    /**
     * Set order volume
     */
    public void setVolume(int volume){
        this.volume = volume;
    }
}
