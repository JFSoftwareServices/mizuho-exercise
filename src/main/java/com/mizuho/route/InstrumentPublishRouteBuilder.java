package com.mizuho.route;

import com.mizuho.service.InstrumentService;
import com.mizuho.service.impl.ResetExchangeBodyService;
import com.mizuho.shared.dto.InstrumentDto;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;


@Component
public class InstrumentPublishRouteBuilder extends RouteBuilder {

    private final InstrumentService instrumentService;
    private final ResetExchangeBodyService resetExchangeBodyService;

    public InstrumentPublishRouteBuilder(InstrumentService instrumentService, ResetExchangeBodyService resetExchangeBodyService) {
        this.instrumentService = instrumentService;
        this.resetExchangeBodyService = resetExchangeBodyService;
    }

    public void configure() {
        errorHandler(deadLetterChannel("{{dead.letter}}"));

        from("{{instruments.prices.in}}")
                .transacted()
                .log("In coming message from {{instruments.prices.in}} is ${body}")
                .log("Unmarshalling incoming message to InstrumentDto")
                .unmarshal().jacksonxml(InstrumentDto.class)
                .log("Unmarshalled message is ${body}")
                .log("Storing InstrumentDto to cache")
                .bean(instrumentService, "saveInstrument")
                .log("InstrumentDto stored")
                .log("Resetting exchange body to latest cached instrument")
                .bean(resetExchangeBodyService, "setBodyToLatestCachedInstrument")
                .log("Reset exchange body ${body}")
                .log("Sending exchange body to {{instruments.prices.out}}")
                .to("{{instruments.prices.out}}");
    }
}