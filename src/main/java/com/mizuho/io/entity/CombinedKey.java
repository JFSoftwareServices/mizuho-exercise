package com.mizuho.io.entity;

import java.util.Objects;

/**
 * The key in the cache map will be a combination of ticker and vendor
 * Not sure if this works well and equals method implemented correctly
 */
public class CombinedKey {
    private String ticker;
    private String vendor;

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