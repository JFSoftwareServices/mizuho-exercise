package com.mizuho.route;

import com.mizuho.service.InstrumentService;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class StalePriceEvictionRouteBuilder extends RouteBuilder {

    final
    InstrumentService instrumentService;

    public StalePriceEvictionRouteBuilder(InstrumentService instrumentService) {
        this.instrumentService = instrumentService;
    }

    public void configure() {
        errorHandler(deadLetterChannel("jms:queue:dead"));

        from("{{priceEvictionTimer}}")
                .id("{{stalePriceEvictionRouteId}}")
                .log("Price eviction commencing")
                .bean(instrumentService, "evictStaleInstrumentsOlderThanDays('{{daysAgo}}')")
                .log("Price eviction finished");
    }
}