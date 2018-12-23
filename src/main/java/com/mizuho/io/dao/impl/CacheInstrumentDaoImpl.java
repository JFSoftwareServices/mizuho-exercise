package com.mizuho.io.dao.impl;

import com.mizuho.io.dao.InstrumentDao;
import com.mizuho.io.entity.InstrumentEntity;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.String.valueOf;

@Component
public class CacheInstrumentDaoImpl implements InstrumentDao {

    private CacheManager cache = new CacheManager();
    private AtomicLong id = new AtomicLong();

    @Override
    public Optional<InstrumentEntity> get(long id) {
        return cache.get(valueOf(id));
    }

    @Override
    public Optional<List<InstrumentEntity>> findByVendor(String vendor) {
        return Optional.of(cache.getAllValues().get().parallelStream().filter(i -> i.getVendor().equals(vendor)).collect(Collectors.toList()));
    }

    @Override
    public Optional<List<InstrumentEntity>> findByTicker(String figi) {
        return Optional.ofNullable(cache.getAllValues().get().parallelStream().filter(i -> i.getTicker().equals(figi)).collect(Collectors.toList()));
    }

    @Override
    public void save(InstrumentEntity instrumentEntity) {
        instrumentEntity.setId(id.getAndIncrement());
        BigDecimal price = instrumentEntity.getPrice();
        instrumentEntity.setPrice(price.setScale(2, BigDecimal.ROUND_HALF_UP));
        cache.put(String.valueOf(instrumentEntity.getId()), instrumentEntity);
    }

    @Override
    public void updatePrice(InstrumentEntity instrumentEntity, BigDecimal price) {
//        instrumentEntity
    }

    /**
     * @param days
     */
    @Override
    public void deleteOlderThanDays(int days) {
        DateTimeZone timeZone = DateTimeZone.forID("Europe/Paris");
        DateTime dateTime = new DateTime(new java.util.Date(), timeZone);
        int thirtyDays = 31;

        Stream<InstrumentEntity> instrumentsToEvictStream = cache
                .getAllValues().get()
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
     */
    public static class CacheManager {
        private static CacheManager instance;
        private static final Object monitor = new Object();
        private Map<String, InstrumentEntity> cache;

        private CacheManager() {
            cache = new ConcurrentHashMap<>();
        }

        public void put(String cacheKey, InstrumentEntity value) {
            cache.put(cacheKey, value);
        }

        Optional<InstrumentEntity> get(String cacheKey) {
            return Optional.ofNullable(cache.get(cacheKey));
        }

        Optional<Collection<InstrumentEntity>> getAllValues() {
            return Optional.of(cache.values());
        }

        void clear(String cacheKey) {
            cache.remove(cacheKey);
        }

        public void clear() {
            cache.clear();
        }
    }
}