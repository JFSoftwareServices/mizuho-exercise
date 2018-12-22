package com.mizuho.ui.model.response;

import java.math.BigDecimal;

public class InstrumentResponseLine {
    private final String ticker;
    private final String vendor;
    private final BigDecimal price;

    public InstrumentResponseLine(String ticker, String vendor, BigDecimal price) {
        this.ticker = ticker;
        this.vendor = vendor;
        this.price = price;
    }

    public String getTicker() {
        return ticker;
    }

    public String getVendor() {
        return vendor;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
