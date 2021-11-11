package com.jitterted.ebp.blackjack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    public static final Card ACE = new Card(CardSuit.Heart, CardValue.Ace);

    private final List<Card> cards = new ArrayList<>();

    public Deck() {
        for (CardSuit suit : CardSuit.values()) {
            for (CardValue cardValue : CardValue.values()) {
                cards.add(new Card(suit, cardValue));
            }
        }
        Collections.shuffle(cards);
    }

    public int size() {
        return cards.size();
    }

    public Card draw() {
        try {
            return cards.remove(0);
        } catch (IndexOutOfBoundsException ex) {
            throw new EmptyDeckException();
        }
    }
}
