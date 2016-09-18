package se.minitrading;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.junit.Before;
import org.junit.Test;

public class TradeEntryTest {

    TradeEntry tradeEntry;
    SimpleDateFormat df ;

    @Before
    public void setUp() throws Exception {
        tradeEntry = new TradeEntry( new Order(Order.OrderType.BUY, 10, 10) , 5, 10, new Date());
        df = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
    }

    @Test
    public void testToString() {

        assertEquals(df.format(tradeEntry.getTradeTime()) + "\t" + 5 + "@" + 10, tradeEntry.toString());
    }
}
