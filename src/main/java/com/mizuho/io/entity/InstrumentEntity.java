package com.mizuho.io.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data @NoArgsConstructor
public class InstrumentEntity implements Serializable {
    private BigDecimal price;
    private String ticker;
    private String vendor;
    private CompositeKeyPair compositeKeyPair = new CompositeKeyPair(ticker, vendor);
    private Date date;

    public InstrumentEntity(BigDecimal price, String ticker, String vendor, Date date) {
        this.price = price;
        this.ticker = ticker;
        this.vendor = vendor;
        this.date = date;
        compositeKeyPair = new CompositeKeyPair(ticker, vendor);
    }
}