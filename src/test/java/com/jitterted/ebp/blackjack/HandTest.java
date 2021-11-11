package com.jitterted.ebp.blackjack;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class HandTest {

    @Test
    public void handWithOneAceTwoCardsIsValuedAt11() throws Exception {
        Hand hand = new Hand("someones");
        List<Card> cards = List.of(new Card(CardSuit.Spade, CardValue.Ace),
                                   new Card(CardSuit.Spade, CardValue.Five));
        cards.forEach(hand::take);

        assertThat(hand.totalValue())
                .isEqualTo(11 + 5);
        assertThat(hand.blackjack())
                .isFalse();
        assertThat(hand.busted())
                .isFalse();
    }

    @Test
    public void handWithOneAceAndOtherCardsEqualTo11IsValuedAt1() throws Exception {
        Hand hand = new Hand("someones");
        List<Card> cards = List.of(new Card(CardSuit.Spade, CardValue.Ace),
                                   new Card(CardSuit.Spade, CardValue.Eight),
                                   new Card(CardSuit.Spade, CardValue.Three));
        cards.forEach(hand::take);

        assertThat(hand.totalValue())
                .isEqualTo(1 + 8 + 3);
        assertThat(hand.blackjack())
                .isFalse();
        assertThat(hand.busted())
                .isFalse();
    }

    @Test
    public void handOver21IsBusted() throws Exception {
        Hand hand = new Hand("someones");
        List<Card> cards = List.of(new Card(CardSuit.Spade, CardValue.Ace),
                                   new Card(CardSuit.Spade, CardValue.Eight),
                                   new Card(CardSuit.Spade, CardValue.Three),
                                   new Card(CardSuit.Spade, CardValue.Ten));
        cards.forEach(hand::take);

        assertThat(hand.totalValue())
                .isEqualTo(1 + 8 + 3 + 10);
        assertThat(hand.blackjack())
                .isFalse();
        assertThat(hand.busted())
                .isTrue();
    }

    @Test
    public void handWithOneAceAndFaceCardIsBlackjack() throws Exception {
        for (CardValue faceCardValue: Arrays.stream(CardValue.values()).filter(CardValue::isCourtCard).toArray(CardValue[]::new)) {
            Hand hand = new Hand(faceCardValue.name());
            List<Card> cards = List.of(new Card(CardSuit.Spade, CardValue.Ace),
                                       new Card(CardSuit.Spade, faceCardValue));
            cards.forEach(hand::take);

            assertThat(hand.totalValue())
                    .isEqualTo(11 + 10);
            assertThat(hand.blackjack())
                    .isTrue();
            assertThat(hand.busted())
                    .isFalse();
        }
    }

    @Test
    public void handWithOneAceAndTenIsBlackjack() throws Exception {
        Hand hand = new Hand("someones");
        List<Card> cards = List.of(new Card(CardSuit.Spade, CardValue.Ace),
                                   new Card(CardSuit.Spade, CardValue.Ten));
        cards.forEach(hand::take);

        assertThat(hand.totalValue())
                .isEqualTo(11 + 10);
        assertThat(hand.blackjack())
                .isTrue();
        assertThat(hand.busted())
                .isFalse();
    }
}
