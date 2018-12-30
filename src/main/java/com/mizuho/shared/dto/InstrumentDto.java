package com.mizuho.shared.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JacksonXmlRootElement(localName = "instrument")
public class InstrumentDto implements Serializable {
    private static final long serialVersionUID = 8503897109288209524L;
    private BigDecimal price;
    private String ticker;
    private String vendor;
    private Date date;

}