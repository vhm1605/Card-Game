package edu.hust.cardgame.logic.tienlen;

import edu.hust.cardgame.strategy.CardOrderingStrategy;
import edu.hust.cardgame.core.StandardCard;
import edu.hust.cardgame.strategy.DefaultStandardCardOrderingStrategy;

public final class TienLenUtils {
    private static final CardOrderingStrategy<StandardCard> ORDER = new DefaultStandardCardOrderingStrategy();

    private TienLenUtils() {
    }

    public static boolean isMatchingPair(StandardCard firstCard, StandardCard secondCard) {
        return ORDER.getFaceOrder(firstCard) == ORDER.getFaceOrder(secondCard);
    }

    public static int getColorGroup(StandardCard card) {
        int suitOrder = ORDER.getSuitOrder(card);
        return (suitOrder - 1) / 2;
    }
}
