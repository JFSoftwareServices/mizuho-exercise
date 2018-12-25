package com.mizuho.io.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

public class InstrumentEntity implements Comparable<Date>, Serializable {
    private BigDecimal price;
    private String ticker;
    private String vendor;
    private CombinedKey combinedKey = new CombinedKey();
    private Date date;

    public InstrumentEntity() {
    }

    public InstrumentEntity(BigDecimal price, String ticker, String vendor, Date date) {
        this.price = price;
        this.ticker = ticker;
        this.vendor = vendor;
        this.date = date;
        combinedKey = new CombinedKey(ticker, vendor);
    }

    public CombinedKey getCombinedKey() {
        return combinedKey;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getTicker() {
        return ticker;
    }

    public String getVendor() {
        return vendor;
    }

    public Date getDate() {
        return date;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setTicker(String ticker) {
        combinedKey.setTicker(ticker);
        this.ticker = ticker;
    }

    public void setVendor(String vendor) {
        combinedKey.setVendor(vendor);
        this.vendor = vendor;
    }

    public void setCombinedKey(CombinedKey combinedKey) {
        this.combinedKey = combinedKey;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public int compareTo(Date o) {
        return this.getDate().compareTo(o);
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     * The other object is considered to be equal to this one if
     * <p>
     * The other object is considered equal to this one if:
     * <ul>
     * <li>The price of the other object is equal to this one
     * <li>The ticker of the other object is equal to this one
     * <li>The vendor of the other object is equal to this one
     * </ul>
     * <p>
     *
     * @param o an object
     * @return {@code true} if the price and ticker and vendor of this object is
     * equal to the price and ticker and vendor of the object
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InstrumentEntity that = (InstrumentEntity) o;
        return Objects.equals(price, that.price) &&
                Objects.equals(ticker, that.ticker) &&
                Objects.equals(vendor, that.vendor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(price, ticker, vendor);
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