package com.ht.tradeprocess.camel.router;

import com.ht.tradeprocess.camel.process.TradeProcessor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TradeRoute extends RouteBuilder {

    @Value("${route.inbound}")
    String inbound;

    @Autowired
    TradeProcessor tradeProcessor;

    @Override
    public void configure() throws Exception {
        from(inbound)
                .convertBodyTo(String.class)
                .process(tradeProcessor)
                .log("File processed successfully");
    }
}
