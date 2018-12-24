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
 * A thread safe double check lock.... Singleton that...
 */
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

    Optional<List<InstrumentEntity>> getAllInstrumentByTicker(String ticker) {
        return Optional.of(
                Objects.requireNonNull(getAllInstrument()
                        .orElse(null))
                        .parallelStream()
                        .filter(i -> i.getTicker().equals(ticker))
                        .sorted(comparing(InstrumentEntity::getDate))
                        .collect(Collectors.toList()));
    }

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