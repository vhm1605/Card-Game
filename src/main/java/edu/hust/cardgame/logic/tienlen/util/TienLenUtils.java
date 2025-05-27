package main.java.edu.hust.cardgame.logic.tienlen.util;

import main.java.edu.hust.cardgame.strategy.CardOrderingStrategy;
import main.java.edu.hust.cardgame.core.StandardCard;
import main.java.edu.hust.cardgame.strategy.DefaultStandardCardOrderingStrategy;

public class TienLenUtils {
    public static final CardOrderingStrategy<StandardCard> order = new DefaultStandardCardOrderingStrategy();
    public static boolean isMatchingPair(StandardCard firstCard, StandardCard secondCard) {
        return order.getFaceOrder(firstCard) == order.getFaceOrder(secondCard);
    }

    public static int getColorGroup(StandardCard card) {
        int suitOrder = order.getSuitOrder(card);
        return (suitOrder - 1) / 2;
    }
}
