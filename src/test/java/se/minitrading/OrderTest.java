package se.minitrading;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class OrderTest {

    Order or1, or2;

    @Before
    public void setUp() throws Exception {
        or1 = new Order(Order.OrderType.BUY, 1, 3);
        or2 = new Order(Order.OrderType.SELL, 3, 1);
    }

    @Test
    public void testToString() {
        assertEquals("BUY 1@3", or1.toString());
        assertEquals("SELL 3@1", or2.toString());
    }

    @Test
    public void testGetPrice() {
        assertEquals(3, or1.getPrice());
        assertEquals(1, or2.getPrice());
    }

    @Test
    public void testGetVolume() {
        assertEquals(1, or1.getVolume());
        assertEquals(3, or2.getVolume());
    }

    @Test
    public void testGetOrderType() {
        assertEquals(Order.OrderType.BUY, or1.getOrderType());
        assertEquals(Order.OrderType.SELL, or2.getOrderType());
    }

    @Test
    public void testSetVolume() {
        or1.setVolume(3);
        or2.setVolume(4);
        assertEquals(3, or1.getVolume());
        assertEquals(4, or2.getVolume());
    }
}
