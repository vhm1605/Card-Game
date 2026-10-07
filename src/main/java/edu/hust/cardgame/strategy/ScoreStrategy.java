package edu.hust.cardgame.strategy;

import edu.hust.cardgame.core.CardCollection;
import edu.hust.cardgame.core.CardType;

public interface ScoreStrategy<C extends CardType> {
    int computeScore(CardCollection<C> hand);
}
