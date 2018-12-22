package com.mizuho.io.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

public class InstrumentEntity {
    private Long id;
    private BigDecimal price;
    private String ticker;
    private String vendor;
    private Date date;

    public InstrumentEntity() {
    }

    public InstrumentEntity(BigDecimal price, String ticker, String vendor, Date date) {
        this.price = price;
        this.ticker = ticker;
        this.vendor = vendor;
        this.date = date;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InstrumentEntity that = (InstrumentEntity) o;
        return Objects.equals(price, that.price) &&
                Objects.equals(ticker, that.ticker) &&
                Objects.equals(vendor, that.vendor) &&
                Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(price, ticker, vendor, date);
    }

    @Override
    public String toString() {
        return "InstrumentEntity{" +
                "price=" + price +
                ", ticker='" + ticker + '\'' +
                ", vendor='" + vendor + '\'' +
                ", date=" + date +
                '}';
    }
}