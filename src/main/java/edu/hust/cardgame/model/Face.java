package main.java.edu.hust.cardgame.model;

public enum Face {
    ACE("A"), TWO("2"), THREE("3"), FOUR("4"), FIVE("5"), SIX("6"), SEVEN("7"), EIGHT("8"), NINE("9"), TEN("10"), JACK("J"), QUEEN("Q"), KING("K");

    private final String shortLabel;

    Face(String shortLabel) {
        this.shortLabel = shortLabel;
    }

    public String getShortLabel() {
        return shortLabel;
    }
}
