package com.mizuho.io.dao;

import com.mizuho.io.entity.InstrumentEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface InstrumentDao {

    Optional<InstrumentEntity> get(long id);

    Optional<List<InstrumentEntity>> findByVendor(String vendor);

    Optional<List<InstrumentEntity>> findByTicker(String name);

    void save(InstrumentEntity instrumentEntity);

    void updatePrice(InstrumentEntity instrumentEntity, BigDecimal price);

    void deleteOlderThanDays(int days);
}