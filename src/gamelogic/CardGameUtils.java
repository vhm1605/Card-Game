package gamelogic;

import module.StandardCard;
import module.DefaultCardOrderingStrategy;

public class CardGameUtils {
    public static boolean isMatchingPair(StandardCard firstCard, StandardCard secondCard) {
        return DefaultCardOrderingStrategy.getFaceOrder(firstCard) == DefaultCardOrderingStrategy.getFaceOrder(secondCard);
    }

    public static int getColorGroup(StandardCard card) {
        return (DefaultCardOrderingStrategy.getSuitOrder(card) - 1) / 2;
    }
}
