package com.jitterted.ebp.blackjack;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class GameBetPayoffTest {
    @Test
    public void newGamePlayerBalanceIsZero() {
        Game game = new Game();
        assertThat(game.playerBalance())
                .isZero();
    }

    @Test
    public void playerDeposits25ThenBalanceIs25() {
        Game game = new Game();
        game.playerDeposits(25);
        assertThat(game.playerBalance())
                .isEqualTo(25);
    }

    @Test
    public void playerWith100Bets50ThenBalanceIs50() {
        Game game = new Game();
        game.playerDeposits(100);
        game.playerBets(50);
        assertThat(game.playerBalance())
                .isEqualTo(100 - 50);
    }

    @Test
    public void playerWith100BalanceBets50AndWinsThenBalanceIs150() {
        Game game = new Game();
        game.playerDeposits(100);
        game.playerBets(50);
        game.playerWinsBet();
        assertThat(game.playerBalance())
                .isEqualTo(100 - 50 + 2*50);
    }

    @Test
    public void playerWith100BalanceBets50AndBlackjacksThenBalanceIs175() {
        Game game = new Game();
        game.playerDeposits(100);
        game.playerBets(50);
        game.playerWinsBlackjack();
        int blackjackWinnings = (int)(2.5 * 50);
        int expected = (100 - 50) + blackjackWinnings;
        assertThat(game.playerBalance())
                .isEqualTo(expected);
    }

    @Test
    public void playerWith100BalanceTwiceBets50AndBlackjacksThenBalanceIs175() {
        Game game = new Game();
        game.playerDeposits(100);
        game.playerBets(50);
        game.playerWinsBlackjack();
        game.playerBets(50);
        game.playerWinsBlackjack();
        int blackjackWinnings = (int)(2.5 * 50);
        int expected = 100 - 50 - 50 + blackjackWinnings + blackjackWinnings;
        assertThat(game.playerBalance())
                .isEqualTo(expected);
    }

    @Test
    public void playerWith100BalanceBets25AndLosesThenBalanceIs75() {
        Game game = new Game();
        game.playerDeposits(100);
        game.playerBets(25);
        game.playerLosesBet();
        assertThat(game.playerBalance())
                .isEqualTo(100 - 25);
    }

    @Test
    public void playerWith100BalanceTwiceBets25AndLosesThenBalanceIs50() {
        Game game = new Game();
        game.playerDeposits(100);
        game.playerBets(25);
        game.playerLosesBet();
        game.playerBets(25);
        game.playerLosesBet();
        assertThat(game.playerBalance())
                .isEqualTo(100 - 25 - 25);
    }

    @Test
    public void playerWith100BalanceBets50AndPushesThenBalanceIs100() {
        Game game = new Game();
        game.playerDeposits(100);
        game.playerBets(50);
        game.playerPushesBet();
        assertThat(game.playerBalance())
                .isEqualTo(100 - 50 + 50);
    }

    @Test
    public void playerWith100BalanceTwiceBets50AndPushesThenBalanceIs100() {
        Game game = new Game();
        game.playerDeposits(100);
        game.playerBets(50);
        game.playerPushesBet();
        game.playerBets(50);
        game.playerPushesBet();
        assertThat(game.playerBalance())
                .isEqualTo(100 - 50 + 50 - 50 + 50);
    }
}
