package com.mizuho.service.impl;

import com.mizuho.io.dao.Dao;
import com.mizuho.io.entity.InstrumentEntity;
import com.mizuho.service.InstrumentService;
import com.mizuho.shared.dto.InstrumentDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;

@Service
public class InstrumentServiceImpl implements InstrumentService {

    @Autowired
    private Dao dao;

    public void saveInstrument(InstrumentEntity instrumentEntity) {
        dao.save(instrumentEntity);
    }

    public List<InstrumentDto> findInstrumentPricesByVendor(String vendor) {
        List prices = dao.findByVendor(vendor);

        Type listType = new TypeToken<List<InstrumentDto>>() {
        }.getType();

        List<InstrumentDto> returnValues;
        returnValues = new ModelMapper().map(prices, listType);
        return returnValues;
    }

    public void evictStaleInstrumentsOlderThanDays(int daysAgo) {
        dao.deleteOlderThanDays(daysAgo);
    }

    public List<InstrumentDto> findInstrumentPricesByTicker(String ticker) {

        List prices = dao.findByTicker(ticker);

        Type listType = new TypeToken<List<InstrumentDto>>() {
        }.getType();

        List<InstrumentDto> returnValues;
        returnValues = new ModelMapper().map(prices, listType);
        return returnValues;
    }
}