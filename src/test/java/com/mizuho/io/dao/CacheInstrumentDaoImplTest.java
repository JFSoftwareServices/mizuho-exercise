package com.mizuho.io.dao;

import com.mizuho.io.dao.impl.CacheInstrumentDaoImpl;
import com.mizuho.io.entity.InstrumentEntity;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertFalse;

class CacheInstrumentDaoImplTest {

    private InstrumentDao dao = new CacheInstrumentDaoImpl();

    @Test
    final void savingMultipleInstrumentsToTheCache() {

        InstrumentEntity firstInstrument = new InstrumentEntity();
        firstInstrument.setTicker("INTC");
        firstInstrument.setDate(new Date());
        firstInstrument.setVendor("CQG");
        firstInstrument.setPrice(new BigDecimal(44.84));
        dao.save(firstInstrument);

        InstrumentEntity secondInstrument = new InstrumentEntity();
        secondInstrument.setTicker("GOOG");
        secondInstrument.setDate(new Date());
        secondInstrument.setVendor("Bloomberg");
        secondInstrument.setPrice(new BigDecimal(979.84));
        dao.save(secondInstrument);

        assertEquals(firstInstrument, dao.get(firstInstrument.getId()).orElseThrow(RuntimeException::new));
        assertEquals(secondInstrument, dao.get(secondInstrument.getId()).orElseThrow(RuntimeException::new));
    }

    @Test
    final void deleteMultipleInstrumentsFromTheCache() {

        DateTimeZone timeZone = DateTimeZone.forID("Europe/Paris");
        DateTime dateTime = new DateTime(new java.util.Date(), timeZone);
        DateTime twentyNineDays = dateTime.minusDays(29);
        DateTime thirtyDays = dateTime.minusDays(30);
        DateTime thirtyOneDays = dateTime.minusDays(31);

        InstrumentEntity firstInstrument = new InstrumentEntity();
        firstInstrument.setTicker("MSFT");
        firstInstrument.setDate(twentyNineDays.toDate());
        firstInstrument.setVendor("CQG");
        firstInstrument.setPrice(new BigDecimal(98.23));
        dao.save(firstInstrument);

        InstrumentEntity secondInstrument = new InstrumentEntity();
        secondInstrument.setTicker("GOOG");
        secondInstrument.setDate(thirtyDays.toDate());
        secondInstrument.setVendor("Bloomberg");
        secondInstrument.setPrice(new BigDecimal(979.84));
        dao.save(secondInstrument);

        InstrumentEntity thirdInstrument = new InstrumentEntity();
        thirdInstrument.setTicker("GOOG");
        thirdInstrument.setDate(thirtyOneDays.toDate());
        thirdInstrument.setVendor("Bloomberg");
        thirdInstrument.setPrice(new BigDecimal(98.23));
        dao.save(thirdInstrument);

        dao.deleteOlderThanDays(30);
        assertEquals(firstInstrument, dao.get(firstInstrument.getId()).orElseThrow(RuntimeException::new));
        assertEquals(secondInstrument, dao.get(secondInstrument.getId()).orElseThrow(RuntimeException::new));
        assertFalse(dao.get(thirdInstrument.getId()).isPresent());
    }

    @Test
    final void findInstrumentsByTicker() {
        InstrumentEntity firstInstrument = new InstrumentEntity();
        firstInstrument.setTicker("GOOG");
        firstInstrument.setDate(new Date());
        firstInstrument.setVendor("CQG");
        firstInstrument.setPrice(new BigDecimal(979.84));
        dao.save(firstInstrument);

        InstrumentEntity secondInstrument = new InstrumentEntity();
        secondInstrument.setTicker("INTC");
        secondInstrument.setDate(new Date());
        secondInstrument.setVendor("CQG");
        secondInstrument.setPrice(new BigDecimal(44.84));
        dao.save(secondInstrument);

        InstrumentEntity thirdInstrument = new InstrumentEntity();
        thirdInstrument.setTicker("GOOG");
        secondInstrument.setDate(new Date());
        thirdInstrument.setVendor("Bloomberg");
        thirdInstrument.setPrice(new BigDecimal(979.84));
        dao.save(thirdInstrument);

        List<InstrumentEntity> instruments = dao.findByTicker("GOOG").orElseThrow(RuntimeException::new);
        assertEquals(2, instruments.size());
        assertEquals(firstInstrument, instruments.get(0));
        assertEquals(thirdInstrument, instruments.get(1));
    }

    @Test
    final void findInstrumentsByVendor() {
        InstrumentEntity firstInstrument = new InstrumentEntity();
        firstInstrument.setTicker("GOOG");
        firstInstrument.setDate(new Date());
        firstInstrument.setVendor("CQG");
        firstInstrument.setPrice(new BigDecimal(979.84));
        dao.save(firstInstrument);

        InstrumentEntity secondInstrument = new InstrumentEntity();
        secondInstrument.setTicker("AAPL");
        secondInstrument.setDate(new Date());
        secondInstrument.setVendor("CQG");
        secondInstrument.setPrice(new BigDecimal(172.29));
        dao.save(secondInstrument);

        InstrumentEntity thirdInstrument = new InstrumentEntity();
        thirdInstrument.setTicker("GOOG");
        secondInstrument.setDate(new Date());
        thirdInstrument.setVendor("Bloomberg");
        thirdInstrument.setPrice(new BigDecimal(979.84));
        dao.save(thirdInstrument);

        List<InstrumentEntity> instruments = dao.findByVendor("CQG").orElseThrow(RuntimeException::new);
        assertEquals(2, instruments.size());
        assertEquals(firstInstrument, instruments.get(0));
        assertEquals(secondInstrument, instruments.get(1));
    }
}