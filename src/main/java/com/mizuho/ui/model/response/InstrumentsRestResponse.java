package com.mizuho.ui.model.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import javax.xml.bind.annotation.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@JacksonXmlRootElement(localName = "instruments")
public class InstrumentsRestResponse {

    @JsonProperty(value = "instrument")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<InstrumentResponseLine> responseLines = new ArrayList<>();

    public void add(InstrumentResponseLine instrumentResponseLine) {
        responseLines.add(instrumentResponseLine);
    }

    public List<InstrumentResponseLine> getResponseLines() {
        return responseLines;
    }
}