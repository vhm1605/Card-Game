package main.java.edu.hust.cardgame.core;

import java.util.List;

public interface ScoringGame<C extends CardType> {
    int getScoreFor(Player<C> p);
    List<Integer> getRanking();
}
