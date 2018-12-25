package com.mizuho.io.dao.impl;

import com.mizuho.io.dao.InstrumentDao;
import com.mizuho.io.entity.InstrumentEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class InstrumentCacheDaoImpl implements InstrumentDao {

    private InstrumentCacheManager cacheManager = InstrumentCacheManager.getInstance();

    @Override
    public Optional<InstrumentEntity> get(InstrumentEntity entity) {
        return cacheManager.getInstrument(entity);
    }

    @Override
    public Optional<List<InstrumentEntity>> findByVendor(String vendor) {
        return cacheManager.getAllInstrumentByVendor(vendor);
    }

    @Override
    public Optional<List<InstrumentEntity>> findByTicker(String ticker) {
        return cacheManager.getAllInstrumentByTicker(ticker);
    }

    @Override
    public void save(InstrumentEntity instrumentEntity) {
        cacheManager.putInstrument(instrumentEntity);
    }

    @Override
    public void deleteOlderThanDays(int days) {
        cacheManager.deleteAllInstrumentsOlderThanDays(days);
    }

    @Override
    public void clearAll() {
        cacheManager.clearAllInstruments();
    }
}