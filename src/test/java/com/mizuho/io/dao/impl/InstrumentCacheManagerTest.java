package com.mizuho.io.dao.impl;

import com.mizuho.io.entity.InstrumentEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InstrumentCacheManagerTest {

    private final InstrumentCacheManager cacheManager = InstrumentCacheManager.getInstance();

    @BeforeEach
    void setup(){
        cacheManager.clearAllInstruments();
    }

    @Test
    void addInstrumentToCache() {
        InstrumentEntity entity = new InstrumentEntity(new BigDecimal("44.84"), "INTC", "CQG", new Date());
        cacheManager.putInstrument(entity);

        assertEquals(Optional.of(entity), InstrumentCacheManager
                .getInstance()
                .getInstrument(entity));
    }

    @Test
    void updateExistingInstrument() {
        InstrumentEntity entity = new InstrumentEntity(new BigDecimal("44.84"), "INTC", "CQG", new Date());
        cacheManager.putInstrument(entity);

        entity = new InstrumentEntity(new BigDecimal("44.85"), "INTC", "CQG", new Date());
        InstrumentCacheManager.getInstance().putInstrument(entity);

        assertEquals(Optional.of(entity), InstrumentCacheManager
                .getInstance()
                .getInstrument(entity));
    }

    @Test
    void removeInstrumentFromCache() {
        InstrumentEntity entity = new InstrumentEntity(new BigDecimal("44.84"), "INTC", "CQG", new Date());
        cacheManager.putInstrument(entity);

        assertEquals(entity, InstrumentCacheManager
                .getInstance()
                .getInstrument(entity)
                .orElseThrow(() -> new RuntimeException("")));

        InstrumentCacheManager.getInstance().clearInstrument(entity);

        assertEquals(Optional.empty(), InstrumentCacheManager
                .getInstance()
                .getInstrument(entity));
    }

    /**
     * In this test the first entity is added to the cache.
     * The second entity updates the price of the first
     * The third entity is added to the cache
     * <p>
     * The expected entities in the cache are the second and third added entities
     * The expected entities are added to a {@code List}
     * <p>
     * Finally the expected and actual list of entities are compared for equality
     */
    @Test
    void getAllInstrumentFromCache() {
        List<InstrumentEntity> entities = new ArrayList<>();

        InstrumentEntity entity = new InstrumentEntity(new BigDecimal("44.84"), "INTC", "CQG", new Date());
        cacheManager.putInstrument(entity);

        entity = new InstrumentEntity(new BigDecimal("44.85"), "INTC", "CQG", new Date());
        entities.add(entity);
        InstrumentCacheManager.getInstance().putInstrument(entity);

        entity = new InstrumentEntity(new BigDecimal("44.85"), "INTC", "Bloomberg", new Date());
        entities.add(entity);
        InstrumentCacheManager.getInstance().putInstrument(entity);

        assertEquals(Optional.of(entities), InstrumentCacheManager
                .getInstance()
                .getAllInstrument());
    }
}