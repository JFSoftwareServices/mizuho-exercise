package com.mizuho.service;

import com.mizuho.io.entity.InstrumentEntity;
import com.mizuho.shared.dto.InstrumentDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface InstrumentService {

    Optional<List<InstrumentDto>> findInstrumentPricesByTicker(String ticker);

    void saveInstrument(InstrumentEntity instrumentEntity);

    void updatePriceOfInstrument(InstrumentEntity instrumentEntity, BigDecimal price);

    void evictStaleInstrumentsOlderThanDays(int daysAgo);

    Optional<List<InstrumentDto>> findInstrumentPricesByVendor(String vendor);
}