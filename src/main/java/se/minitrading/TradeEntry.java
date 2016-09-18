package se.minitrading;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A plain old data type to store a single trade entry
 *
 */
public class TradeEntry {

    private Date tradingTime;
    private Order order;
    private int volume;
    private int price;

    /**
     * Constructor
     * @param order is the order being executed.
     * @param volume is the amount of order executed.
     * @param price is the price the order executed.
     * @param tradingTime is the time of execution.
     */
    public TradeEntry(Order order, int volume, int price, Date tradingTime) {
        this.order = order;
        this.volume = volume;
        this.price = price;
        this.tradingTime = tradingTime;
    }

    @Override
    public String toString() {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        return df.format(tradingTime) + "\t" + volume + "@" + price;
    }

    /**
     * Returns volume of the trade.
     */
    public int getVolume() {
        return volume;
    }

    /**
     * Returns the price of the trade.
     */
    public int getPrice() {
        return price;
    }

    /**
     * Returns the price of the trade.
     */
    public Order getOrder() {
        return order;
    }

    /**
     * Returns time stamp of the trade.
     */
    public Date getTradeTime(){
        return tradingTime;
    }
}