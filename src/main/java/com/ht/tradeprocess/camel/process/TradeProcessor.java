package com.ht.tradeprocess.camel.process;

import com.ht.tradeprocess.entity.Report;
import com.ht.tradeprocess.entity.Trade;
import com.ht.tradeprocess.service.ReportGeneratorService;
import com.ht.tradeprocess.service.VerificationUtil;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        if(VerificationUtil.verifyNotEmpty(in)){
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
                VerificationUtil.verifyNumerical(trade[0]) &&
                VerificationUtil.verifyNumerical(trade[1]) &&
                VerificationUtil.verifyNotEmpty(trade[2]) &&
                VerificationUtil.verifyNumerical(trade[3]) &&
                VerificationUtil.verifyDirection(trade[4]) &&
                VerificationUtil.verifyNotEmpty(trade[5]) &&
                VerificationUtil.verifyOperation(trade[6]);
    }

    private Trade convertToTrade(String[] tradeInfo){
        return new Trade(tradeInfo);
    }


    public List<Trade> remainHighestVersionTrade(List<Trade> tradeList){
        Map<Integer, Trade> currentHighest = new HashMap<>();
        for(Trade trade: tradeList){
            if(!currentHighest.containsKey(trade.getTradeId()) ||
                    currentHighest.get(trade.getTradeId()).getTradeVersion()<trade.getTradeVersion()){

                Trade toBeReplaced = currentHighest.put(trade.getTradeId(),trade);
                if (toBeReplaced != null) {
                    LOGGER.info("replacing highest, clear the old one.");
                    toBeReplaced.setQuantity(0);
                    toBeReplaced.setPositionQuantity(0);
                }
            }else {//currentHighest trade version >= tradeList
                trade.setQuantity(0);
                trade.setPositionQuantity(0);
            }
        }

        return tradeList;
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

//        reportList.add(new Report(tradeList.get(0)));

        if(tradeList.size()>0){
            for(int i = 0;i<tradeList.size();i++){
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
