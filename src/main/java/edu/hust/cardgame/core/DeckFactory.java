package main.java.edu.hust.cardgame.core;

import java.util.List;

public interface DeckFactory<C extends CardType> {
    List<C> createDeck();
}
