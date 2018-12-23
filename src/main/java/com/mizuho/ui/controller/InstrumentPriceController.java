package com.mizuho.ui.controller;

import com.mizuho.service.InstrumentService;
import com.mizuho.shared.dto.InstrumentDto;
import com.mizuho.ui.exceptions.InstrumentPricesNotFoundException;
import com.mizuho.ui.model.response.InstrumentResponseLine;
import com.mizuho.ui.model.response.InstrumentsRestResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
                . orElseThrow(() -> new InstrumentPricesNotFoundException(ticker));

        if (instruments.size() == 0){
            throw new InstrumentPricesNotFoundException(ticker);
        }

        return instrumentDtosToInstrumentrestResponse(instruments);
    }

    @GetMapping(value = "/vendor/prices", produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public InstrumentsRestResponse findInstrumentPricesByVendor(@RequestParam("v") String vendor) {
        List<InstrumentDto> instruments = instrumentService.findInstrumentPricesByVendor(vendor);

        return instrumentDtosToInstrumentrestResponse(instruments);
    }

    private InstrumentsRestResponse instrumentDtosToInstrumentrestResponse(List<InstrumentDto> instruments) {
        InstrumentsRestResponse response = new InstrumentsRestResponse();

        for (InstrumentDto instrument : instruments) {
            InstrumentResponseLine instrumentResponseLine = new InstrumentResponseLine(
                    instrument.getTicker(),
                    instrument.getVendor(),
                    instrument.getPrice());

            response.add(instrumentResponseLine);
        }
        return response;
    }
}