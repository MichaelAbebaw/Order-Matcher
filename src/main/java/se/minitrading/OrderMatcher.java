package se.minitrading;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.regex.Pattern;

public class OrderMatcher {

    private final String re1= "(buy|sell)[ ]([a-z]|[A-Z])+[ ][1-9][0-9]*@[1-9][0-9]*";
    private final String re2 = "(add|remove|print|price|history)[ ]([a-z]|[A-Z])+";

    /**
     * Main function, program entry point
     */
    public static void main(String[] args) {
        OrderMatcher orderMatcher = new OrderMatcher();
        orderMatcher.start();
    }

    /**
     * Parses user input and starts trading. Stops on Ctrl+D.
     */
    private void start() {

        Pattern p1 = Pattern.compile(re1, Pattern.CASE_INSENSITIVE);
        Pattern p2 = Pattern.compile(re2, Pattern.CASE_INSENSITIVE);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))){

            String line = null;
            OrderBook orderBook;
            Instrument instrument = new Instrument();
            System.out.println("Start inserting orders. [exit or Ctrl+D to stop]");
            System.out.println("----------------------------------------");

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                // If input is empty line continue loop
                if(line.isEmpty())
                    continue;

                // Match [buy, sell]
                if ((p1.matcher(line)).matches()) {
                    String[] orderLine = line.split(" ");
                    Order.OrderType orderType = orderLine[0].equalsIgnoreCase("BUY") ? Order.OrderType.BUY : Order.OrderType.SELL;
                    String[] values = orderLine[2].split("@");

                    // Check if the security exists
                    orderBook = instrument.getOrderBook(orderLine[1]);
                    if(orderBook == null){
                        System.out.println("[ERROR] security is not listed!");
                        continue;
                    }

                    // Execute trade.
                    boolean isTraded = orderBook.trade(new Order(orderType, Integer.parseInt(values[0]), Integer.parseInt(values[1])));
                    if (isTraded) {
                        for (TradeEntry tradeEntry : orderBook.getTradeEntries()) {
                            System.out.println("TRADE " + tradeEntry.getVolume() + "@" + tradeEntry.getPrice());
                        }
                    }
                }
                // Match [add, remove, print, price, history]
                else if((p2.matcher(line)).matches()){
                    String[] commandLine = line.split(" ");
                    orderBook = instrument.getOrderBook(commandLine[1]);
                    switch (commandLine[0].toUpperCase()){
                        case "ADD":
                            if(orderBook != null){
                                System.out.println("[ERROR] security already listed!");
                            } else {
                                instrument.addSecurity(new OrderBook(commandLine[1]));
                            }
                            break;
                        case "REMOVE":
                            if(orderBook == null){
                                System.out.println("[ERROR] security is not available!");
                            } else {
                                instrument.removeSecurity(commandLine[1]);
                            }
                            break;
                        case "PRINT":
                            if(orderBook == null){
                                System.out.println("[ERROR] security is not listed!");
                            } else {
                                System.out.println("--- SELL ---");
                                for (Order order : orderBook.getSellPool().navigableKeySet()) {
                                    System.out.println(order.toString());
                                }
                                System.out.println("--- BUY ---");
                                for (Order order : orderBook.getBuyPool().navigableKeySet()) {
                                    System.out.println(order.toString());
                                }
                            }
                            break;
                        case "PRICE":
                            if(orderBook == null){
                                System.out.println("[ERROR] security is not available!");
                            } else {
                                System.out.println("-- MARKET PRICE --");
                                try {
                                    System.out.println("Ask: " + orderBook.getAskPrice());
                                } catch (NoSuchElementException e) {
                                    System.out.println("Ask: [empty]");
                                }
                                try {
                                    System.out.println("Bid: " + orderBook.getBidPrice());
                                } catch (NoSuchElementException e) {
                                    System.out.println("Bid: [empty]");
                                }
                            }
                            break;
                        case "HISTORY":
                            if(orderBook == null){
                                System.out.println("[ERROR] security is not listed!");
                            } else {
                                System.out.println("--- TRADE HISTORY ---");
                                List<TradeEntry> tl = orderBook.getFillBook().getTradeLog();
                                ListIterator<TradeEntry> li = tl.listIterator(tl.size());
                                while (li.hasPrevious()) {
                        	        System.out.println(li.previous().toString());
                                }
                            }
                            break;
                        default:
            			    break;
                    }
                }
                // Match [list]
                else if(line.equalsIgnoreCase("list")){
                    System.out.println("--- SECURITIES ---");
                    for(OrderBook ob : instrument.getLists()){
                        System.out.println(ob.getSymbol());
                    }
                }
                // Match [exit]
                else if(line.equalsIgnoreCase("exit")){
                    break;
                }
                else{
                    System.out.println("[ERROR] bad input detected and ignored.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
