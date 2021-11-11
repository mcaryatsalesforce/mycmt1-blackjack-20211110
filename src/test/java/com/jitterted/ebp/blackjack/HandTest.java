package com.jitterted.ebp.blackjack;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static com.jitterted.ebp.blackjack.CardSuit.Club;
import static com.jitterted.ebp.blackjack.CardSuit.Diamond;
import static com.jitterted.ebp.blackjack.CardSuit.Heart;
import static com.jitterted.ebp.blackjack.CardSuit.Spade;
import static com.jitterted.ebp.blackjack.CardValue.*;
import static org.assertj.core.api.Assertions.*;

public class HandTest {

    @Test
    public void handWithOneAceTwoCardsIsValuedAt11() throws Exception {
        Hand hand = new Hand("someones");
        List<Card> cards = List.of(new Card(Spade, Ace),
                                   new Card(Spade, Five));
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
        List<Card> cards = List.of(new Card(Spade, Ace),
                                   new Card(Spade, Eight),
                                   new Card(Spade, Three));
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
        List<Card> cards = List.of(new Card(Spade, Ace),
                                   new Card(Spade, Eight),
                                   new Card(Spade, Three),
                                   new Card(Spade, Ten));
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
        for (CardValue faceCardValue: Arrays.stream(values()).filter(CardValue::isCourtCard).toArray(CardValue[]::new)) {
            Hand hand = new Hand(faceCardValue.name());
            List<Card> cards = List.of(new Card(Spade, Ace),
                                       new Card(Spade, faceCardValue));
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
        List<Card> cards = List.of(new Card(Spade, Ace),
                                   new Card(Spade, Ten));
        cards.forEach(hand::take);

        assertThat(hand.totalValue())
                .isEqualTo(11 + 10);
        assertThat(hand.blackjack())
                .isTrue();
        assertThat(hand.busted())
                .isFalse();
    }

    @Test
    public void handWithOneAceDegradesWithMoreCards() {
        Hand hand = new Hand("someones");
        List<Card> cards = List.of(new Card(Spade, Ace),
                                   new Card(Spade, Nine));
        cards.forEach(hand::take);

        assertThat(hand.totalValue())
                .isEqualTo(11 + 9);

        hand.take(new Card(Spade, Deuce));
        assertThat(hand.totalValue())
                .isEqualTo(1 + 9 + 2);
    }

    @Test
    public void handWithOneAceOneNineDoesNotDegrade() {
        Hand hand = new Hand("someones");
        hand.take(new Card(Spade, Ace));
        hand.take(new Card(Club, Nine));
        assertThat(hand.totalValue()).isEqualTo(20);
    }

    @Test
    public void handWithTwoAcesOneNineDoesNotDegrade() {
        Hand hand = new Hand("someones");
        hand.take(new Card(Spade, Ace));
        hand.take(new Card(Diamond, Ace));
        hand.take(new Card(Club, Nine));
        assertThat(hand.totalValue()).isEqualTo(21);
    }

    @Test
    public void handWithOneAceOneTenDoesNotDegrade() {
        Hand hand = new Hand("someones");
        hand.take(new Card(Spade, Ace));
        hand.take(new Card(Club, Ten));
        assertThat(hand.totalValue()).isEqualTo(21);
    }

    @Test
    public void handWithTwoAcesDegradesWithMoreCards() {
        Hand hand = new Hand("someones");
        hand.take(new Card(Spade, Ace));
        assertThat(hand.totalValue()).isEqualTo(11);

        hand.take(new Card(Club, Ace));
        assertThat(hand.totalValue()).isEqualTo(11 + 1);

        hand.take(new Card(Spade, Eight));
        assertThat(hand.totalValue()).isEqualTo(1 + 11 + 8);

        hand.take(new Card(Diamond, Ace));
        assertThat(hand.totalValue()).isEqualTo(1 + 1 + 8 + 11);
    }

    @Test
    public void acesOverKings() {
        Hand ace = new Hand("ace").take(new Card(Spade, Ace));
        Hand king = new Hand("king").take(new Card(Heart, King));
        assertThat(ace.beats(king))
                .isTrue();
        assertThat(ace.pushes(king))
                .isFalse();
    }

    @Test
    public void faceCardsPush() {
        Hand queen = new Hand("queen").take(new Card(Spade, Queen));
        Hand king = new Hand("king").take(new Card(Heart, King));
        assertThat(queen.pushes(king))
                .isTrue();
        assertThat(king.pushes(queen))
                .isTrue();
        assertThat(queen.beats(king))
                .isFalse();
        assertThat(king.beats(queen))
                .isFalse();
    }
}
