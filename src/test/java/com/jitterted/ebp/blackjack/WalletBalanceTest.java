package com.jitterted.ebp.blackjack;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class WalletBalanceTest {
    @Test
    public void newIsEmpty() {
        Wallet wallet = new Wallet();
        assertThat(wallet.isEmpty()).isTrue();
    }

    @Test
    public void holdsNothingInitially() {
        Wallet wallet = new Wallet();
        assertThat(wallet.balance()).isZero();
    }

    @Test
    public void creditPositiveThenNotIsEmpty() {
        Wallet wallet = new Wallet();
        wallet.credit(10);
        assertThat(wallet.isEmpty()).isFalse();
    }

    @Test
    public void creditPositiveThenReportsBalance() {
        Wallet wallet = new Wallet();
        wallet.credit(10);
        assertThat(wallet.balance()).isNotZero();
        assertThat(wallet.balance()).isEqualTo(10);
    }

    @Test
    public void creditNegativeThrowsException() {
        Wallet wallet = new Wallet();
        assertThatThrownBy(() -> wallet.credit(-3))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void creditZeroThrowsException() {
        Wallet wallet = new Wallet();
        assertThatThrownBy(() -> wallet.credit(0))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void debitPositiveThenReportsOwed() {
        Wallet wallet = new Wallet();
        wallet.debit(10);
        assertThat(wallet.balance()).isNotZero();
        assertThat(wallet.balance()).isEqualTo(-10);
    }

    @Test
    public void debitNegativeThrowsException() {
        Wallet wallet = new Wallet();
        assertThatThrownBy(() -> wallet.debit(-3))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void debitZeroThrowsException() {
        Wallet wallet = new Wallet();
        assertThatThrownBy(() -> wallet.debit(0))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void twoAddsThenReportsEveryAmountInBalance() {
        Wallet wallet = new Wallet();
        wallet.credit(10)
              .credit(3);
        assertThat(wallet.balance()).isNotZero();
        assertThat(wallet.balance()).isEqualTo(10 + 3);
    }

    @Test
    public void creditAndDebitThenReportsDifferenceOfAmountsInBalance() {
        Wallet wallet = new Wallet();
        wallet.credit(10)
              .debit(3);
        assertThat(wallet.balance()).isNotZero();
        assertThat(wallet.balance()).isEqualTo(10 - 3);
    }

    @Test
    public void creditAndDebitTwiceThenReportsDifferenceOfAmountsInBalance() {
        Wallet wallet = new Wallet();
        wallet.credit(10)
              .debit(3)
              .debit(2);
        assertThat(wallet.balance()).isNotZero();
        assertThat(wallet.balance()).isEqualTo(10 - 3 - 2);
    }

    @Test
    public void debitAndCreditThenReportsDifferenceOfAmountsInBalance() {
        Wallet wallet = new Wallet();
        wallet.debit(3)
              .credit(10);
        assertThat(wallet.balance()).isNotZero();
        assertThat(wallet.balance()).isEqualTo(-3 + 10);
    }
}
