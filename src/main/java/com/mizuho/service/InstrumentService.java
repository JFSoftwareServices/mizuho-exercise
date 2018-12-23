package com.mizuho.service;

import com.mizuho.io.entity.InstrumentEntity;
import com.mizuho.shared.dto.InstrumentDto;

import java.util.List;
import java.util.Optional;

public interface InstrumentService {

    Optional<List<InstrumentDto>> findInstrumentPricesByTicker(String ticker);

    void saveInstrument(InstrumentEntity instrumentEntity);

    void evictStaleInstrumentsOlderThanDays(int daysAgo);

    List<InstrumentDto> findInstrumentPricesByVendor(String vendor);
}