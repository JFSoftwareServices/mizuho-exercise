package com.mizuho.shared.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JacksonXmlRootElement(localName = "instrument")
public class InstrumentDto implements Serializable {
    private static final long serialVersionUID = 8503897109288209524L;
    private BigDecimal price;
    private String ticker;
    private String vendor;
    private Date date;

    @Override
    public String toString() {
        return "InstrumentDto{" +
                "price=" + price +
                ", ticker='" + ticker + '\'' +
                ", vendor='" + vendor + '\'' +
                ", date=" + date +
                '}';
    }
}