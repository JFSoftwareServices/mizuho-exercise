package com.mizuho.route;

import com.mizuho.service.InstrumentService;
import com.mizuho.shared.dto.InstrumentDto;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;


@Component
public class InstrumentPublishRouteBuilder extends RouteBuilder {

    private final InstrumentService instrumentService;

    public InstrumentPublishRouteBuilder(InstrumentService instrumentService) {
        this.instrumentService = instrumentService;
    }

    public void configure() {
        errorHandler(deadLetterChannel("{{dead.letter}}"));

        from("{{instruments.prices.in}}")
                .transacted()
                .id("instrumentStoreRouteBuilder")
                .log("In coming message from {{instruments.prices.in}} is ${body}")
                .log("Unmarshalling instrument with body ${body}")
                .unmarshal().jacksonxml(InstrumentDto.class)
                .log("Storing instrument with body ${body} to cache")
                .bean(instrumentService, "saveInstrument")
                .log("Instrument stored")
//                .log("Marshalling instrument with body ${body}")
//                .marshal().jacksonxml(true)
                .log("Sending body ${body} to {{instruments.prices.out}}")
                .to("{{instruments.prices.out}}")
                .log("Sent body ${body} to {{instruments.prices.out}}");
    }
}