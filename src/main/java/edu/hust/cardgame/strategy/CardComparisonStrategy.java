package edu.hust.cardgame.strategy;

import edu.hust.cardgame.core.CardType;

public interface CardComparisonStrategy<C extends CardType> {
    int compare(C firstCard, C secondCard);
}
