package com.mizuho.io.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

import static java.util.Comparator.comparing;

/**
 * The key used in the cache will be a combined key consisting  of ticker and vendor
 */
@Getter
@Setter
@AllArgsConstructor
public class CompositeKeyPair implements Comparable<CompositeKeyPair>, Serializable {
    private String ticker;
    private String vendor;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompositeKeyPair that = (CompositeKeyPair) o;
        return Objects.equals(ticker, that.ticker) &&
                Objects.equals(vendor, that.vendor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ticker, vendor);
    }

    @Override
    public int compareTo(CompositeKeyPair o) {
        return comparing((CompositeKeyPair o1) -> o1.ticker.compareTo(ticker))
                .thenComparing((CompositeKeyPair o1) -> o1.vendor.compareTo(vendor))
                .compare(this, o);
    }
}