package com.mizuho.shared.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@JacksonXmlRootElement(localName = "instrument")
public class InstrumentDto implements Serializable {
    private static final long serialVersionUID = 8503897109288209524L;
    private BigDecimal price;
    private String ticker;
    private String vendor;
    private Date date;

    public InstrumentDto(BigDecimal price, String ticker, String vendor, Date date) {
        this.price = price;
        this.ticker = ticker;
        this.vendor = vendor;
        this.date = date;
    }

    public InstrumentDto() {
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

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