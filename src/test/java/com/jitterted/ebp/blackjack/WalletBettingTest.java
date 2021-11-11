package com.jitterted.ebp.blackjack;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class WalletBettingTest {
    @Test
    public void emptyAndBetThenThrows() {
        Wallet wallet = new Wallet();
        assertThatThrownBy(() -> wallet.bet(10))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    public void creditAndBetNegativeThrowsException() {
        Wallet wallet = new Wallet().credit(10);
        assertThatThrownBy(() -> wallet.debit(-3))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void creditAndBetZeroThrowsException() {
        Wallet wallet = new Wallet().credit(10);
        assertThatThrownBy(() -> wallet.bet(0))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void creditAndCanBetTooMuchReportsFalse() {
        Wallet wallet = new Wallet().credit(3);
        assertThat(wallet.canBet(4)).isFalse();
        assertThat(wallet.balance()).isEqualTo(3);
    }

    @Test
    public void creditAndBetTooMuchThrowsException() {
        Wallet wallet = new Wallet().credit(3);
        assertThatThrownBy(() -> wallet.bet(4))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    public void debitAndCanBetReportsFalse() {
        Wallet wallet = new Wallet().debit(1);
        assertThat(wallet.canBet(1)).isFalse();
        assertThat(wallet.balance()).isEqualTo(-1);
    }

    @Test
    public void debitAndBetThrowsException() {
        Wallet wallet = new Wallet().debit(1);
        assertThatThrownBy(() -> wallet.bet(1))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    public void creditAndCanBetPositiveReportsTrue() {
        Wallet wallet = new Wallet().credit(10);
        assertThat(wallet.canBet(3)).isTrue();
        assertThat(wallet.balance()).isEqualTo(10);
    }

    @Test
    public void creditAndBetPositiveReportsReducedAmountInBalance() {
        Wallet wallet = new Wallet().credit(10);
        wallet.bet(3);
        assertThat(wallet.balance()).isNotZero();
        assertThat(wallet.balance()).isEqualTo(10 - 3);
    }

    @Test
    public void creditAndBetTriceThenReportsDifferenceOfAmountsInBalance() {
        Wallet wallet = new Wallet().credit(10);
        wallet.bet(3)
              .bet(2)
              .bet(1);
        assertThat(wallet.balance()).isNotZero();
        assertThat(wallet.balance()).isEqualTo(10 - 3 - 2 - 1);
    }

    @Test
    public void creditAndBetToEmptyThenReportsEmptyBalance() {
        Wallet wallet = new Wallet().credit(6);
        wallet.bet(3)
              .bet(2)
              .bet(1);
        assertThat(wallet.isEmpty()).isTrue();
        assertThat(wallet.balance()).isZero();
    }
}
