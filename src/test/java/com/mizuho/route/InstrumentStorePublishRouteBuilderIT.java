package com.mizuho.route;

import com.mizuho.service.InstrumentService;
import com.mizuho.shared.dto.InstrumentDto;
import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;

/**
 * The properties from main/resources will be loaded if no application.prop in test.
 * if there is an app.props in test then that test one will be used.....
 */
@RunWith(CamelSpringBootRunner.class)
@SpringBootTest()
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class InstrumentStorePublishRouteBuilderIT {

    @Autowired
    private Environment env;

    @Autowired
    private CamelContext camelContext;

    @Autowired
    private InstrumentService instrumentService;

    @EndpointInject(uri = "mock:result")
    private MockEndpoint mockEndpoint;

    @Before
    public void prepareForTest() throws Exception {
        camelContext.getRouteDefinitions().get(0)
                .adviceWith(camelContext, new AdviceWithRouteBuilder() {
                    @Override
                    public void configure() {
                        weaveAddLast().to(mockEndpoint);
                    }
                });
    }

    @Test
    public void storingMessagingConsumedFromInputInstrumentQueue() {
        camelContext.createProducerTemplate().sendBody("{{instruments.prices.in}}",
                "<instrument><price>234.5</price><ticker>GOOG</ticker><vendor>Bloomberg" +
                        "</vendor><date>2012-08-21T13:21:58.000Z</date></instrument>");
        await().atMost(10, SECONDS).until(this::cacheSize, is(1));
    }

    private int cacheSize() {
        return instrumentService.findInstrumentPricesByTicker("GOOG").orElseThrow(() -> new RuntimeException("")).size();
    }

    @Test
    public void instrumentPricesArePublishedToTopic() throws Exception {
        mockEndpoint.expectedMessageCount(1);
        mockEndpoint.expectedMessageCount(1);
        camelContext.createProducerTemplate().sendBody("{{instruments.prices.in}}",
                "<instrument><price>234.5</price><ticker>GOOG</ticker><vendor>Bloomberg" +
                        "</vendor><date>2012-08-21T13:21:58.000Z</date></instrument>");
        mockEndpoint.assertIsSatisfied();
        Exchange exchange = mockEndpoint.getExchanges().get(0);
        InstrumentDto instrumentDto = exchange.getIn().getBody(InstrumentDto.class);
        assertEquals(new BigDecimal("234.5"), instrumentDto.getPrice());
        assertEquals("GOOG", instrumentDto.getTicker());
        assertEquals("Bloomberg", instrumentDto.getVendor());
        assertEquals("Tue Aug 21 14:21:58 BST 2012", instrumentDto.getDate().toString());
    }
}