package com.mizuho.service;

import com.mizuho.io.dao.InstrumentDao;
import com.mizuho.io.entity.InstrumentEntity;
import com.mizuho.service.impl.InstrumentServiceImpl;
import com.mizuho.shared.dto.InstrumentDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class InstrumentServiceTest {

    @InjectMocks
    InstrumentServiceImpl instrumentService;

    @Mock
    InstrumentDao dao;

    private List<InstrumentEntity> entities;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        entities = new ArrayList<>();
        InstrumentEntity instrumentEntity = new InstrumentEntity(
                new BigDecimal("979.84"),"GOOG","CQG", new Date() );
        entities.add(instrumentEntity);

        instrumentEntity = new InstrumentEntity(
                new BigDecimal("44.84"),"INTC", "Bloomberg", new Date() );
        entities.add(instrumentEntity);
    }

    @Test
    void findInstrumentPricesByInstrumentName() {
        when(dao.findByTicker((anyString()))).thenReturn(Optional.of(entities));

        List<InstrumentDto> instrumentRestResponse = instrumentService.findInstrumentPricesByTicker("").orElse(null);

        assertNotNull(instrumentRestResponse);
        assertEquals(2, instrumentRestResponse.size());

        assertEquals("GOOG", instrumentRestResponse.get(0).getTicker());
        assertEquals( "CQG", instrumentRestResponse.get(0).getVendor());
        assertEquals(new BigDecimal("979.84"), instrumentRestResponse.get(0).getPrice());

        assertEquals("INTC", instrumentRestResponse.get(1).getTicker());
        assertEquals("Bloomberg", instrumentRestResponse.get(1).getVendor());
        assertEquals(new BigDecimal("44.84"), instrumentRestResponse.get(1).getPrice());
    }

    @Test
    void findInstrumentsByVendor() {
        when(dao.findByVendor((anyString()))).thenReturn(Optional.of(entities));

        List<InstrumentDto> instrumentRestResponse = instrumentService.findInstrumentPricesByVendor("").orElse(null);

        assertNotNull(instrumentRestResponse);
        assertEquals(2, instrumentRestResponse.size());

        assertEquals("GOOG", instrumentRestResponse.get(0).getTicker());
        assertEquals("CQG", instrumentRestResponse.get(0).getVendor());
        assertEquals(new BigDecimal("979.84"), instrumentRestResponse.get(0).getPrice());

        assertEquals("INTC", instrumentRestResponse.get(1).getTicker());
        assertEquals("Bloomberg", instrumentRestResponse.get(1).getVendor());
        assertEquals(new BigDecimal("44.84"), instrumentRestResponse.get(1).getPrice());
    }
}