package com.jitterted.ebp.blackjack;

import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import static org.fusesource.jansi.Ansi.ansi;

public class Game {

    private final Deck deck;

    private final Hand dealerHand = new Hand();
    private final Hand playerHand = new Hand();

    public static void main(String[] args) {
        ensureConsoleAttached();
        initDisplay();
        greetUser();
        promptUserToStart();
        playGame();

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

    private static void playGame() {
        Game game = new Game();
        game.initialDeal();
        game.play();
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

    private static void promptUserToStart() {
        System.out.println(ansi()
                                   .cursor(3, 1)
                                   .fgBrightBlack().a("Hit [ENTER] to start..."));

        System.console().readLine();
    }

    private static void resetDisplay() {
        System.out.println(ansi().reset());
    }

    public Game() {
        deck = new Deck();
    }

    public void initialDeal() {
        // deal first and second round of cards
        dealRoundToHands();
        dealRoundToHands();
    }

    private void dealRoundToHands() {
        // deal players first
        for (final Hand hand: List.of(playerHand, dealerHand)) {
            dealToHand(hand);
        }
    }

    private void dealToHand(Hand hand) {
        hand.take(deck.draw());
    }

    public void play() { // long method (many many _many_ decisions)
        // get Player's decision: hit until they stand, then they're done (or they go bust)
        if (!dealerHand.blackjack()) {
            playerDraws();
            dealerDraws();
        }
        displayFinalGameState();
        displayGameResult();
    }

    private void dealerDraws() {
        // Dealer makes its choice automatically based on a simple heuristic (<=16, hit, 17>=stand)
        if (!playerHand.busted()) {
            while (dealerMustDraw()) {
                dealToHand(dealerHand);
            }
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
        String playerChoice = inputFromPlayer().toLowerCase();
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

    private void remindUserOfCommands() {
        System.out.println("You need to [H]it or [S]tand");
    }

    private boolean playerStands(String playerChoice) {
        return playerChoice.startsWith("s");
    }

    private boolean playerHits(String playerChoice) {
        return playerChoice.startsWith("h");
    }

    private String inputFromPlayer() {
        System.out.println("[H]it or [S]tand?");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    private void displayGameState() {
        System.out.print(ansi().eraseScreen().cursor(1, 1));
        System.out.println("Dealer has: ");
        System.out.println(dealerHand.firstCard().display()); // first card is Face Up

        // second card is the hole card, which is hidden
        displayBackOfCard();

        System.out.println();
        displayHand("Player", playerHand);
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

    private void displayCards(Hand hand) {
        System.out.println(hand.cards().stream()
                               .map(Card::display)
                               .collect(Collectors.joining(
                                       ansi().cursorUp(6).cursorRight(1).toString())));
    }

    private void displayFinalGameState() {
        System.out.print(ansi().eraseScreen().cursor(1, 1));
        displayHand("Dealer ", dealerHand);

        System.out.println();
        displayHand("Player", playerHand);
    }

    private void displayHand(String owner, Hand hand) {
        System.out.printf("%s has: %n", owner);
        displayCards(hand);
        System.out.println(" (" + hand.totalValue() + ")");
    }

    private void displayGameResult() {
        if (playerHand.busted()) {
            System.out.println("You Busted, so you lose.  💸");
            return;
        } else if (dealerHand.busted()) {
            System.out.println("Dealer went BUST, Player wins! Yay for you!! 💵");
            return;
        }
        if (playerHand.blackjack()) {
            System.out.println("You hit Blackjack!");
        }
        if (dealerHand.blackjack()) {
            System.out.println("Dealer hit Blackjack!");
        }
        if (dealerHand.totalValue() < playerHand.totalValue()) {
            System.out.println("You beat the Dealer! 💵");
        } else if (dealerHand.totalValue() == playerHand.totalValue()) {
            System.out.println("Push: You tie with the Dealer. 💸");
        } else {
            System.out.println("You lost to the Dealer. 💸");
        }
    }
}
