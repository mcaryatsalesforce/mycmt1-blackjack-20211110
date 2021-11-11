package com.jitterted.ebp.blackjack;

public enum CardSuit {
    Heart("♥", true),
    Spade("♠"),
    Diamond("♦", true),
    Club("♣");

    private final String symbol;
    private final boolean red;

    CardSuit(String symbol, boolean red) {
       this.symbol = symbol;
       this.red = red;
    }

    CardSuit(String symbol) {
        this(symbol, false);
    }

    public boolean red() {
        return red;
    }

    public String symbol() {
        return symbol;
    }
}
