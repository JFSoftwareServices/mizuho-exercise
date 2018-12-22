package com.mizuho.ui.controller;

import com.mizuho.service.InstrumentService;
import com.mizuho.shared.dto.InstrumentDto;
import com.mizuho.ui.model.response.InstrumentsRestResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class InstrumentControllerTest {

    @InjectMocks
    InstrumentPriceController instrumentPriceController;

    @Mock
    InstrumentService instrumentService;

    private List<InstrumentDto> instrumentDtoList;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        instrumentDtoList = new ArrayList<>();
        InstrumentDto instrumentDto = new InstrumentDto();
        instrumentDto.setDate(new Date());
        instrumentDto.setTicker("GOOG");
        instrumentDto.setPk(10001);
        instrumentDto.setPrice(new BigDecimal(979.84));
        instrumentDto.setVendor("CQG");
        instrumentDtoList.add(instrumentDto);

        instrumentDto = new InstrumentDto();
        instrumentDto.setDate(new Date());
        instrumentDto.setTicker("INTC");
        instrumentDto.setPk(10002);
        instrumentDto.setPrice(new BigDecimal(44.84));
        instrumentDto.setVendor("Bloomberg");
        instrumentDtoList.add(instrumentDto);
    }

    @Test
    void getInstrumentsPricesByInstrumentName() {
        when(instrumentService.findInstrumentPricesByTicker(anyString())).thenReturn(instrumentDtoList);

        InstrumentsRestResponse instrumentsRestResponse = instrumentPriceController.findInstrumentPricesByTicker("");

        assertNotNull(instrumentsRestResponse);
        assertEquals(2, instrumentsRestResponse.getResponseLines().size());

        assertEquals("GOOG", instrumentsRestResponse.getResponseLines().get(0).getTicker());
        assertEquals("CQG", instrumentsRestResponse.getResponseLines().get(0).getVendor());
        assertEquals(new BigDecimal(979.84), instrumentsRestResponse.getResponseLines().get(0).getPrice());

        assertEquals("INTC", instrumentsRestResponse.getResponseLines().get(1).getTicker());
        assertEquals("Bloomberg", instrumentsRestResponse.getResponseLines().get(1).getVendor());
        assertEquals(new BigDecimal(44.84), instrumentsRestResponse.getResponseLines().get(1).getPrice());
    }

    @Test
    void getAllPricesForVendor() {
        when(instrumentService.findInstrumentPricesByVendor(anyString())).thenReturn(instrumentDtoList);

        InstrumentsRestResponse instrumentsRestResponse = instrumentPriceController.findInstrumentPricesByVendor("");

        assertNotNull(instrumentsRestResponse);
        assertEquals(2, instrumentsRestResponse.getResponseLines().size());

        assertEquals("GOOG", instrumentsRestResponse.getResponseLines().get(0).getTicker());
        assertEquals("CQG", instrumentsRestResponse.getResponseLines().get(0).getVendor());
        assertEquals(new BigDecimal(979.84), instrumentsRestResponse.getResponseLines().get(0).getPrice());

        assertEquals("INTC", instrumentsRestResponse.getResponseLines().get(1).getTicker());
        assertEquals("Bloomberg", instrumentsRestResponse.getResponseLines().get(1).getVendor());
        assertEquals(new BigDecimal(44.84), instrumentsRestResponse.getResponseLines().get(1).getPrice());
    }
}