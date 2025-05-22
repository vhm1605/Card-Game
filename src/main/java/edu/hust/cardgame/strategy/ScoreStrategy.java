package main.java.edu.hust.cardgame.strategy;

import main.java.edu.hust.cardgame.model.CardCollection;
import main.java.edu.hust.cardgame.model.CardType;

public interface ScoreStrategy<C extends CardType> {
    int computeScore(CardCollection<C> hand);
}
