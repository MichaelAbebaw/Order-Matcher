package se.minitrading;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.ArrayList;

/**
 * OrderBook maintains two pools - buy and sell pools - that store currently available bids and asks.
 */

public class OrderBook {

    /**
     *  buyPool stores orders in descending order of price and then order time.
     *  sellPool stores orders in ascending order of price and then order time.
     */
    private String symbol;
    private TreeMap<Order, Integer> buyPool;
    private TreeMap<Order, Integer> sellPool;
    private FillBook fillBook;
    private List<TradeEntry> currentTradeEntries;

    /**
     * Constructor
     */
    public OrderBook(String symbol) {
        this.symbol = symbol;
        this.buyPool = new TreeMap<Order, Integer>();
        this.sellPool = new TreeMap<Order, Integer>();
        this.fillBook = new FillBook();
        this.currentTradeEntries = new ArrayList<TradeEntry>();
    }

    /**
     * Execute trades for both buy or a sell order.
     * @param order to trade.
     * @return true if at least one trading has occurred, false otherwise.
     */
    public boolean trade(Order order) {

        // clear old trade entries
        currentTradeEntries.clear();

        Order.OrderType orderType = order.getOrderType();

        TreeMap<Order, Integer> ownPool = orderType == Order.OrderType.BUY ? buyPool : sellPool;
        TreeMap<Order, Integer> otherPool = orderType == Order.OrderType.BUY ? sellPool : buyPool;
        Map.Entry<Order, Integer> entry = getEntry(orderType);

        if (otherPool.isEmpty() || !canTrade(orderType, order.getPrice(), entry.getValue())) {
            ownPool.put(order, order.getPrice());
            return false;
        }

        int orderVolume = order.getVolume();
        int orderPrice = order.getPrice();
        int entryVolume = entry.getKey().getVolume();
        int entryPrice = entry.getKey().getPrice();
        int delta = orderVolume - entryVolume;

        // is the order volume completely satisfied/complete in trading?
        boolean isVolumeOut = false;

        while (canTrade(orderType, orderPrice, entryPrice)) {
            if (delta <= 0) {
                entry.getKey().setVolume(-delta);
                if (delta == 0) {
                    // on-the-spot removal of an entry
                    removeEntry(orderType);
                }

                // current trade entry
                TradeEntry te = new TradeEntry(order, orderVolume, entryPrice, new Date());

                // save current trade
                currentTradeEntries.add(te);

                // log current trade to history
                fillBook.addEntry(te);

                isVolumeOut = true;

                break;
            } else {

                removeEntry(orderType);
                order.setVolume(delta);

                TradeEntry te = new TradeEntry(order, entryVolume, entryPrice, new Date());
                currentTradeEntries.add(te);
                fillBook.addEntry(te);

                if (!otherPool.isEmpty()) {
                    entry = getEntry(orderType);
                    entryVolume = entry.getKey().getVolume();
                    entryPrice = entry.getValue();
                    orderVolume = delta;
                    delta = orderVolume - entryVolume;
                } else {
                    break;
                }
                isVolumeOut = false;
            }
        }

        // we still have some volumes in our order that we couldn't buy/sell
        if (!isVolumeOut) {
            ownPool.put(order, order.getPrice());
        }
        return true;
    }

    /**
     * Checks to see if we can do a trading based on the order type and prices.
     * @param orderType the order type - either a BUY or SELL
     * @param orderPrice the order price.
     * @param entryPrice the best market price available for an order.
     * @return true if a trading can happen, false if it can't.
     */
    private boolean canTrade(Order.OrderType orderType, int orderPrice, int entryPrice) {
        return orderType == Order.OrderType.BUY ? orderPrice >= entryPrice : orderPrice <= entryPrice;
    }

    /**
     * Gets the entry (in either the buy or sell pool) that represents the best available market price.
     * @param orderType the order type.
     * @return entry that represents the best available market price for the order.
     */
    private Map.Entry<Order, Integer> getEntry(Order.OrderType orderType) {
        return orderType == Order.OrderType.BUY ? sellPool.firstEntry() : buyPool.firstEntry();
    }

    /**
     * Removes an order entry in either the buy or sell pools (depending on the order type).
     * @param orderType the order type.
     * @return the order that is removed.
     */
    private Map.Entry<Order, Integer> removeEntry(Order.OrderType orderType) {
        return orderType == Order.OrderType.BUY ? sellPool.pollFirstEntry() : buyPool.pollFirstEntry();
    }

    /**
     * Returns the buy pool.
     */
    public TreeMap<Order, Integer> getBuyPool() {
        return buyPool;
    }

    /**
     * Returns the sell pool.
     */
    public TreeMap<Order, Integer> getSellPool() {
        return sellPool;
    }

    /**
     * Gets the Ask price.
     */
    public int getAskPrice() {
        return sellPool.firstKey().getPrice();
    }

    /**
     * Gets the Bid price.
     */
    public int getBidPrice() {
        return buyPool.firstKey().getPrice();
    }

    /**
     * Returns collection of trades.
     */
    public List<TradeEntry> getTradeEntries() {
        return currentTradeEntries;
    }

    /**
     * Returns log of trades.
     */
    public FillBook getFillBook() {
        return fillBook;
    }

    /**
     * Sets the underlined security symbol
     */
    public void setSymbol(String symbol){
        this.symbol = symbol;
    }

    /**
     * Gets the underlined security symbol
     */
    public String getSymbol(){
        return symbol;
    }
}
