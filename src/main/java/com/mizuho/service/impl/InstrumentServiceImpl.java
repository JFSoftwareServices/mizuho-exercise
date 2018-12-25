package com.mizuho.service.impl;

import com.mizuho.io.dao.InstrumentDao;
import com.mizuho.io.entity.InstrumentEntity;
import com.mizuho.service.InstrumentService;
import com.mizuho.shared.dto.InstrumentDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class InstrumentServiceImpl implements InstrumentService {

    private final InstrumentDao dao;

    public InstrumentServiceImpl(InstrumentDao dao) {
        this.dao = dao;
    }

    @Override
    public void saveInstrument(InstrumentDto instrumentDto) {
        InstrumentEntity instrumentEntity = new InstrumentEntity();
        BeanUtils.copyProperties(instrumentDto, instrumentEntity);
        dao.save(instrumentEntity);
    }

    public Optional<List<InstrumentDto>> findInstrumentPricesByVendor(String vendor) {
        List prices = dao.findByVendor(vendor).orElse(new ArrayList<>());

        Type listType = new TypeToken<List<InstrumentDto>>() {
        }.getType();

        List<InstrumentDto> returnValues;
        returnValues = new ModelMapper().map(prices, listType);
        return Optional.of(returnValues);
    }

    public void evictStaleInstrumentsOlderThanDays(int daysAgo) {
        dao.deleteOlderThanDays(daysAgo);
    }

    public Optional<List<InstrumentDto>> findInstrumentPricesByTicker(String ticker) {

        List prices = dao.findByTicker(ticker).orElse(new ArrayList<>());

        Type listType = new TypeToken<List<InstrumentDto>>() {
        }.getType();

        List<InstrumentDto> returnValues;
        returnValues = new ModelMapper().map(prices, listType);
        return Optional.of(returnValues);
    }
}