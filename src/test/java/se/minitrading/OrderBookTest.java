package se.minitrading;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class OrderBookTest {

    Order or1, or2, or3, or4, or5, or6, or7, or8, or9;
    Order sl1, sl2, sl3, sl4, sl5, sl6, sl7, sl8, sl9;
    OrderBook orderBook;

    @Before
    public void setUp() throws Exception {
        // Initialize order book.
        orderBook = new OrderBook("abc");

        // Buy orders.
        or1 = new Order(Order.OrderType.BUY, 1, 1);
        or2 = new Order(Order.OrderType.BUY, 1, 2);
        or3 = new Order(Order.OrderType.BUY, 1, 3);

        or4 = new Order(Order.OrderType.BUY, 2, 1);
        or5 = new Order(Order.OrderType.BUY, 2, 2);
        or6 = new Order(Order.OrderType.BUY, 2, 3);

        or7 = new Order(Order.OrderType.BUY, 3, 1);
        or8 = new Order(Order.OrderType.BUY, 3, 2);
        or9 = new Order(Order.OrderType.BUY, 3, 3);

        // Sell orders
        sl1 = new Order(Order.OrderType.SELL, 1, 1);
        sl2 = new Order(Order.OrderType.SELL, 1, 2);
        sl3 = new Order(Order.OrderType.SELL, 1, 3);

        sl4 = new Order(Order.OrderType.SELL, 2, 1);
        sl5 = new Order(Order.OrderType.SELL, 2, 2);
        sl6 = new Order(Order.OrderType.SELL, 2, 3);

        sl7 = new Order(Order.OrderType.SELL, 3, 1);
        sl8 = new Order(Order.OrderType.SELL, 3, 2);
        sl9 = new Order(Order.OrderType.SELL, 3, 3);
    }

    @Test
    public void testTrade() {
        // Buy price is less than sell price, thus no trade should occur.
        orderBook.trade(or1); // buy 1@1
        orderBook.trade(or4); // buy 2@1
        orderBook.trade(or7); // buy 3@1
        orderBook.trade(sl2); // sell 1@2
        orderBook.trade(sl5); // sell 2@2
        orderBook.trade(sl8); // sell 3@2

        assertEquals(3,orderBook.getBuyPool().size());
        assertEquals(1,orderBook.getBuyPool().firstEntry().getKey().getVolume());
        assertEquals(3,orderBook.getBuyPool().lastEntry().getKey().getVolume());

        assertEquals(3,orderBook.getSellPool().size());
        assertEquals(1,orderBook.getSellPool().firstEntry().getKey().getVolume());
        assertEquals(3,orderBook.getSellPool().lastEntry().getKey().getVolume());
        //

        // Sell price is equal to buy price 1@1, thus a trade should occur.
        orderBook.trade(sl1); // sell 1@1
        assertEquals(2,orderBook.getBuyPool().size());
        assertEquals(2,orderBook.getBuyPool().firstEntry().getKey().getVolume());
        assertEquals(3,orderBook.getBuyPool().lastEntry().getKey().getVolume());

        assertEquals(3,orderBook.getSellPool().size());
        assertEquals(1,orderBook.getSellPool().firstEntry().getKey().getVolume());
        assertEquals(3,orderBook.getSellPool().lastEntry().getKey().getVolume());

        // Buy price is equal to a sell price 1@2, thus a trade should occur.
        orderBook.trade(or2); // buy 1@2
        assertEquals(2,orderBook.getBuyPool().size());
        assertEquals(2,orderBook.getBuyPool().firstEntry().getKey().getVolume());
        assertEquals(3,orderBook.getBuyPool().lastEntry().getKey().getVolume());

        assertEquals(2,orderBook.getSellPool().size());
        assertEquals(2,orderBook.getSellPool().firstEntry().getKey().getVolume());
        assertEquals(3,orderBook.getSellPool().lastEntry().getKey().getVolume());

        // Sell order that span two orders, sl5 & sl8
        orderBook.trade(sl9); // sell 3@3
        assertEquals(2, orderBook.getBuyPool().firstEntry().getKey().getVolume());
        assertEquals(1, orderBook.getBuyPool().firstEntry().getKey().getPrice());

        assertEquals(3, orderBook.getSellPool().lastEntry().getKey().getVolume());
        assertEquals(3, orderBook.getSellPool().lastEntry().getKey().getPrice());
        assertEquals(2, orderBook.getSellPool().firstEntry().getKey().getVolume());
        assertEquals(2, orderBook.getSellPool().firstEntry().getKey().getPrice());

        // Multiple buy orders
        orderBook.trade(or5); // buy 2@2
        orderBook.trade(or5); // buy 2@2
        orderBook.trade(or5); // buy 2@2
        assertEquals(3, orderBook.getBuyPool().size());
        assertEquals(1, orderBook.getBuyPool().firstEntry().getKey().getVolume());
        assertEquals(2, orderBook.getBuyPool().firstEntry().getKey().getPrice());
        assertEquals(3, orderBook.getBuyPool().lastEntry().getKey().getVolume());
        assertEquals(1, orderBook.getBuyPool().lastEntry().getKey().getPrice());

        assertEquals(3, orderBook.getSellPool().firstEntry().getKey().getPrice());
        assertEquals(3, orderBook.getSellPool().firstEntry().getKey().getVolume());

        // Single sell orders
        orderBook.trade(sl7); // sell 3@1
        assertEquals(1, orderBook.getBuyPool().size());
        assertEquals(3, orderBook.getBuyPool().firstEntry().getKey().getVolume());
        assertEquals(1, orderBook.getBuyPool().firstEntry().getKey().getPrice());

        assertEquals(3, orderBook.getSellPool().firstEntry().getKey().getPrice());
        assertEquals(3, orderBook.getSellPool().firstEntry().getKey().getVolume());

        // Multiple orders
        orderBook.trade(or2); // buy 1@2
        orderBook.trade(sl6); // sell 2@3
        orderBook.trade(sl4); // sell 2@1
        assertEquals(2, orderBook.getBuyPool().firstEntry().getKey().getVolume());
        assertEquals(1, orderBook.getBuyPool().firstEntry().getKey().getPrice());

        assertEquals(2, orderBook.getSellPool().size());
        assertEquals(2, orderBook.getSellPool().lastEntry().getKey().getVolume());
        assertEquals(3, orderBook.getSellPool().lastEntry().getKey().getPrice());
        assertEquals(3, orderBook.getSellPool().firstEntry().getKey().getVolume());
        assertEquals(3, orderBook.getSellPool().firstEntry().getKey().getPrice());

    }

    @Test
    public void testGetAskPrice() {
        orderBook.trade(sl1); // sell 1@2
        orderBook.trade(sl5); // sell 2@2
        orderBook.trade(sl9); // sell 3@3
        assertEquals(1, orderBook.getAskPrice());
    }

    @Test
    public void testGetBidPrice() {
        orderBook.trade(or1); // buy 1@2
        orderBook.trade(or5); // buy 2@2
        orderBook.trade(or9); // buy 3@3
        assertEquals(3,orderBook.getBidPrice());
    }

    @Test
    public void testGetTradeEntries() {
        orderBook.trade(sl1); // sell 1@2
        orderBook.trade(or1); // buy 1@2
        assertEquals(1, orderBook.getTradeEntries().size());

        orderBook.trade(sl5); // sell 2@2
        orderBook.trade(or5); // buy 2@2
        assertEquals(1, orderBook.getTradeEntries().size());

        orderBook.trade(sl9); // sell 3@3
        orderBook.trade(or9); // buy 3@3
        assertEquals(1, orderBook.getTradeEntries().size());
    }

    @Test
    public void testGetFillBook() {
        orderBook.trade(sl1); // sell 1@2
        orderBook.trade(or1); // buy 1@2
        assertEquals(1, orderBook.getFillBook().getTradeLog().size());

        orderBook.trade(sl5); // sell 2@2
        orderBook.trade(or5); // buy 2@2
        assertEquals(2, orderBook.getFillBook().getTradeLog().size());

        orderBook.trade(sl9); // sell 3@3
        orderBook.trade(or9); // buy 3@3
        assertEquals(3, orderBook.getFillBook().getTradeLog().size());

    }
}
