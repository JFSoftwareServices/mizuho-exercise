package com.mizuho.io.entity;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class InstrumentEntityTest {

    @Test
    void twoInstrumentsWithTheSamePriceAndTickerAndVendorAndDifferentDate() {
        DateTimeZone timeZone = DateTimeZone.forID("Europe/Paris");
        DateTime dateTime = new DateTime(new Date(), timeZone);
        DateTime yesterday = dateTime.minusDays(1);

        InstrumentEntity goog = new InstrumentEntity(new BigDecimal(979.84), "GOOG", "CQG", new Date());
        InstrumentEntity goog_1 = new InstrumentEntity(new BigDecimal(979.84), "GOOG", "CQG", yesterday.toDate());

        assertEquals(goog, goog_1);
    }

    @Test
    void twoInstrumentsWithDifferentVendorSamePriceAndTicker() {
        InstrumentEntity goog = new InstrumentEntity(new BigDecimal(979.84), "GOOG", "CQG", null);
        InstrumentEntity goog_1 = new InstrumentEntity(new BigDecimal(979.84), "GOOG", "Bloomberg", null);

        assertNotEquals(goog, goog_1);
    }

    @Test
    void twoInstrumentsWithDifferentTickerSamePriceAndVendor() {
        InstrumentEntity vod = new InstrumentEntity(new BigDecimal(156.74), "VOD", "Reuters", null);
        InstrumentEntity goog = new InstrumentEntity(new BigDecimal(979.84), "GOOG", "Bloomberg", null);

        assertNotEquals(vod, goog);
    }
}