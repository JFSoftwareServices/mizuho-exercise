package com.mizuho.route;

import com.mizuho.service.InstrumentService;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.NotifyBuilder;
import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.core.Is.is;
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
    public void storingMessagingConsumedFromInputInstrumentQueue() throws Exception {
        camelContext.createProducerTemplate().sendBody("{{instruments.prices.in}}", "<instrument><price>234.5</price><ticker>GOOG</ticker><vendor>Bloomberg" +
                "</vendor><date>2012-08-21T13:21:58.000Z</date></instrument>");
        await().atMost(10, SECONDS).until(this::cacheSize, is(1));
    }

    private int cacheSize() {
        return instrumentService.findInstrumentPricesByTicker("GOOG").orElseThrow(() -> new RuntimeException("")).size();
    }

    @Test
    public void instrumentPricesArePublishedToTopic() throws Exception {

        notify = new NotifyBuilder(camelContext).whenDone(1).create();
        camelContext.createProducerTemplate().sendBody("{{instruments.prices.in}}", "<instrument><price>234.5</price><ticker>GOOG</ticker><vendor>Bloomberg" +
                "</vendor><date>2012-08-21T13:21:58.000Z</date></instrument>");

        boolean matches = notify.matches(20, SECONDS);
        assertTrue(matches);


        /*Endpoint endpoint = camelContext.getEndpoint("{{instruments.prices.out}}");
        PollingConsumer consumer = endpoint.createPollingConsumer();
        Exchange exchange = consumer.receive();
        exchange.getIn().getBody();*/

//        String body = camelContext.createConsumerTemplate().receiveBody("{{instruments.prices.out}}", 5000, String.class);

        //TODO complete test
//        Assert.assertNotNull(body);
//        Assert.assertEquals("Hey2", ex2.getIn().getBody());

//        Source xml = Input.fromString(list.get(0).getIn().getBody(String.class)).build();
//        JAXPXPathEngine xpath = new JAXPXPathEngine();
//
//        assertEquals(xpath.evaluate("//InstrumentEntity/price", xml), "174.72");
//        assertEquals(xpath.evaluate("//InstrumentEntity/figi", xml), "BBG009BHXGK6");
//        assertEquals(xpath.evaluate("//InstrumentEntity/vendor", xml), "Fidessa");
    }
}