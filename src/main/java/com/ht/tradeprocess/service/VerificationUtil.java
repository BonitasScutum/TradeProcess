package com.ht.tradeprocess.service;

import org.apache.commons.lang3.StringUtils;

public class VerificationUtil {
    private static final String DIRECTION_BUY = "BUY";
    private static final String DIRECTION_SELL = "SELL";
    private static final String OPERATION_NEW = "NEW";
    private static final String OPERATION_AMEND = "AMEND";
    private static final String OPERATION_CANCEL = "CANCEL";


    public static boolean verifyNotEmpty(String tradeInfo){
        return (tradeInfo!=null && !tradeInfo.isEmpty());
    }

    public static boolean verifyNumerical(String tradeInfo){
        if(verifyNotEmpty(tradeInfo)){
            try{
                return Integer.parseInt(tradeInfo) >= 0;
            }catch (NumberFormatException e){
                return false;
            }
        }
        return false;
    }

    public static boolean verifyDirection(String direction){
        return verifyNotEmpty(direction) && (StringUtils.equals(DIRECTION_BUY,direction) || StringUtils.equals(DIRECTION_SELL,direction));
    }

    public static boolean verifyOperation(String operation){
        return verifyNotEmpty(operation) && (StringUtils.equals(OPERATION_NEW,operation) || StringUtils.equals(OPERATION_AMEND,operation) || StringUtils.equals(OPERATION_CANCEL,operation));
    }
}
