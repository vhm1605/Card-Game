package main.java.edu.hust.cardgame.core;
import main.java.edu.hust.cardgame.model.CardType;
import main.java.edu.hust.cardgame.model.Player;

import java.util.List;

public interface ScoringGame<C extends CardType> {
    int getScoreFor(Player<C> p);
}
