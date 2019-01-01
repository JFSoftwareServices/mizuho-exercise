package com.mizuho.ui.controller;

import com.mizuho.service.InstrumentService;
import com.mizuho.shared.dto.InstrumentDto;
import com.mizuho.ui.exceptions.InstrumentPricesNotFoundException;
import com.mizuho.ui.model.response.InstrumentResponseLine;
import com.mizuho.ui.model.response.InstrumentsRestResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

@RestController
@RequestMapping("/api")
public class InstrumentPriceController {

    private final InstrumentService instrumentService;

    public InstrumentPriceController(InstrumentService instrumentService) {
        this.instrumentService = instrumentService;
    }

    @GetMapping(path = "/instrument/prices", produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public InstrumentsRestResponse findInstrumentPricesByTicker(@RequestParam("q") String ticker) {
        List<InstrumentDto> instruments = instrumentService.findInstrumentPricesByTicker(ticker)
                .orElse(new ArrayList<>());

        if (instruments.size() == 0) {
            throw new InstrumentPricesNotFoundException(ticker);
        }

        return instrumentDtosToInstrumentRestResponse(instruments);
    }

    @GetMapping(value = "/vendor/prices", produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public InstrumentsRestResponse findInstrumentPricesByVendor(@RequestParam("v") String vendor) {
        List<InstrumentDto> instruments = instrumentService.findInstrumentPricesByVendor(vendor)
                .orElse(new ArrayList<>());

        if (instruments.size() == 0) {
            throw new InstrumentPricesNotFoundException(vendor);
        }

        return instrumentDtosToInstrumentRestResponse(instruments);
    }

    private InstrumentsRestResponse instrumentDtosToInstrumentRestResponse(List<InstrumentDto> instruments) {
        InstrumentsRestResponse response = new InstrumentsRestResponse();

        instruments.stream().map(i -> new InstrumentResponseLine(
                i.getTicker(), i.getVendor(), i.getPrice())).forEach(response::add);
        return response;
    }
}