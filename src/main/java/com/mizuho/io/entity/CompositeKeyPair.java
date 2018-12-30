package com.mizuho.io.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

import static java.util.Comparator.comparing;

/**
 * The key used in the cache will be a combined key consisting  of ticker and vendor
 */
@Data @AllArgsConstructor
public class CompositeKeyPair implements Comparable<CompositeKeyPair>, Serializable {
    private String ticker;
    private String vendor;

    @Override
    public int compareTo(CompositeKeyPair o) {
        return comparing((CompositeKeyPair o1) -> o1.ticker.compareTo(ticker))
                .thenComparing((CompositeKeyPair o1) -> o1.vendor.compareTo(vendor))
                .compare(this, o);
    }
}