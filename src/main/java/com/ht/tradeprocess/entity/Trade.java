package com.ht.tradeprocess.entity;

import org.apache.commons.lang3.StringUtils;


public class Trade {
    private static final String DIRECTION_BUY = "BUY";
    private static final String DIRECTION_SELL = "SELL";
    private static final String OPERATION_NEW = "NEW";
    private static final String OPERATION_AMEND = "AMEND";
    private static final String OPERATION_CANCEL = "CANCEL";

    private int tradeId;
    private int tradeVersion;
    private String securityIdentifier;
    private int quantity;
    private String direction;
    private String accountNumber;
    private String operation;


    private int positionQuantity;

    public Trade(int tradeId, int tradeVersion, String securityIdentifier, int quantity, String direction, String accountNumber, String operation) {
        this.tradeId = tradeId;
        this.tradeVersion = tradeVersion;
        this.securityIdentifier = securityIdentifier;
        this.quantity = quantity;
        this.direction = direction;
        this.accountNumber = accountNumber;
        this.operation = operation;
        setPositionQuantity();

    }

    public Trade(String[] tradeInfo){
        this.tradeId = Integer.parseInt(tradeInfo[0]);
        this.tradeVersion = Integer.parseInt(tradeInfo[1]);
        this.securityIdentifier = tradeInfo[2];
        this.quantity = Integer.parseInt(tradeInfo[3]);
        this.direction = tradeInfo[4];
        this.accountNumber = tradeInfo[5];
        this.operation = tradeInfo[6];
        setPositionQuantity();
    }

    public int getTradeId() {
        return tradeId;
    }

    public void setTradeId(int tradeId) {
        this.tradeId = tradeId;
    }

    public int getTradeVersion() {
        return tradeVersion;
    }

    public void setTradeVersion(int tradeVersion) {
        this.tradeVersion = tradeVersion;
    }

    public String getSecurityIdentifier() {
        return securityIdentifier;
    }

    public void setSecurityIdentifier(String securityIdentifier) {
        this.securityIdentifier = securityIdentifier;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public int getPositionQuantity() {
        return positionQuantity;
    }

    public void setPositionQuantity(int positionQuantity) {
        this.positionQuantity = positionQuantity;
    }
    public void setPositionQuantity() {
        switch (direction){
            case DIRECTION_BUY:
                if(StringUtils.equals(OPERATION_NEW,operation) || StringUtils.equals(OPERATION_AMEND,operation)){
                    this.positionQuantity = this.quantity;
                }else {
                    this.positionQuantity = -this.quantity;
                }
                break;
            case DIRECTION_SELL:
                if(StringUtils.equals(OPERATION_CANCEL,operation)){
                    this.positionQuantity = this.quantity;
                }else{
                    this.positionQuantity = -this.quantity;
                }
                break;
        }
    }
}
