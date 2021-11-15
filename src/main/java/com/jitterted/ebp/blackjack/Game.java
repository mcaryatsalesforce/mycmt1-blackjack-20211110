package com.jitterted.ebp.blackjack;

import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

import java.util.List;
import java.util.Scanner;

import static org.fusesource.jansi.Ansi.ansi;

public class Game {

    private final Deck deck;

    private final Hand dealerHand = new Hand("Dealer");
    private final Hand playerHand = new Hand("Player");
    private int playerBalance;
    private int playerBet;

    public static void main(String[] args) {
        ensureConsoleAttached();
        initDisplay();
        greetUser();
        int initialBalance = promptUserForBalance();
        playGame(initialBalance);

        resetDisplay();
    }

    private static void ensureConsoleAttached() {
        if (System.console() == null) {
            throw new IllegalStateException("must have a tty available (pipe or non-interactive session?");
        }
    }

    private static void initDisplay() {
        AnsiConsole.systemInstall();
    }

    private static void playGame(int balance) {
        try {
            Deck deck = new Deck();
            while (true) {
                Game game = new Game(deck, balance);
                game.playerBets(promptUserForBet());
                game.initialDeal();
                game.play();
                balance = game.playerBalance();
                if (!promptUserToContinue()) {
                    return;
                }
            }
        } catch (EmptyDeckException ex) {
            System.out.printf("Game over - %s.%n", ex.getMessage());
        }
    }

    private static void greetUser() {
        System.out.println(ansi()
                                   .bgBright(Ansi.Color.WHITE)
                                   .eraseScreen()
                                   .cursor(1, 1)
                                   .fgGreen().a("Welcome to")
                                   .fgRed().a(" JitterTed's")
                                   .fgBlack().a(" BlackJack game"));
    }

    private static int promptUserForBalance() {
        int initialBalance = 0;
        while (initialBalance <= 0) {
            System.out.print(ansi().cursor(3, 1));
            String value = inputFromPlayer("Please enter the total which you are willing to gamble:  ");
            try {
                initialBalance = Integer.parseInt(value.strip());
            } catch (NumberFormatException ex) {
                System.out.println(ansi()
                                           .fg(Ansi.Color.BLACK)
                                           .a("\n\n")
                                           .a(ex.getMessage()).a("; please enter a valid whole number."));
            }
        }
        return initialBalance;
    }

    private static int promptUserForBet() {
        int bet = 0;
        while (bet <= 0) {
            String value = inputFromPlayer("Please enter how much you want to bet [$2-$500]:  ");
            try {
                bet = Integer.parseInt(value.strip());
                if (bet < 2 || bet > 500) {
                    System.out.println(ansi()
                                               .fg(Ansi.Color.BLACK)
                                               .a("\n\n")
                                               .a("Please bet no less than $2 and no more than $500."));
                    bet = 0;
                }
            } catch (NumberFormatException ex) {
                System.out.println(ansi()
                                           .fg(Ansi.Color.BLACK)
                                           .a("\n\n")
                                           .a(ex.getMessage()).a("; please enter a valid whole number."));
            }
        }
        return bet;
    }

    private static boolean promptUserToContinue() {
        while (true) {
            String playerChoice = inputFromPlayer("Would you like to play again? [YN]").toLowerCase();
            if (playerContinues(playerChoice)) {
                return true;
            }
            if (playerFinished(playerChoice)) {
                return false;
            }
        }
    }

    private static void resetDisplay() {
        System.out.println(ansi().reset());
    }

    public Game(Deck deck, int initialBalance) {
        this.deck = deck;
        this.playerBalance = initialBalance;
        this.playerBet = 0;
    }

    Game() {
        this(new Deck(), 0);
    }

    public void initialDeal() {
        // deal first and second round of cards
        dealRoundToHands();
        dealRoundToHands();
    }

    private void dealRoundToHands() {
        // deal players first
        List.of(playerHand, dealerHand).forEach(this::dealToHand);
    }

    private void dealToHand(Hand hand) {
        hand.take(deck.draw());
    }

    public void play() { // long method (many many _many_ decisions)
        // get Player's decision: hit until they stand, then they're done (or they go bust)
        if (!dealerHand.blackjack()) {
            playerDraws();
        }
        if (!playerHand.blackjack() && !playerHand.busted()) {
            dealerDraws();
        }
        displayFinalGameState();
        displayGameResult();
    }

    private void dealerDraws() {
        // Dealer makes its choice automatically based on a simple heuristic (<=16, hit, 17>=stand)
        while (dealerMustDraw()) {
            dealToHand(dealerHand);
        }
    }

    private boolean dealerMustDraw() {
        return dealerHand.totalValue() <= 16;
    }

    private void playerDraws() {
        while (true) {
            displayGameState();
            boolean playerStands = playDeal();
            if (playerStands || playerHand.busted()) {
                return;
            }
        }
    }

    private boolean playDeal() {
        String playerChoice = inputFromPlayer("[H]it or [S]tand?").toLowerCase();
        if (playerStands(playerChoice)) {
            return true;
        }
        if (playerHits(playerChoice)) {
            dealToHand(playerHand);
            if (playerHand.busted()) {
                return false;
            }
        }
        remindUserOfCommands();
        return false;
    }

    private static void remindUserOfCommands() {
        System.out.println("You need to [H]it or [S]tand");
    }

    private static boolean playerContinues(String playerChoice) {
        return playerChoice.startsWith("y");
    }

    private static boolean playerFinished(String playerChoice) {
        return playerChoice.startsWith("n");
    }

    private static boolean playerHits(String playerChoice) {
        return playerChoice.startsWith("h");
    }

    private static boolean playerStands(String playerChoice) {
        return playerChoice.startsWith("s");
    }

    private static String inputFromPlayer(String prompt) {
        System.out.print(ansi().eraseLine().fgBrightBlack().a(prompt).a("  "));
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    private void displayGameState() {
        System.out.print(ansi().eraseScreen().cursor(1, 1));
        System.out.println("Dealer has: ");
        System.out.println(dealerHand.firstCard().display()); // first card is Face Up

        // second card is the hole card, which is hidden
        displayBackOfCard();

        System.out.printf("%n%n%n");
        playerHand.display();
        System.out.printf("%n%n%nWallet: $%6d  Current Bet: $%6d%n", playerBalance(), playerBet());
    }

    private void displayBackOfCard() {
        System.out.print(
                ansi()
                        .cursorUp(7)
                        .cursorRight(12)
                        .a("┌─────────┐").cursorDown(1).cursorLeft(11)
                        .a("│░░░░░░░░░│").cursorDown(1).cursorLeft(11)
                        .a("│░ J I T ░│").cursorDown(1).cursorLeft(11)
                        .a("│░ T E R ░│").cursorDown(1).cursorLeft(11)
                        .a("│░ T E D ░│").cursorDown(1).cursorLeft(11)
                        .a("│░░░░░░░░░│").cursorDown(1).cursorLeft(11)
                        .a("└─────────┘"));
    }

    private void displayFinalGameState() {
        System.out.print(ansi().eraseScreen().cursor(1, 1));
        dealerHand.display();

        System.out.println();
        playerHand.display();
    }

    private void displayGameResult() {
        if (playerHand.busted()) {
            System.out.println("\nYou Busted, so you lose.  💸");
            playerLosesBet();
        } else if (dealerHand.busted()) {
            System.out.println("\nDealer went BUST, Player wins! Yay for you!! 💵");
            playerWinsBet();
        } else {
            if (playerHand.blackjack()) {
                System.out.println("You hit Blackjack!");
                playerWinsBlackjack();
            }
            if (dealerHand.blackjack()) {
                System.out.println("Dealer hit Blackjack!");
                playerLosesBet();
            }
            if (playerHand.beats(dealerHand)) {
                System.out.println("You beat the Dealer! 💵");
                playerWinsBet();
            } else if (playerHand.pushes(dealerHand)) {
                System.out.println("Push: You tie with the Dealer. 💸");
                playerPushesBet();
            } else {
                System.out.println("You lost to the Dealer. 💸");
                playerLosesBet();
            }
        }
        System.out.printf("Wallet: $%6d%n", playerBalance());
    }

    public int playerBalance() {
        return playerBalance;
    }

    public int playerBet() {
        return playerBet;
    }

    public void playerDeposits(int amount) {
        playerBalance += amount;
    }

    public void playerBets(int amount) {
        playerBalance -= amount;
        playerBet += amount;
    }

    public void playerWinsBet() {
        payoffPlayer(2);
    }

    public void playerWinsBlackjack() {
        payoffPlayer(2.5);
    }

    public void playerLosesBet() {
        payoffPlayer(0);
    }

    public void playerPushesBet() {
        payoffPlayer(1);
    }

    private void payoffPlayer(double payoff) {
        playerBalance += playerBet * payoff;
        playerBet = 0;
    }
}
