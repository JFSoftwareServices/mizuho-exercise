package com.mizuho.io.dao;

import com.mizuho.io.entity.CombinedKey;
import com.mizuho.io.entity.InstrumentEntity;

import java.util.List;
import java.util.Optional;

public interface InstrumentDao {

    Optional<InstrumentEntity> get(InstrumentEntity instrumentEntity);

    Optional<List<InstrumentEntity>> findByVendor(String vendor);

    Optional<List<InstrumentEntity>> findByTicker(String name);

    void save(InstrumentEntity instrumentEntity);

    void deleteOlderThanDays(int days);

    void clearAll();
}