package com.jitterted.ebp.blackjack;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.fusesource.jansi.Ansi.ansi;

class CardTest {
    @Test
    public void rankReturned() throws Exception {
        Card card = new Card(CardSuit.Heart, CardValue.Seven);

        assertThat(card.rank())
                .isEqualTo(CardValue.Seven);
    }

    @Test
    public void suitReturned() throws Exception {
        Card card = new Card(CardSuit.Heart, CardValue.Seven);

        assertThat(card.suit())
                .isEqualTo(CardSuit.Heart);
    }

    @Test
    public void suitOfHeartsOrDiamondsIsDisplayedInRed() throws Exception {
        // given a card with Hearts or Diamonds
        Card heartsCard = new Card(CardSuit.Heart, CardValue.Ten);
        Card diamondsCard = new Card(CardSuit.Diamond, CardValue.Eight);

        // when we ask for its display representation
        String ansiRedString = ansi().fgRed().toString();

        // then we expect a red color ansi sequence
        assertThat(heartsCard.display())
                .contains(ansiRedString);
        assertThat(diamondsCard.display())
                .contains(ansiRedString);
    }

    @Test
    public void cardDisplaysSuitAsSymbol() throws Exception {
        Card spadesCard = new Card(CardSuit.Spade, CardValue.Nine);

        assertThat(spadesCard.display())
                .contains("│    ♠    │");
    }
}