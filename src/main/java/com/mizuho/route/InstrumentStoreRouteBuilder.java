package com.mizuho.route;

import com.mizuho.io.entity.InstrumentEntity;
import com.mizuho.service.InstrumentService;
import com.mizuho.shared.dto.InstrumentDto;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;


@Component
public class InstrumentStoreRouteBuilder extends RouteBuilder {

    private final InstrumentService instrumentService;

    public InstrumentStoreRouteBuilder(InstrumentService instrumentService) {
        this.instrumentService = instrumentService;
    }

    public void configure() {
        errorHandler(deadLetterChannel("{{deadLetter}}"));

        from("{{instrumentStoreRouteUpstreamEndPoint}}")
                .id("{{instrumentStoreRouteId}}")
                .log("The id of the instrument store route is: {{instrumentStoreRouteId}}")
                .transacted()
                .log("In coming message from {{instrumentStoreRouteUpstreamEndPoint}} queue is ${body}")
                .log("Unmarshalling instrument with body ${body}")
                .unmarshal().jacksonxml(InstrumentEntity.class)
                .log("Unmarshaled instrument body is ${body}")
                .log("Storing instrument with body ${body} to cache")
                .bean(instrumentService, "saveInstrument")
                .log("Instrument stored");


//                .log("Marshalling instrument with body ${body} to cache")
//                .marshal().jacksonxml(String.class)
//                .log("Sending body ${body} to {{outPutPrices}} queue")
//                .to("{{outPutPrices}}");
    }
}