package com.ht.tradeprocess.service;


import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class VerificationUtilTest {

    @Test
    public void verifyNotEmptyWhenInputIsNull() {
        String input = null;
        boolean expect = false;
        boolean actual = VerificationUtil.verifyNotEmpty(input);
        assertEquals(expect,actual);
    }

    @Test
    public void verifyNotEmptyWhenInputIsEmpty() {
        String input = "";
        boolean expect = false;
        boolean actual = VerificationUtil.verifyNotEmpty(input);
        assertEquals(expect,actual);
    }

    @Test
    public void verifyNotEmptyWhenInputIsNotNullNotEmpty() {
        String input = "abc";
        boolean expect = true;
        boolean actual = VerificationUtil.verifyNotEmpty(input);
        assertEquals(expect,actual);
    }

    @Test
    public void verifyNumericalWhenInputIsEmpty() {
        String input = "";
        boolean expect = false;
        boolean actual = VerificationUtil.verifyNumerical(input);
        assertEquals(expect,actual);
    }

    @Test
    public void verifyNumericalWhenInputNotNumerical() {
        String input = "ssss";
        boolean expect = false;
        boolean actual = VerificationUtil.verifyNumerical(input);
        assertEquals(expect,actual);
    }


    @Test
    public void verifyNumericalWhenInputIsNumerical() {
        String input = "123";
        boolean expect = true;
        boolean actual = VerificationUtil.verifyNumerical(input);
        assertEquals(expect,actual);
    }

    @Test
    public void verifyDirectionWhenInputIsNull() {
        String input = null;
        boolean expect = false;
        boolean actual = VerificationUtil.verifyDirection(input);
        assertEquals(expect,actual);
    }

    @Test
    public void verifyDirectionWhenInputIsBUY() {
        String input = "BUY";
        boolean expect = true;
        boolean actual = VerificationUtil.verifyDirection(input);
        assertEquals(expect,actual);
    }

    @Test
    public void verifyDirectionWhenInputIsSELL() {
        String input = "SELL";
        boolean expect = true;
        boolean actual = VerificationUtil.verifyDirection(input);
        assertEquals(expect,actual);
    }

    @Test
    public void verifyDirectionWhenInputIsNotBUYNotSELL() {
        String input = "ASDF";
        boolean expect = false;
        boolean actual = VerificationUtil.verifyDirection(input);
        assertEquals(expect,actual);
    }


    @Test
    public void verifyOperationWhenInputIsEmpty() {
        String input = "";
        boolean expect = false;
        boolean actual = VerificationUtil.verifyOperation(input);
        assertEquals(expect,actual);
    }

    @Test
    public void verifyOperationWhenInputIsNEW() {
        String input = "NEW";
        boolean expect = true;
        boolean actual = VerificationUtil.verifyOperation(input);
        assertEquals(expect,actual);
    }

    @Test
    public void verifyOperationWhenInputIsAMEDN() {
        String input = "AMEND";
        boolean expect = true;
        boolean actual = VerificationUtil.verifyOperation(input);
        assertEquals(expect,actual);
    }

    @Test
    public void verifyOperationWhenInputIsCANCEL() {
        String input = "CANCEL";
        boolean expect = true;
        boolean actual = VerificationUtil.verifyOperation(input);
        assertEquals(expect,actual);
    }

    @Test
    public void verifyOperationWhenInputIsNotNEWNotAMEDNNotCANCEL() {
        String input = "SSSSS";
        boolean expect = false;
        boolean actual = VerificationUtil.verifyOperation(input);
        assertEquals(expect,actual);
    }
}