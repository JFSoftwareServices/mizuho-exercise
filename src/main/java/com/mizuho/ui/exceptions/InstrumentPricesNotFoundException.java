package com.mizuho.ui.exceptions;

public class InstrumentPricesNotFoundException extends RuntimeException {

    public InstrumentPricesNotFoundException(String ticker) {
        super("Could not find prices for instrument " + ticker);
    }
}