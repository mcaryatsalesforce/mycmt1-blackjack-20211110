package com.jitterted.ebp.blackjack;

public class Wallet {
    private int balance;

    public Wallet() {
        balance = 0;
    }

    public boolean isEmpty() {
        return balance == 0;
    }

    public double balance() {
        return balance;
    }

    public Wallet credit(int credit) {
        requirePositiveAmount(credit);
        balance += credit;
        return this;
    }

    public Wallet debit(int debit) {
        requirePositiveAmount(debit);
        balance -= debit;
        return this;
    }

    public Wallet bet(int amount) {
        requireBalanceCovering(amount);
        return debit(amount);
    }

    private void requireBalanceCovering(int amount) {
        if (amount > balance) {
            throw new IllegalStateException("insufficient funds for your wager");
        }
    }

    private void requirePositiveAmount(int amount) {
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
