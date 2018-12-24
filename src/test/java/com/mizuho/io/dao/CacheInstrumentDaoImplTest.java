package com.mizuho.io.dao;

import com.mizuho.io.dao.impl.CacheInstrumentDaoImpl;
import com.mizuho.io.entity.InstrumentEntity;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class CacheInstrumentDaoImplTest {

    private InstrumentDao dao  = new CacheInstrumentDaoImpl();

    @BeforeEach
    void setUp() {
        dao.clearAll();
    }

    @Test
    final void savingMultipleInstrumentsToTheCache() {
        InstrumentEntity firstInstrument = new InstrumentEntity(
                new BigDecimal(44.84), "INTC", "CQG", new Date());
        dao.save(firstInstrument);

        InstrumentEntity secondInstrument = new InstrumentEntity(
                new BigDecimal(979.84), "GOOG", "Bloomberg", new Date());
        dao.save(secondInstrument);

        assertEquals(firstInstrument, dao.get(firstInstrument).orElse(null));
        assertEquals(secondInstrument, dao.get(secondInstrument).orElse(null));
    }

    @Test
    final void deleteStaleInstrumentsFromTheCache() {
        DateTimeZone timeZone = DateTimeZone.forID("Europe/Paris");
        DateTime dateTime = new DateTime(new java.util.Date(), timeZone);
        Date twentyEightDays = dateTime.minusDays(28).toDate();
        Date twentyNineDays = dateTime.minusDays(29).toDate();
        Date thirtyDays = dateTime.minusDays(30).toDate();
        Date thirtyOneDays = dateTime.minusDays(31).toDate();

        InstrumentEntity firstInstrument = new InstrumentEntity(new BigDecimal(98.23), "MSFT", "CQG", twentyEightDays);
        dao.save(firstInstrument);

        InstrumentEntity secondInstrument = new InstrumentEntity(new BigDecimal(98.23), "MSFT", "CQG", twentyNineDays);
        dao.save(secondInstrument);

        InstrumentEntity thirdInstrument = new InstrumentEntity(new BigDecimal(979.84), "GOOG", "Bloomberg", thirtyDays);
        dao.save(thirdInstrument);

        InstrumentEntity fourthInstrument = new InstrumentEntity(new BigDecimal(98.23), "GOOG", "Bloomberg", thirtyOneDays);
        dao.save(fourthInstrument);

        dao.deleteOlderThanDays(30);
        assertEquals(firstInstrument, dao.get(firstInstrument).orElse(null));
        assertEquals(secondInstrument, dao.get(secondInstrument).orElse(null));
    }

    @Test
    final void findInstrumentsByTicker() {
        InstrumentEntity firstInstrument = new InstrumentEntity(
                new BigDecimal(979.84), "GOOG", "CQG", new Date());
        dao.save(firstInstrument);

        InstrumentEntity secondInstrument = new InstrumentEntity(
                new BigDecimal(44.84), "INTC", "CQG", new Date());
        dao.save(secondInstrument);

        InstrumentEntity thirdInstrument = new InstrumentEntity(
                new BigDecimal(979.84), "GOOG", "Bloomberg", new Date());

        thirdInstrument.setPrice(new BigDecimal(979.84));
        dao.save(thirdInstrument);

        List<InstrumentEntity> instruments = dao.findByTicker("GOOG").orElseThrow(RuntimeException::new);
        assertEquals(2, instruments.size());

        assertEquals(firstInstrument, instruments.get(0));
        assertEquals(thirdInstrument, instruments.get(1));
    }

    @Test
    final void findInstrumentsByVendor() {
        InstrumentEntity firstInstrument = new InstrumentEntity(
                new BigDecimal(979.84), "GOOG", "CQG", new Date());
        dao.save(firstInstrument);

        InstrumentEntity secondInstrument = new InstrumentEntity(
                new BigDecimal(172.29), "AAPL", "CQG", new Date());
        dao.save(secondInstrument);

        InstrumentEntity thirdInstrument = new InstrumentEntity(
                new BigDecimal(979.84), "GOOG", "Bloomberg", new Date());
        dao.save(thirdInstrument);

        List<InstrumentEntity> instruments = dao.findByVendor("CQG").orElseThrow(RuntimeException::new);

        assertEquals(firstInstrument, instruments.get(1));
        assertEquals(secondInstrument, instruments.get(0));
    }
}