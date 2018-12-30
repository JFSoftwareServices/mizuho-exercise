package com.mizuho.ui.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter @AllArgsConstructor
public class InstrumentResponseLine {
    private final String ticker;
    private final String vendor;
    private final BigDecimal price;

}
