package com.mizuho.ui.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class InstrumentPricesNotFoundAdvice {

    @ResponseBody
    @ExceptionHandler(InstrumentPricesNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String instrumentPricesNotFoundHandler(InstrumentPricesNotFoundException ex) {
        return ex.getMessage();
    }
}