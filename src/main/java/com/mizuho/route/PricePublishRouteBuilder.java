package com.mizuho.route;

import com.mizuho.io.entity.InstrumentEntity;
import com.mizuho.service.InstrumentService;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class PricePublishRouteBuilder extends RouteBuilder {

    private final InstrumentService instrumentService;

    public PricePublishRouteBuilder(InstrumentService instrumentService) {
        this.instrumentService = instrumentService;
    }

    public void configure() {
        errorHandler(deadLetterChannel("{{deadLetter}}"));

        from("{{incomingInstruments}}")
                .id("{{incomingInstrumentsRouteId}}")
                .transacted()
                .log("In coming message from {{incomingInstruments}} queue is ${body}")
                .log("Unmarshalling instrument with body ${body}")
                .unmarshal().jacksonxml(InstrumentEntity.class)
                .log("Storing instrument with body ${body} to cache")
                .bean(instrumentService, "saveInstrument")
                .log("Marshalling instrument with body ${body} to cache")
                .marshal().jacksonxml(String.class)
                .log("Sending body ${body} to {{outPutPrices}} queue")
                .to("{{outPutPrices}}");
    }
}