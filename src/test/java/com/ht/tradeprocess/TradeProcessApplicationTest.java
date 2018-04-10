package com.ht.tradeprocess;

import com.ht.tradeprocess.camel.process.TradeProcessorTest;
import com.ht.tradeprocess.entity.TradeTest;
import com.ht.tradeprocess.service.ReportGeneratorServiceTest;
import com.ht.tradeprocess.service.VerificationServiceTest;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ VerificationServiceTest.class, ReportGeneratorServiceTest.class,TradeTest.class,TradeProcessorTest.class })
public class TradeProcessApplicationTest {


}