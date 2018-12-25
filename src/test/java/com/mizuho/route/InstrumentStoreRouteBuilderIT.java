package com.mizuho.route;

import com.mizuho.io.entity.InstrumentEntity;
import com.mizuho.service.InstrumentService;
import com.mizuho.shared.dto.InstrumentDto;
import org.apache.camel.*;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.builder.NotifyBuilder;
import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * The properties from main/resources will be loaded if no application.prop in test.
 * if there is an app.props in test then that test one will be used.....
 */
@RunWith(CamelSpringBootRunner.class)
@SpringBootTest()
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class InstrumentStoreRouteBuilderIT {

    //    @Value("${incomingInstrumentsRouteId}")
//    private String incomingInstrumentsRouteId;
//
//    @Value("${stalePriceEvictionRouteId}")
//    private String priceEvictionRouteId;
//
    @Produce(uri = "{{instrumentStoreRouteUpstreamEndPoint}}")
    private ProducerTemplate producerTemplate;
//
//    @EndpointInject(uri = "{{outPutPrices}}")
//    private Endpoint outPutPrices;
//
//    @EndpointInject(uri = "seda:outPutPrices")
//    private Endpoint sedaOutPutPrices;
//
//    @EndpointInject(uri = "seda:deadLetter")
//    private Endpoint sedaDeadLetter;
//
//    @EndpointInject(uri = "{{deadLetter}}")
//    private Endpoint deadLetter;

    @Autowired
    private CamelContext camelContext;

    @Autowired
    private InstrumentService instrumentService;

    private NotifyBuilder notify;

    @Before
    public void stopPriceEvictionRoute() {
//        camelContext.getRouteDefinition(priceEvictionRouteId).stop();
    }

    @Test
    public void storingMessagingConsumedFromInputInstrumentQueue() throws Exception {
        producerTemplate.sendBody("<instrument><price>234.5</price><ticker>GOOG</ticker><vendor>Bloomberg" +
                "</vendor><date>2012-08-21T13:21:58.000Z</date></instrument>");
        await().atMost(10, SECONDS).until(this::cacheSize, is(1));
    }

    private int cacheSize(){
        return instrumentService.findInstrumentPricesByTicker("GOOG").orElseThrow(() -> new RuntimeException("")).size();
    }

    @Test
    public void subscribersShouldBeAbleToConsumeFromOutputTopic() throws Exception {

//<instrument><price>234.5</price><ticker>GOOG</ticker><vendor>Bloomberg</vendor><date>2012-08-21T13:21:58.000Z</date></instrument>
        /*configureRouteMockOutPutTopic();

        notify = new NotifyBuilder(camelContext).whenDone(2).create();

        String vendorMessage1 = "<InstrumentEntity><price>174.72</price><figi>BBG009BHXGK6</figi><vendor>Fidessa</vendor><date>2018-12-08T00:00:00.000+0000</date></InstrumentEntity>";
        String vendorMessage2 = "<InstrumentEntity><price>48.37</price><figi>BBG0012BMV07</figi><vendor>Fidessa</vendor><date>2018-12-07T00:00:00.000+0000</date></InstrumentEntity>";

        producerTemplate.sendBody(vendorMessage1);
        producerTemplate.sendBody(vendorMessage2);

        boolean matches = notify.matches(20, SECONDS);
        assertTrue(matches);

        BrowsableEndpoint browsableEndpoint = camelContext.getEndpoint(sedaOutPutPrices.getEndpointUri(), BrowsableEndpoint.class);

        List<Exchange> list = browsableEndpoint.getExchanges();
        assertEquals(2, list.size());

        Source xml = Input.fromString(list.get(0).getIn().getBody(String.class)).build();
        JAXPXPathEngine xpath = new JAXPXPathEngine();

        assertEquals(xpath.evaluate("//InstrumentEntity/price", xml), "174.72");
        assertEquals(xpath.evaluate("//InstrumentEntity/figi", xml), "BBG009BHXGK6");
        assertEquals(xpath.evaluate("//InstrumentEntity/vendor", xml), "Fidessa");

        xml = Input.fromString(list.get(1).getIn().getBody(String.class)).build();
        assertEquals(xpath.evaluate("//InstrumentEntity/price", xml), "48.37");
        assertEquals(xpath.evaluate("//InstrumentEntity/figi", xml), "BBG0012BMV07");
        assertEquals(xpath.evaluate("//InstrumentEntity/vendor", xml), "Fidessa");*/
    }

//    private void configureRouteMockOutPutTopic() throws Exception {
//        camelContext.getRouteDefinition(incomingInstrumentsRouteId)
//                .adviceWith(camelContext, new AdviceWithRouteBuilder() {
//                    @Override
//                    public void configure() {
//                        replaceFromWith(producerTemplate.getDefaultEndpoint());
//                        interceptSendToEndpoint(outPutPrices.getEndpointUri())
//                                .skipSendToOriginalEndpoint()
//                                .to(sedaOutPutPrices.getEndpointUri());
//                    }
//                });
//    }
}