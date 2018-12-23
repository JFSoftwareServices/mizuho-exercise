package com.mizuho.ui.exceptions;

public class InstrumentPricesNotFoundException extends RuntimeException {

    public InstrumentPricesNotFoundException(String name) {
        super("Could not find prices for instrument " + name);
    }
}