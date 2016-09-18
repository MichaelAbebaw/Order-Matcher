package se.minitrading;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds orderbooks for all Securities
 */
public class Instrument {

    private List<OrderBook> securityList;

    /**
     * Constructor
     */
    public Instrument(){
        securityList = new ArrayList<OrderBook>();
    }

    /**
     * Gets securities currently listed for trading
     */
    public List<OrderBook> getLists(){
        return securityList;
    }

    /**
     * Adds new security to the listing for trading
     * @param orderBook is the new security to be added in the listing
     * @return true if adding succeeds, otherwise false
     */
    public Boolean addSecurity(OrderBook orderBook){
        return securityList.add(orderBook);
    }

    /**
     * Removes a security from the security listing
     * @param symbol
     * @return true if removing succeeds, otherwise false
     */
    public Boolean removeSecurity(String symbol){
        return securityList.remove(getOrderBook(symbol));
    }

    /**
     * Checks if a security is listed
     * @param security is the symbol of the instrument
     * @return true if the security is found, otherwise false
     */
    public OrderBook getOrderBook(String security){
        for(OrderBook orderBook : securityList){
            if (orderBook.getSymbol().equalsIgnoreCase(security)){
                return orderBook;
            }
        }
        return null;
    }
}