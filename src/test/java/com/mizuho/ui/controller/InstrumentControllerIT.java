package com.mizuho.ui.controller;

import com.mizuho.PriceApplication;
import com.mizuho.io.entity.InstrumentEntity;
import com.mizuho.service.InstrumentService;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;

import static org.junit.Assert.assertEquals;

@SpringBootTest(classes = PriceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class InstrumentControllerIT {

    @Autowired
    private InstrumentService instrumentService;

    @LocalServerPort
    private int port;

    private TestRestTemplate restTemplate = new TestRestTemplate();

    private HttpHeaders headers = new HttpHeaders();

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }

    @BeforeEach
    void setUp() {
        InstrumentEntity firstInstrument = new InstrumentEntity();
        firstInstrument.setTicker("GOOG");
        firstInstrument.setDate(new Date());
        firstInstrument.setVendor("CQG");
        firstInstrument.setPrice(new BigDecimal(979.84));
        instrumentService.saveInstrument(firstInstrument);

        InstrumentEntity secondInstrument = new InstrumentEntity();
        secondInstrument.setTicker("APPL");
        secondInstrument.setDate(new Date());
        secondInstrument.setVendor("CQG");
        secondInstrument.setPrice(new BigDecimal(150.73));
        instrumentService.saveInstrument(secondInstrument);

        InstrumentEntity thirdInstrument = new InstrumentEntity();
        thirdInstrument.setTicker("GOOG");
        thirdInstrument.setDate(new Date());
        thirdInstrument.setVendor("CQG");
        thirdInstrument.setPrice(new BigDecimal(979.84));
        instrumentService.saveInstrument(thirdInstrument);

        InstrumentEntity fourthInstrument = new InstrumentEntity();
        fourthInstrument.setTicker("GOOG");
        fourthInstrument.setDate(new Date());
        fourthInstrument.setVendor("CQG");
        fourthInstrument.setPrice(new BigDecimal(979.84));
        instrumentService.saveInstrument(fourthInstrument);
    }

    @Test
    void findInstrumentPricesForVendors_json() throws JSONException {
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<String> entity = new HttpEntity<String>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("api/vendor/prices?v=CQG"),
                HttpMethod.GET, entity, String.class);

        String expectedBody = "{instrument:[{ticker:GOOG,vendor:CQG,price:979.84},{ticker:APPL,vendor:CQG,price:150.73},{ticker:GOOG,vendor:CQG,price:979.84},{ticker:GOOG,vendor:CQG,price:979.84}]}";
        JSONAssert.assertEquals(expectedBody, response.getBody(), false);
    }

    @Test
    void findInstrumentPricesForVendors_xml() throws JSONException {
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_XML));

        HttpEntity<String> entity = new HttpEntity<String>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("api/vendor/prices?v=CQG"),
                HttpMethod.GET, entity, String.class);

        String expectedBody = "<instruments><instrument><ticker>GOOG</ticker><vendor>CQG</vendor><price>979.84</price></instrument><instrument><ticker>APPL</ticker><vendor>CQG</vendor><price>150.73</price></instrument><instrument><ticker>GOOG</ticker><vendor>CQG</vendor><price>979.84</price></instrument><instrument><ticker>GOOG</ticker><vendor>CQG</vendor><price>979.84</price></instrument></instruments>";
        assertEquals(expectedBody, response.getBody());
    }

    @Test
    void findAllInstrumentPrices_json() throws JSONException {
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<String> entity = new HttpEntity<String>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("api/instrument/prices?q=GOOG"),
                HttpMethod.GET, entity, String.class);

        String expected = "{instrument:[{ticker:GOOG,vendor:CQG,price:979.84},{ticker:GOOG,vendor:CQG,price:979.84},{ticker:GOOG,vendor:CQG,price:979.84}]}";
        JSONAssert.assertEquals(expected, response.getBody(), false);
    }

    @Test
    void findAllInstrumentPricesForVendors_xml() throws JSONException {
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_XML));

        HttpEntity<String> entity = new HttpEntity<String>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("api/instrument/prices?q=APPL"),
                HttpMethod.GET, entity, String.class);

        String expectedBody = "<instruments><instrument><ticker>APPL</ticker><vendor>CQG</vendor><price>150.73</price></instrument></instruments>";
        assertEquals(expectedBody, response.getBody());
    }

    @Test
    void instrumentNotFound() {
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("api/instrument/prices?q=VOD.L"),
                HttpMethod.GET, entity, String.class);


        String expectedBody = "Could not find prices for instrument VOD.L";
        assertEquals(expectedBody, response.getBody());

        HttpStatus expectedStatusCode = HttpStatus.NOT_FOUND;
        assertEquals(expectedStatusCode, response.getStatusCode());
    }
}