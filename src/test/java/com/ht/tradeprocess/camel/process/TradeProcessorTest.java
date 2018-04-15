package com.ht.tradeprocess.camel.process;


import com.ht.tradeprocess.entity.Report;
import com.ht.tradeprocess.entity.Trade;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;



import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TradeProcessorTest{
    private TradeProcessor tradeProcessor;
    @Before
    public void setup(){
        tradeProcessor = new TradeProcessor();
    }

    /**
     * when 1 record is invalid, ignore that record
     */
    @Test
    public void extractTradeListWhenOneRecordMissingInfo(){
        String in = "Trade ID,Version,Security Identifier,Trade Quantity,Trade Direction,Account,Operation"+
                "\n"+"1234,1,XYZ,100,BUY,NEW"+"\n"+"5678,2,QED,0,BUY,ACC-2345,CANCEL";
        List<Trade> actualTradeList = tradeProcessor.extractTradeList(in);
        List<Trade> expectTradeList = new ArrayList<>();
        Trade trade = new Trade(5678,2,"QED",0,"BUY","ACC-2345","CANCEL");
        expectTradeList.add(trade);

        if(actualTradeList.size() == expectTradeList.size()){
            int i = 0;
            for(Trade trade1:actualTradeList){
                assertTrue(compareTwoTrade(trade1,expectTradeList.get(i)));
                i++;
            }
        }else {
            fail();
        }
    }

    @Test
    public void extractTradeListWhenOneRecordWithTradeIDNotNumber(){
        String in = "Trade ID,Version,Security Identifier,Trade Quantity,Trade Direction,Account,Operation"+
                "\n"+"d1234,1,XYZ,100,BUY,ACC-1234,NEW"+"\n"+"5678,2,QED,0,BUY,ACC-2345,CANCEL";
        List<Trade> actualTradeList = tradeProcessor.extractTradeList(in);
        List<Trade> expectTradeList = new ArrayList<>();
        Trade trade = new Trade(5678,2,"QED",0,"BUY","ACC-2345","CANCEL");
        expectTradeList.add(trade);

        if(actualTradeList.size() == expectTradeList.size()){
            int i = 0;
            for(Trade trade1:actualTradeList){
                assertTrue(compareTwoTrade(trade1,expectTradeList.get(i)));
                i++;
            }
        }else {
            fail();
        }
    }

    @Test
    public void remainHighestVersionTradeWhenTradeVersionsNotInOrder(){

        List<Trade> input = new ArrayList<>();
        Trade trade1 = new Trade(1234,1,"XYZ",100,"BUY","ACC-1234","NEW");
        Trade trade2 = new Trade(1234,3,"XYZ",300,"BUY","ACC-1234","AMEND");
        Trade trade3 = new Trade(1234,2,"XYZ",400,"BUY","ACC-1234","AMEND");
        input.add(trade1);
        input.add(trade2);
        input.add(trade3);
        List<Trade> actualTradeList = tradeProcessor.remainHighestVersionTrade(input);

        List<Trade> expectTradeList = new ArrayList<>();
        Trade trade4 = new Trade(1234,1,"XYZ",0,"BUY","ACC-1234","NEW");
        Trade trade5 = new Trade(1234,3,"XYZ",300,"BUY","ACC-1234","AMEND");
        Trade trade6 = new Trade(1234,2,"XYZ",0,"BUY","ACC-1234","AMEND");
        expectTradeList.add(trade4);
        expectTradeList.add(trade5);
        expectTradeList.add(trade6);

        if(actualTradeList.size()==expectTradeList.size()){
            int i = 0;
            for(Trade actualTrade:actualTradeList){
                assertTrue(compareTwoTrade(actualTrade,expectTradeList.get(i)));
                i++;
            }
        }else {
            fail();
        }
    }

    @Test
    public void remainHighestVersionTradeWhenVersionInOrder(){
        List<Trade> input = new ArrayList<>();
        Trade trade1 = new Trade(1234,1,"XYZ",100,"BUY","ACC-1234","NEW");
        Trade trade2 = new Trade(1234,2,"XYZ",300,"BUY","ACC-1234","AMEND");
        Trade trade3 = new Trade(1234,3,"XYZ",400,"BUY","ACC-1234","AMEND");
        input.add(trade1);
        input.add(trade2);
        input.add(trade3);
        List<Trade> actualTradeList = tradeProcessor.remainHighestVersionTrade(input);

        List<Trade> expectTradeList = new ArrayList<>();
        Trade trade4 = new Trade(1234,1,"XYZ",0,"BUY","ACC-1234","NEW");
        Trade trade5 = new Trade(1234,2,"XYZ",0,"BUY","ACC-1234","AMEND");
        Trade trade6 = new Trade(1234,3,"XYZ",400,"BUY","ACC-1234","AMEND");
        expectTradeList.add(trade4);
        expectTradeList.add(trade5);
        expectTradeList.add(trade6);

        if(actualTradeList.size()==expectTradeList.size()){
            int i = 0;
            for(Trade actualTrade:actualTradeList){
                assertTrue(compareTwoTrade(actualTrade,expectTradeList.get(i)));
                i++;
            }
        }else {
            fail();
        }

    }


    @Test
    public void transferToReportListWhenMatchAggregateConditionAndTradeIdDifferent(){
        Trade trade1 = new Trade(2233,1,"RET",100,"SELL","ACC-3456","NEW");
        Trade trade2 = new Trade(2213,2,"RET",400,"SELL","ACC-3456","AMEND");

        List<Trade> actualInput = new ArrayList<>();
        actualInput.add(trade1);
        actualInput.add(trade2);
        List<Report> actualResults = tradeProcessor.transferToReportList(actualInput);

        List<Report> expectResults = new ArrayList<>();
        Report report1 = new Report("ACC-3456","RET",-500,"2233,2213");
        expectResults.add(report1);

        if(actualResults.size() == expectResults.size()){
            int i = 0;
            for(Report actualReport:actualResults){
                assertTrue(compareTwoReport(actualReport,expectResults.get(i)));
                i++;
            }
        }else {
            fail();
        }
    }

    @Test
    public void transferToReportListWhenMatchAggregateConditionAndTradeIdSame(){
        Trade trade1 = new Trade(2233,1,"RET",0,"SELL","ACC-3456","NEW");
        Trade trade2 = new Trade(2233,2,"RET",400,"SELL","ACC-3456","AMEND");

        List<Trade> actualInput = new ArrayList<>();
        actualInput.add(trade1);
        actualInput.add(trade2);
        List<Report> actualResults = tradeProcessor.transferToReportList(actualInput);

        List<Report> expectResults = new ArrayList<>();
        Report report1 = new Report("ACC-3456","RET",-400,"2233");
        expectResults.add(report1);

        if(actualResults.size() == expectResults.size()){
            int i = 0;
            for(Report actualReport:actualResults){
                assertTrue(compareTwoReport(actualReport,expectResults.get(i)));
                i++;
            }
        }else {
            fail();
        }
    }

    private boolean compareTwoReport(Report report1, Report report2){
        return StringUtils.equals(report1.getAccount(),report2.getAccount()) &&
                StringUtils.equals(report1.getInstrument(),report2.getInstrument()) &&
                StringUtils.equals(report1.getTrades(),report2.getTrades()) &&
                report1.getQuantity()==report2.getQuantity();
    }

    private boolean compareTwoTrade(Trade trade1, Trade trade2){
        return trade1.getTradeId() == trade2.getTradeId() &&
                trade1.getTradeVersion() == trade2.getTradeVersion() &&
                StringUtils.equals(trade1.getSecurityIdentifier(),trade2.getSecurityIdentifier()) &&
                trade1.getQuantity() == trade2.getQuantity() &&
                StringUtils.equals(trade1.getDirection(),trade2.getDirection()) &&
                StringUtils.equals(trade1.getAccountNumber(), trade2.getAccountNumber()) &&
                StringUtils.equals(trade1.getOperation(), trade2.getOperation());
    }
}