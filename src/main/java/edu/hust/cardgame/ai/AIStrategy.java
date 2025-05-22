package main.java.edu.hust.cardgame.ai;

import main.java.edu.hust.cardgame.model.CardCollection;
import main.java.edu.hust.cardgame.model.CardType;
import main.java.edu.hust.cardgame.model.Player;
import main.java.edu.hust.cardgame.core.GeneralGame;

public interface AIStrategy<C extends CardType, G extends GeneralGame<C>> {
    CardCollection<C> decideMove(G game, Player<C> ai);
}
