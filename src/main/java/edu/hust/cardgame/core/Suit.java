package main.java.edu.hust.cardgame.core;

public enum Suit {
    HEARTS("♥"), DIAMONDS("♦"), CLUBS("♣"), SPADES("♠");

    private final String suitSymbol;

    Suit(String suitSymbol) {
        this.suitSymbol = suitSymbol;
    }

    public String getSuitSymbol() {
        return suitSymbol;
    }
}
