package com.mizuho.io.dao.impl;

import com.mizuho.io.dao.Dao;
import com.mizuho.io.entity.InstrumentEntity;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.String.valueOf;

@Component
public class CacheInstrumentDaoImpl implements Dao<InstrumentEntity> {

    private CacheManager<InstrumentEntity> cache = new CacheManager<>();
    private AtomicLong id = new AtomicLong();

    @Override
    public InstrumentEntity get(long id) {
        return cache.get(valueOf(id));
    }

    @Override
    public List<InstrumentEntity> findByVendor(String vendor) {
        return cache.getAllValues().parallelStream().filter(i -> i.getVendor().equals(vendor)).collect(Collectors.toList());
    }

    @Override
    public List<InstrumentEntity> findByTicker(String figi) {
        return cache.getAllValues().parallelStream().filter(i -> i.getTicker().equals(figi)).collect(Collectors.toList());
    }

    @Override
    public void save(InstrumentEntity instrumentEntity) {
        instrumentEntity.setId(id.getAndIncrement());
        BigDecimal price = instrumentEntity.getPrice();
        instrumentEntity.setPrice(price.setScale(2, BigDecimal.ROUND_HALF_UP));
        cache.put(String.valueOf(instrumentEntity.getId()), instrumentEntity);
    }

    @Override
    public void update(InstrumentEntity instrumentEntity, String[] params) {
//        instrumentEntity
    }

    /**
     *
     * @param days
     */
    @Override
    public void deleteOlderThanDays(int days) {
        DateTimeZone timeZone = DateTimeZone.forID("Europe/Paris");
        DateTime dateTime = new DateTime(new java.util.Date(), timeZone);
        int thirtyDays = 31;

        Stream<InstrumentEntity> instrumentsToEvictStream = cache
                .getAllValues()
                .parallelStream()
                .filter(i -> new DateTime(i.getDate()).isBefore(new DateTime().minusDays(thirtyDays)));

        List<Long> instrumentsToEvictList = instrumentsToEvictStream
                .map(InstrumentEntity::getId)
                .collect(Collectors.toList());

        for (Long instrumentId : instrumentsToEvictList) {
            cache.clear(String.valueOf(instrumentId));
        }
    }

    /**
     *
     * @param <T>
     */
    public static class CacheManager<T> {
        private static CacheManager instance;
        private static final Object monitor = new Object();
        private Map<String, T> cache;

        private CacheManager() {
            cache = new ConcurrentHashMap<>();
        }

        public void put(String cacheKey, T value) {
            cache.put(cacheKey, value);
        }

        T get(String cacheKey) {
            return cache.get(cacheKey);
        }

        Collection<T> getAllValues() {
            return cache.values();
        }

        void clear(String cacheKey) {
            cache.remove(cacheKey);
        }

        public void clear() {
            cache.clear();
        }
    }
}