package com.mizuho.service.impl;

import com.mizuho.io.dao.impl.InstrumentCacheManager;
import com.mizuho.io.entity.InstrumentEntity;
import com.mizuho.shared.dto.InstrumentDto;
import org.apache.camel.Exchange;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ResetExchangeBodyService {
    public void setBodyToLatestCachedInstrument(Exchange exchange) {
        InstrumentEntity instrumentEntity = new InstrumentEntity();
        InstrumentDto instrumentDto = (InstrumentDto) exchange.getIn().getBody();
        BeanUtils.copyProperties(instrumentDto, instrumentEntity);
        Optional<InstrumentEntity> optionalEntity = InstrumentCacheManager.getInstance().getInstrument(instrumentEntity);

        instrumentEntity = optionalEntity.orElseThrow(() -> new RuntimeException("There is no entity present"));
        BeanUtils.copyProperties(instrumentEntity, instrumentDto);
        exchange.getOut().setBody(instrumentDto);
    }
}