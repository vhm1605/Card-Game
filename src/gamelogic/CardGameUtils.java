package gamelogic;

import module.CardOrderingStrategy;
import module.StandardCard;
import module.DefaultStandardCardOrderingStrategy;
import module.TienLenCardOrderingStrategy;

public class CardGameUtils {
    public static final CardOrderingStrategy<StandardCard> order = new DefaultStandardCardOrderingStrategy();
    public static boolean isMatchingPair(StandardCard firstCard, StandardCard secondCard) {
        return order.getFaceOrder(firstCard) == order.getFaceOrder(secondCard);
    }

    public static int getColorGroup(StandardCard card) {
        int suitOrder = order.getSuitOrder(card);
        return (suitOrder - 1) / 2;
    }
}
