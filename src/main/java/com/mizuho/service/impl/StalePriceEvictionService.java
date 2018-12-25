package com.mizuho.service.impl;

import com.mizuho.service.InstrumentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class StalePriceEvictionService {

    private Logger logger = LoggerFactory.getLogger(StalePriceEvictionService.class);
    private final InstrumentService instrumentService;

    @Value("${daysAgo}")
    private int daysAgo;

    public StalePriceEvictionService(InstrumentServiceImpl instrumentService) {
        this.instrumentService = instrumentService;
    }

//    @Scheduled(cron = "0/20 * * * * ?")
    @Scheduled(cron = "0 0 06 * * ?")
    public void startService() {
        logger.info("Stale instrument price eviction commencing");
        instrumentService.evictStaleInstrumentsOlderThanDays(daysAgo);
        logger.info("Stale instrument Price eviction finished");
    }
}