package edu.hust.cardgame.strategy;

import edu.hust.cardgame.core.CardType;

public interface CardOrderingStrategy <C extends CardType> {
    int getFaceOrder(C card);
    int getSuitOrder(C card);
}
