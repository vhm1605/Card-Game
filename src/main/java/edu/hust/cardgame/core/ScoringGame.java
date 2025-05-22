package main.java.edu.hust.cardgame.core;
import main.java.edu.hust.cardgame.model.CardType;
import main.java.edu.hust.cardgame.model.Player;

import java.util.List;

public interface ScoringGame<C extends CardType> extends GeneralGame<C> {
    void deal();
    int getScoreFor(Player<C> p);
    List<Integer> getRanking();
}
