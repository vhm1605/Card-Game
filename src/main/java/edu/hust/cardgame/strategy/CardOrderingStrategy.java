package main.java.edu.hust.cardgame.strategy;

import main.java.edu.hust.cardgame.model.CardType;

public interface CardOrderingStrategy <C extends CardType> {
    int getFaceOrder(C card);
    int getSuitOrder(C card);
}
