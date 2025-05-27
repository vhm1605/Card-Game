package main.java.edu.hust.cardgame.strategy;

import main.java.edu.hust.cardgame.core.StandardCard;

public class DefaultComparisonStrategy implements CardComparisonStrategy<StandardCard> {
    public final CardOrderingStrategy<StandardCard> order = new DefaultStandardCardOrderingStrategy();
    @Override
    public int compare(StandardCard firstCard, StandardCard secondCard) {
        int faceDiff = order.getFaceOrder(firstCard) - order.getFaceOrder(secondCard);
        if (faceDiff != 0) {
            return faceDiff;
        }
        return order.getSuitOrder(firstCard) - order.getSuitOrder(secondCard);
    }
}
