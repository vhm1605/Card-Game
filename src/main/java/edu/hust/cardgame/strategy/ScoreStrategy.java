package main.java.edu.hust.cardgame.strategy;

import main.java.edu.hust.cardgame.core.CardCollection;
import main.java.edu.hust.cardgame.core.CardType;

public interface ScoreStrategy<C extends CardType> {
    int computeScore(CardCollection<C> hand);
}
