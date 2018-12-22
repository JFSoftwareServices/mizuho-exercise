package com.mizuho.service;

import com.mizuho.io.entity.InstrumentEntity;
import com.mizuho.shared.dto.InstrumentDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface InstrumentService {

    List<InstrumentDto> findInstrumentPricesByTicker(String ticker);

    void saveInstrument(InstrumentEntity instrumentEntity);

    void evictStaleInstrumentsOlderThanDays(int daysAgo);

    List<InstrumentDto> findInstrumentPricesByVendor(String vendor);
}