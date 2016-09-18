package se.minitrading;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Holds the history of all executed (filled) trades.
 *
 */
public class FillBook {

    private List<TradeEntry> tradeLog;

    /**
     * Constructor
     */
    public FillBook() {
        this.tradeLog = new ArrayList<TradeEntry>();
    }

    /**
     * Add trade entry log
     */
    public void addEntry(TradeEntry tradeEntry) {
        tradeLog.add(tradeEntry);
    }

    /**
     * Return collection of trade logs
     */
    public List<TradeEntry> getTradeLog() {
        return tradeLog;
    }
}
