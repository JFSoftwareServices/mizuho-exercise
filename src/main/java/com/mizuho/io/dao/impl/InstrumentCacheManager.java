package com.mizuho.io.dao.impl;

import com.mizuho.io.entity.CombinedKey;
import com.mizuho.io.entity.InstrumentEntity;
import org.joda.time.DateTime;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;

/**
 * A thread safe singleton Instrument Cache that uses an memory concurrent map
 */
//TODO - Add the instrument eviction functionality to this class. At the minute the functionality resides in a route
class InstrumentCacheManager {
    private static InstrumentCacheManager instance;
    private ConcurrentHashMap<CombinedKey, InstrumentEntity> cache = new ConcurrentHashMap<>();

    private InstrumentCacheManager() {
    }

    public static InstrumentCacheManager getInstance() {
        if (instance == null) {
            synchronized (InstrumentCacheManager.class) {
                if (instance == null) {
                    instance = new InstrumentCacheManager();
                }
            }
        }
        return instance;
    }

    /**
     * Adds an Instrument entity to the cache if one isn't present. An entity is determined to be
     * present in the cache if there is currently an entry with in the cache containing the same
     * ticker and vendor as the one being added
     */
    void putInstrument(InstrumentEntity entity) {
        cache.putIfAbsent(entity.getCombinedKey(), entity);
        cache.computeIfPresent(entity.getCombinedKey(), (k, v) -> entity);
    }

    Optional<InstrumentEntity> getInstrument(InstrumentEntity entity) {
        return Optional.ofNullable(cache.get(entity.getCombinedKey()));
    }

    Optional<List<InstrumentEntity>> getAllInstrument() {
        return Optional.of(cache.values().parallelStream().collect(Collectors.toList()));
    }

    void clearInstrument(InstrumentEntity entity) {
        cache.remove(entity.getCombinedKey());
    }

    /**
     * @param vendor
     * @return A List of InstrumentEntity ordered by insertion date
     */
    Optional<List<InstrumentEntity>> getAllInstrumentByVendor(String vendor) {
        return Optional.of(
                Objects.requireNonNull(getAllInstrument()
                        .orElse(null))
                        .parallelStream()
                        .filter(i -> i.getVendor().equals(vendor))
                        .sorted(comparing(InstrumentEntity::getDate))
                        .collect(Collectors.toList())
        );
    }

    /**
     * @param ticker
     * @return A List of InstrumentEntity ordered by insertion date
     */
    Optional<List<InstrumentEntity>> getAllInstrumentByTicker(String ticker) {
        return Optional.of(
                Objects.requireNonNull(getAllInstrument()
                        .orElse(null))
                        .parallelStream()
                        .filter(i -> i.getTicker().equals(ticker))
                        .sorted(comparing(InstrumentEntity::getDate))
                        .collect(Collectors.toList()));
    }

    /**
     * @param days Instrument entities older than this date will be expired from the cache
     */
    void deleteAllInstrumentsOlderThanDays(int days) {
        List<InstrumentEntity> instruments =
                Objects.requireNonNull(getAllInstrument()
                        .orElse(null))
                        .parallelStream()
                        .filter(i -> new DateTime(i.getDate())
                                .isBefore(new DateTime()
                                        .minusDays(days)))
                        .collect(Collectors.toList());

        for (InstrumentEntity entity : instruments) {
            clearInstrument(entity);
        }
    }

    void clearAllInstruments() {
        cache.clear();
    }
}