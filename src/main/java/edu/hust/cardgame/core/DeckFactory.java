package main.java.edu.hust.cardgame.core;

import main.java.edu.hust.cardgame.model.CardType;

import java.util.List;

public interface DeckFactory<C extends CardType> {
    List<C> createDeck();
}
