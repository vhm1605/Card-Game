package main.java.edu.hust.cardgame.core;

import main.java.edu.hust.cardgame.model.CardCollection;
import main.java.edu.hust.cardgame.model.CardType;
import main.java.edu.hust.cardgame.model.Player;

public interface GeneralGame<C extends CardType> {
    CardCollection<C> getSelectedCards();
    boolean isValidPlay();
    void playGame();
    void passTurn();
    CardCollection<C> getHandOf(Player<C> p);
}
