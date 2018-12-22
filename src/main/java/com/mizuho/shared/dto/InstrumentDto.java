package com.mizuho.shared.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class InstrumentDto implements Serializable {

    private static final long serialVersionUID = 170541511581065014L;
    private BigDecimal price;
    private String ticker;
    private String vendor;
    private Date date;
    private long pk;

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

    public long getPk() {
        return pk;
    }

    public void setPk(long pk) {
        this.pk = pk;
    }
}
