package com.jitterted.ebp.blackjack;

import org.fusesource.jansi.Ansi;

import static org.fusesource.jansi.Ansi.ansi;

public class Card {
    private final String suit;
    private final String rank;

    public Card(String suit, String rank) {
        this.suit = suit;
        this.rank = rank;
    }

    public int rankValue() {
        if (isRankFaceCardNotAce()) {
            return 10;
        } else if (isRankAce()) {
            return 1;
        } else {
            return Integer.parseInt(rank);
        }
    }

    public int rankAlternateValue() {
        if (isRankAce()) {
            return 11;
        }
        return rankValue();
    }

    public boolean isRankAce() {
        return rank.equals("A");
    }

    private boolean isRankFaceCardNotAce() {
        return "JQK".contains(rank);
    }

    public String display() { // long method (something about the handling of `lines` and its formatting strikes me as cantankerous)
        String[] lines = generateFace(this);

        Ansi.Color cardColor = getCardColor();
        return ansi()
                .fg(cardColor).toString()
                + String.join(ansi().cursorDown(1)
                                    .cursorLeft(11)
                                    .toString(), lines);
    }

    private Ansi.Color getCardColor() {
        return "♥♦".contains(suit) ? Ansi.Color.RED : Ansi.Color.BLACK;
    }

    private static String[] generateFace(Card card) {
        String rankSpacing = card.rank.equals("10") ? "" : " ";
        String[] lines = new String[7];
        lines[0] = "┌─────────┐";
        lines[1] = String.format("│%s%s       │", card.rank, rankSpacing);
        lines[2] = "│         │";
        lines[3] = String.format("│    %s    │", card.suit);
        lines[4] = "│         │";
        lines[5] = String.format("│       %s%s│", rankSpacing, card.rank);
        lines[6] = "└─────────┘";
        return lines;
    }

    @Override
    public String toString() {
        return "Card {" +
                "suit=" + suit +
                ", rank=" + rank +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Card card = (Card) o;

        if (!suit.equals(card.suit)) return false;
        return rank.equals(card.rank);
    }

    @Override
    public int hashCode() {
        int result = suit.hashCode();
        result = 31 * result + rank.hashCode();
        return result;
    }
}
