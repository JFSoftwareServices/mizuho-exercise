package com.mizuho.service.impl;

import com.mizuho.service.InstrumentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class StalePriceEvictionService {

    private final InstrumentService instrumentService;

    @Value("${daysAgo}")
    private int daysAgo;

    public StalePriceEvictionService(InstrumentServiceImpl instrumentService) {
        this.instrumentService = instrumentService;
    }

    @Scheduled(cron = "0 0 12 * * ?")
    public void startService() {
        log.info("Stale instrument price eviction commencing: " + new Date());
        instrumentService.evictStaleInstrumentsOlderThanDays(daysAgo);
        log.info("Stale instrument Price eviction finished: "+ new Date());
    }
}