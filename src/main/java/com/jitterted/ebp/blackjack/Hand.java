package com.jitterted.ebp.blackjack;

import org.fusesource.jansi.Ansi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.fusesource.jansi.Ansi.ansi;

public class Hand {
    private final String owner;
    private final List<Card> cards;
    private int totalValue;

    public Hand(String owner) {
        this.owner = owner;
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

    public boolean beats(Hand that) {
        return this.totalValue() > that.totalValue();
    }

    public boolean pushes(Hand that) {
        return this.totalValue() == that.totalValue();
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

    private boolean containsAnAce() {
        // does the hand contain at least 1 Ace?
        return cards
                .stream()
                .anyMatch(Card::isRankAce);
    }

    private boolean aceCanTakeHigherValue(int baseValue) {
        return aceAlternateValueGreaterThanOrEqualTo(baseValue) ||
                blackjack();
    }

    private boolean aceAlternateValueGreaterThanOrEqualTo(int baseValue) {
        return baseValue <= Deck.ACE.rankAlternateValue();
    }

    public Card firstCard() {
        if (cards.isEmpty()) {
            throw new IllegalStateException("hand is empty");
        }
        return cards.get(0);
    }

    public void display() {
        System.out.print(ansi().boldOff().fg(Ansi.Color.BLACK)
                                 .format("%s has: %n", this.owner));
        displayCards();
        System.out.print(ansi().boldOff().fg(Ansi.Color.BLACK)
                                       .format("(%d)%n", totalValue()));
    }

    public void displayCards() {
        System.out.println(cards().stream()
                                  .map(Card::display)
                                  .collect(Collectors.joining(
                                       ansi().cursorUp(6).cursorRight(1).toString())));
    }

    public String toString() {
        return "Hand {" +
                "owner=" + owner +
                ", cards=" + cards +
                ", totalValue=" + totalValue +
                "}";
    }
}
