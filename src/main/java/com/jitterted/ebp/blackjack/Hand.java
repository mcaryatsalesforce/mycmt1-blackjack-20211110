package com.jitterted.ebp.blackjack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Hand {
    private final List<Card> cards;
    private int totalValue;

    public Hand() {
        cards = new ArrayList<>();
        totalValue = 0;
    }

    public Hand take(Card card) {
        cards.add(card);
        totalValue = addAceAlternateValue(baseValue());
        return this;
    }

    public List<Card> cards() {
        return Collections.unmodifiableList(cards);
    }

    public int totalValue() {
        return totalValue;
    }

    public boolean blackjack() {
        return cards.size() == 2 &&
                cards.stream().anyMatch(Card::isRankAce) &&
                cards.stream().anyMatch(c -> c.rankValue() == 10);
    }

    public boolean busted() {
        return totalValue > 21;
    }

    private int baseValue() {
        return cards
                .stream()
                .mapToInt(Card::rankValue)
                .sum();
    }

    private int addAceAlternateValue(int handValue) {
        // if the total hand value <= Ace's alternate value, then change the Ace's value to the alternate
        if (containsAnAce() && aceCanTakeHigherValue(handValue)) {
            handValue += (Deck.ACE.rankAlternateValue() - Deck.ACE.rankValue());
        }
        return handValue;
    }

    public boolean containsAnAce() {
        // does the hand contain at least 1 Ace?
        return cards
                .stream()
                .anyMatch(Card::isRankAce);
    }

    private boolean aceCanTakeHigherValue(int handValue) {
        return (handValue < Deck.ACE.rankAlternateValue()) ||
                cards.size() == 2 && cards.stream().anyMatch(c -> c.rankValue() == 10);
    }

    public Card firstCard() {
        if (cards.isEmpty()) {
            throw new IllegalStateException("hand is empty");
        }
        return cards.get(0);
    }
}
