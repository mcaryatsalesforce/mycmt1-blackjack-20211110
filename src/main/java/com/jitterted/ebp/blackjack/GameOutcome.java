package com.jitterted.ebp.blackjack;

public enum GameOutcome {
    Blackjack(2.5),
    Lose(0.0),
    Push(1.0),
    Win(2.0);

    private final double multiplier;

    GameOutcome(double multiplier) {
        this.multiplier = multiplier;
    }

    public int playerPayoff(int bet) {
        return (int) (bet * multiplier);
    }
}
