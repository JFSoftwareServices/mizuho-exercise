package com.mizuho.io.entity;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.number.OrderingComparison.comparesEqualTo;

class CompositeKeyPairTest {

    @Test
    void testEqual() {
        CompositeKeyPair compositeKeyPairOne = new CompositeKeyPair("GOOG", "Bloomberg");
        CompositeKeyPair compositeKeyPairTwo = new CompositeKeyPair("GOOG", "Bloomberg");
        assertThat(compositeKeyPairOne, comparesEqualTo(compositeKeyPairTwo));
        assertThat(compositeKeyPairTwo, comparesEqualTo(compositeKeyPairOne));
    }

    @Test
    void testGreaterThan() {
        CompositeKeyPair compositeKeyPairOne = new CompositeKeyPair("GOOG", "Bloomberg");
        CompositeKeyPair compositeKeyPairTwo = new CompositeKeyPair("GOOG", "CQG");
        assertThat(compositeKeyPairTwo, greaterThan(compositeKeyPairOne));
    }

    @Test
    void testLessThan() {
        CompositeKeyPair compositeKeyPairOne = new CompositeKeyPair("GOOG", "CQG");
        CompositeKeyPair compositeKeyPairTwo = new CompositeKeyPair("GOOG", "Bloomberg");
        assertThat(compositeKeyPairTwo, lessThan(compositeKeyPairOne));
    }
}