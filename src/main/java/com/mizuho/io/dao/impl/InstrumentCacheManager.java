package com.mizuho.io.dao.impl;

import com.mizuho.io.entity.CompositeKeyPair;
import com.mizuho.io.entity.InstrumentEntity;
import org.joda.time.DateTime;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * A thread safe singleton Instrument Cache that uses an memory concurrent map
 */
public class InstrumentCacheManager {
    private static InstrumentCacheManager instance;
    private ConcurrentHashMap<CompositeKeyPair, InstrumentEntity> cache = new ConcurrentHashMap<>();

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
        cache.putIfAbsent(entity.getCompositeKeyPair(), entity);
        cache.computeIfPresent(entity.getCompositeKeyPair(), (k, v) -> entity);
    }

    public Optional<InstrumentEntity> getInstrument(InstrumentEntity entity) {
        return Optional.ofNullable(cache.get(entity.getCompositeKeyPair()));
    }

    Optional<List<InstrumentEntity>> getAllInstrument() {
        return Optional.of(cache.values().parallelStream().collect(Collectors.toList()));
    }

    void clearInstrument(InstrumentEntity entity) {
        cache.remove(entity.getCompositeKeyPair());
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

        instruments.forEach(this::clearInstrument);
    }

    public void clearAllInstruments() {
        cache.clear();
    }
}