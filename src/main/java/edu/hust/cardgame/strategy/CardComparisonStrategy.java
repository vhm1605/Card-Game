package main.java.edu.hust.cardgame.strategy;

import main.java.edu.hust.cardgame.model.CardType;

public interface CardComparisonStrategy<C extends CardType> {
    int compare(C firstCard, C secondCard);
}
