package com.mizuho.route;

import com.mizuho.service.InstrumentService;
import com.mizuho.shared.dto.InstrumentDto;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.NotifyBuilder;
import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The properties from main/resources will be loaded if no application.prop in test.
 * if there is an app.props in test then that test one will be used.....
 */
@RunWith(CamelSpringBootRunner.class)
@SpringBootTest()
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class InstrumentStorePublishRouteBuilderIT {

    @Autowired
    private CamelContext camelContext;

    @Autowired
    private InstrumentService instrumentService;

    private NotifyBuilder notify;

    @Test
    public void storingMessagingConsumedFromInputInstrumentQueue()  {
        camelContext.createProducerTemplate().sendBody("{{instruments.prices.in}}", "<instrument><price>234.5</price><ticker>GOOG</ticker><vendor>Bloomberg" +
                "</vendor><date>2012-08-21T13:21:58.000Z</date></instrument>");
        await().atMost(10, SECONDS).until(this::cacheSize, is(1));
    }

    private int cacheSize() {
        return instrumentService.findInstrumentPricesByTicker("GOOG").orElseThrow(() -> new RuntimeException("")).size();
    }

    @Test
    public void instrumentPricesArePublishedToTopic()  {

        notify = new NotifyBuilder(camelContext).whenDone(1).create();
        camelContext.createProducerTemplate().sendBody("{{instruments.prices.in}}", "<instrument><price>234.5</price><ticker>GOOG</ticker><vendor>Bloomberg" +
                "</vendor><date>2012-08-21T13:21:58.000Z</date></instrument>");

        boolean matches = notify.matches(20, SECONDS);
        assertTrue(matches);

        InstrumentDto actualInstrumentDto = camelContext.createConsumerTemplate().receiveBody("{{instruments.prices.out}}", 5000, InstrumentDto.class);

        assertEquals(new BigDecimal(234.5), actualInstrumentDto.getPrice());
        assertEquals("GOOG", actualInstrumentDto.getTicker());
        assertEquals("Bloomberg", actualInstrumentDto.getVendor());
        assertEquals("Tue Aug 21 14:21:58 BST 2012", actualInstrumentDto.getDate().toString());
    }
}