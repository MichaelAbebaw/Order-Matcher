package se.minitrading;

import static org.junit.Assert.*;
import java.util.Date;
import org.junit.Before;
import org.junit.Test;

public class FillBookTest {

    Order or1, or2, or3;
    Order sl1, sl2, sl3;
    TradeEntry t1,t2,t3,t4;

    @Before
    public void setUp() throws Exception {
        or1 = new Order(Order.OrderType.BUY, 1, 1);
        or2 = new Order(Order.OrderType.BUY, 1, 2);
        sl1 = new Order(Order.OrderType.SELL, 1, 1);
        sl2 = new Order(Order.OrderType.SELL, 1, 2);

        t1 = new TradeEntry(or1, 1, 1, new Date());
        t2 = new TradeEntry(or2, 1, 2, new Date());
        t3 = new TradeEntry(sl1, 1, 1, new Date());
        t4 = new TradeEntry(sl2, 1, 2, new Date());

    }

    @Test
    public void testGetTradeLog() {
        FillBook fb = new FillBook();
        fb.addEntry(t1);
        fb.addEntry(t2);
        fb.addEntry(t3);
        fb.addEntry(t4);
        assertEquals(4, fb.getTradeLog().size());
    }

}