package com.ht.tradeprocess.entity;

public class Report {
    private String account;
    private String instrument;
    private int quantity;
    private String trades;

    public Report(String account, String instrument, int quantity, String trades) {
        this.account = account;
        this.instrument = instrument;
        this.quantity = quantity;
        this.trades = trades;
    }

    public Report(Trade trade) {
        this.account = trade.getAccountNumber();
        this.instrument = trade.getSecurityIdentifier();
        this.quantity = trade.getPositionQuantity();
        this.trades = Integer.toString(trade.getTradeId());
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getInstrument() {
        return instrument;
    }

    public void setInstrument(String instrument) {
        this.instrument = instrument;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getTrades() {
        return trades;
    }

    public void setTrades(String trades) {
        this.trades = trades;
    }
}
