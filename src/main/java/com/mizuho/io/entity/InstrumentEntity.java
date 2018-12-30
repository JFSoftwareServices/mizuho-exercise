package com.mizuho.io.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
public class InstrumentEntity implements Serializable {
    private BigDecimal price;
    private String ticker;
    private String vendor;
    private CompositeKeyPair compositeKeyPair = new CompositeKeyPair(ticker, vendor);
    private Date date;

    public InstrumentEntity(BigDecimal price, String ticker, String vendor, Date date) {
        this.price = price;
        this.ticker = ticker;
        this.vendor = vendor;
        this.date = date;
        compositeKeyPair = new CompositeKeyPair(ticker, vendor);
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

}