package com.jitterted.ebp.blackjack;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.*;

public class CardValueTest {
    private CardValue[] cardValuesMatching(Predicate<CardValue> predicate) {
        return Arrays.stream(CardValue.values()).filter(predicate).toArray(CardValue[]::new);
    }

    @Test
    public void aceSaysItIsAce() {
        assertThat(CardValue.Ace.isAce())
                .isTrue();
    }

    @Test
    public void noOtherCardSaysItIsAce() {
        CardValue[] cardValues = cardValuesMatching(v -> !v.symbol().equals("A"));
        for (CardValue cardValue: cardValues) {
            assertThat(cardValue.isAce())
                    .describedAs("CardValue %s should not think that it's an Ace", cardValue)
                    .isFalse();
        }
    }

    @Test
    public void courtValuesSayTheyAreCourt() {
        CardValue[] cardValues = cardValuesMatching(v -> "JQK".contains(v.symbol()));
        for (CardValue cardValue: cardValues) {
            assertThat(cardValue.isCourtCard())
                    .describedAs("CardValue %s should think that it's in court", cardValue)
                    .isTrue();
        }
    }

    @Test
    public void noOtherCardSaysItIsCourt() {
        CardValue[] cardValues = cardValuesMatching(v -> !"JQK".contains(v.symbol()));
        for (CardValue cardValue: cardValues) {
            assertThat(cardValue.isCourtCard())
                    .describedAs("CardValue %s should not think that it's in court", cardValue)
                    .isFalse();
        }
    }
}
