package com.mizuho.service;

import com.mizuho.shared.dto.InstrumentDto;

import java.util.List;
import java.util.Optional;

public interface InstrumentService {

    Optional<List<InstrumentDto>> findInstrumentPricesByTicker(String ticker);

    void saveInstrument(InstrumentDto instrumentDto);

    void evictStaleInstrumentsOlderThanDays(int daysAgo);

    Optional<List<InstrumentDto>> findInstrumentPricesByVendor(String vendor);
}