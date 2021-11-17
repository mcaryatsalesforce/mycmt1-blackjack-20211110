package com.jitterted.ebp.blackjack;

public enum CardValue {
    Ace("A", 1, 11, false, true),
    Deuce("2"),
    Three("3"),
    Four("4"),
    Five("5"),
    Six("6"),
    Seven("7"),
    Eight("8"),
    Nine("9"),
    Ten("10"),
    Jack("J", 10, 10, true, false),
    Queen("Q", 10, 10, true, false),
    King("K", 10, 10, true, false);

    private final String symbol;
    private final int value;
    private final int alternateValue;
    private final boolean courtCard;
    private final boolean ace;

    CardValue(String symbol, int value, int alternateValue, boolean courtCard, boolean ace) {
        this.symbol = symbol;
        this.value = value;
        this.alternateValue = alternateValue;
        this.courtCard = courtCard;
        this.ace = ace;
    }

    CardValue(String symbol) {
        this(symbol, Integer.parseInt(symbol), Integer.parseInt(symbol), false, false);
    }

    public String symbol() {
        return symbol;
    }

    public int value() {
        return value;
    }

    public int alternateValue() {
        return alternateValue;
    }

    public boolean isCourtCard() {
        return courtCard;
    }

    public boolean isAce() {
        return ace;
    }
}
