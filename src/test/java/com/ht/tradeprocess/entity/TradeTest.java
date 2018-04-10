package com.ht.tradeprocess.entity;



import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TradeTest  {

    @Test
    public void setPositionQuantityWhenBUY_NEW() {
        Trade trade = new Trade(1234,1,"XYZ",100,"BUY","ACC-1234","NEW");
        trade.setPositionQuantity();
        int actual = trade.getPositionQuantity();
        int expect = 100;
        assertEquals(expect,actual);

    }
    @Test
    public void setPositionQuantityWhenBUY_AMEND() {
        Trade trade = new Trade(1234,1,"XYZ",100,"BUY","ACC-1234","AMEND");
        trade.setPositionQuantity();
        int actual = trade.getPositionQuantity();
        int expect = 100;
        assertEquals(expect,actual);

    }

    @Test
    public void setPositionQuantityWhenBUY_CANCEL() {
        Trade trade = new Trade(1234,1,"XYZ",0,"BUY","ACC-1234","CANCEL");
        trade.setPositionQuantity();
        int actual = trade.getPositionQuantity();
        int expect = 0;
        assertEquals(expect,actual);

    }

    @Test
    public void setPositionQuantityWhenSELL_NEW() {
        Trade trade = new Trade(1234,1,"XYZ",100,"SELL","ACC-1234","NEW");
        trade.setPositionQuantity();
        int actual = trade.getPositionQuantity();
        int expect = -100;
        assertEquals(expect,actual);

    }

    @Test
    public void setPositionQuantityWhenSELL_AMEND() {
        Trade trade = new Trade(1234,1,"XYZ",100,"SELL","ACC-1234","AMEND");
        trade.setPositionQuantity();
        int actual = trade.getPositionQuantity();
        int expect = -100;
        assertEquals(expect,actual);

    }

    @Test
    public void setPositionQuantityWhenSELL_CANCEL() {
        Trade trade = new Trade(1234,1,"XYZ",0,"SELL","ACC-1234","CANCEL");
        trade.setPositionQuantity();
        int actual = trade.getPositionQuantity();
        int expect = 0;
        assertEquals(expect,actual);

    }

}