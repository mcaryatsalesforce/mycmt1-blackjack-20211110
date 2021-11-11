package com.jitterted.ebp.blackjack;

public class Wallet {
    private int value;

    public Wallet() {
        value = 0;
    }

    public boolean isEmpty() {
        return value == 0;
    }

    public double balance() {
        return value;
    }

    public Wallet credit(int amount) {
        requirePositive(amount);
        value += amount;
        return this;
    }

    public Wallet debit(int amount) {
        requirePositive(amount);
        value -= amount;
        return this;
    }

    private void requirePositive(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("positive values only");
        }
    }

    public String toString() {
        return "Wallet {" +
                "empty=" + isEmpty() +
                ", balance=" + balance() +
                "}";
    }
}
