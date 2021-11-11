package com.jitterted.ebp.blackjack;

public class EmptyDeckException extends IllegalStateException {
    public EmptyDeckException() {
        super("all cards have been dealt");
    }
}
