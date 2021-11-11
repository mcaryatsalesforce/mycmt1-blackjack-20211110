package com.jitterted.ebp.blackjack;

public enum CardValue {
    Ace("A", false, true),
    Deuce("2"),
    Three("3"),
    Four("4"),
    Five("5"),
    Six("6"),
    Seven("7"),
    Eight("8"),
    Nine("9"),
    Ten("10"),
    Jack("J", true, false),
    Queen("Q", true, false),
    King("K", true, false);

    private final String symbol;
    private final boolean courtCard;
    private final boolean ace;

    CardValue(String symbol, boolean courtCard, boolean ace) {
        this.symbol = symbol;
        this.courtCard = courtCard;
        this.ace = ace;
    }

    CardValue(String symbol) {
        this(symbol, false, false);
    }

    public String symbol() {
        return symbol;
    }

    public boolean isCourtCard() {
        return courtCard;
    }

    public boolean isAce() {
        return ace;
    }
}
