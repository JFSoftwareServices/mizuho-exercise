package com.mizuho.io.entity;

import java.io.Serializable;
import java.util.Objects;

/**
 * The key used in the cache will be a combined key consisting  of ticker and vendor
 */
public class CombinedKey implements Serializable {
    private String ticker;
    private String vendor;

    public CombinedKey() {
    }

    CombinedKey(String ticker, String vendor) {
        this.ticker = ticker;
        this.vendor = vendor;
    }

    public String getTicker() {
        return ticker;
    }

    public String getVendor() {
        return vendor;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CombinedKey that = (CombinedKey) o;
        return Objects.equals(ticker, that.ticker) &&
                Objects.equals(vendor, that.vendor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ticker, vendor);
    }

    @Override
    public String toString() {
        return vendor.concat(":").concat(ticker);
    }
}