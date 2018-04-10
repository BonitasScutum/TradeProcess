package com.ht.tradeprocess.camel.process;

import com.ht.tradeprocess.entity.Report;
import com.ht.tradeprocess.entity.Trade;
import com.ht.tradeprocess.service.ReportGeneratorService;
import com.ht.tradeprocess.service.VerificationService;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Component
public class TradeProcessor implements Processor{
    private static final Logger LOGGER = LoggerFactory.getLogger(TradeProcessor.class);
    private static ArrayList<Trade> output = new ArrayList<>();

    @Value("${route.outbound}")
    String outbound;

    @Override
    public void process(Exchange exchange) throws Exception {
        LOGGER.info("Start Process The File");
        String in = (String) exchange.getIn().getBody();
        if(VerificationService.verifyNotEmpty(in)){
            List<Trade> tradeList = extractTradeList(in);
            tradeList = remainHighestVersionTrade(tradeList);
            List<Report> reportList = transferToReportList(tradeList);
            File file = new File(outbound);
            ReportGeneratorService.exportCsv(file, reportList);

        }else {
            LOGGER.info("File is empty.");
        }
    }

    private String[] getRecords(String in){
        return in.split("\\r?\\n");
    }

    private String[] getTradeInfo(String record){
        return record.split(",");
    }

    private boolean verifyTrade(String[] trade){
        return trade!=null && trade.length==7 &&
                VerificationService.verifyNumerical(trade[0]) &&
                VerificationService.verifyNumerical(trade[1]) &&
                VerificationService.verifyNotEmpty(trade[2]) &&
                VerificationService.verifyNumerical(trade[3]) &&
                VerificationService.verifyDirection(trade[4]) &&
                VerificationService.verifyNotEmpty(trade[5]) &&
                VerificationService.verifyOperation(trade[6]);
    }

    private Trade convertToTrade(String[] tradeInfo){
        return new Trade(tradeInfo);
    }

    private boolean meetTradeClearCondition(Trade clearTrade, Trade originalTrade){
        return clearTrade.getTradeId()==originalTrade.getTradeId() &&
                clearTrade.getTradeVersion() < originalTrade.getTradeVersion();
    }

    public List<Trade> remainHighestVersionTrade(List<Trade> tradeList){
        List<Trade> highestVersionTradeList = tradeList;

        for(Trade expectTrade:highestVersionTradeList){
            for(Trade originalTrade:tradeList){
                if(meetTradeClearCondition(expectTrade,originalTrade)){
                        expectTrade.setPositionQuantity(0);
                        expectTrade.setQuantity(0);
                }
            }
        }
        return highestVersionTradeList;
    }

    private boolean meetAggregateCondition(Trade trade, Report report){
        return StringUtils.equals(trade.getAccountNumber(),report.getAccount()) &&
                StringUtils.equals(trade.getSecurityIdentifier(),report.getInstrument());
    }
    private int aggregatePositionQuantity(Trade trade,Report report){
        return report.getQuantity()+trade.getPositionQuantity();
    }
    private String aggregatePositionTrades(Trade trade,Report report){
        if(!StringUtils.contains(report.getTrades(),Integer.toString(trade.getTradeId()))){
            return report.getTrades()+","+Integer.toString(trade.getTradeId());
        }
        return report.getTrades();
    }
    private Report aggregate(Trade trade, Report report){
        report.setQuantity(aggregatePositionQuantity(trade,report));
        report.setTrades(aggregatePositionTrades(trade,report));
        return report;
    }

    public List<Report> transferToReportList(List<Trade> tradeList){
        List<Report> reportList = new ArrayList<>();

        reportList.add(new Report(tradeList.get(0)));

        if(tradeList.size()>1){
            for(int i = 1;i<tradeList.size();i++){
                boolean includeFlag = false;
                for(Report report:reportList){
                    if(meetAggregateCondition(tradeList.get(i),report)){
                        aggregate(tradeList.get(i),report);

                        includeFlag = true;
                    }
                }
                if(!includeFlag){
                    reportList.add(new Report(tradeList.get(i)));
                }
            }
        }
        return reportList;
    }

    public List<Trade> extractTradeList(String in){
        String[] records = getRecords(in);
        List<Trade> tradeList = new ArrayList<>();
        if(records.length>1){
            for(int i=1;i<records.length;i++){
                String[] tradeInfo = getTradeInfo(records[i]);
                if(verifyTrade(tradeInfo)){
                    Trade trade = convertToTrade(tradeInfo);
                    tradeList.add(trade);
                }
            }
        }
        return tradeList;
    }
}
