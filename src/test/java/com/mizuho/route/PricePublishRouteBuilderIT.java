package com.mizuho.route;

import org.apache.camel.*;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.builder.NotifyBuilder;
import org.apache.camel.spi.BrowsableEndpoint;
import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.xmlunit.builder.Input;
import org.xmlunit.xpath.JAXPXPathEngine;

import javax.xml.transform.Source;
import java.util.List;

import static java.util.concurrent.TimeUnit.SECONDS;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

@RunWith(CamelSpringBootRunner.class)
@SpringBootTest()
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class PricePublishRouteBuilderIT {

    @Value("${incomingInstrumentsRouteId}")
    private String incomingInstrumentsRouteId;

    @Value("${stalePriceEvictionRouteId}")
    private String priceEvictionRouteId;

    @Produce(uri = "seda:incomingInstruments")
    private ProducerTemplate producerTemplate;

    @EndpointInject(uri = "{{outPutPrices}}")
    private Endpoint outPutPrices;

    @EndpointInject(uri = "seda:outPutPrices")
    private Endpoint sedaOutPutPrices;

    @EndpointInject(uri = "seda:deadLetter")
    private Endpoint sedaDeadLetter;

    @EndpointInject(uri = "{{deadLetter}}")
    private Endpoint deadLetter;

    @Autowired
    private CamelContext camelContext;

    private NotifyBuilder notify;

    @Before
    public void stopPriceEvictionRoute() {
        camelContext.getRouteDefinition(priceEvictionRouteId).stop();
    }

    @Test
    public void subscribersShouldBeAbleToConsumeFromOutputTopic() throws Exception {
        configureRouteMockOutPutTopic();

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
        assertEquals(xpath.evaluate("//InstrumentEntity/vendor", xml), "Fidessa");
    }

    private void configureRouteMockOutPutTopic() throws Exception {
        camelContext.getRouteDefinition(incomingInstrumentsRouteId)
                .adviceWith(camelContext, new AdviceWithRouteBuilder() {
                    @Override
                    public void configure() {
                        replaceFromWith(producerTemplate.getDefaultEndpoint());
                        interceptSendToEndpoint(outPutPrices.getEndpointUri())
                                .skipSendToOriginalEndpoint()
                                .to(sedaOutPutPrices.getEndpointUri());
                    }
                });
    }
}