package gamelogic;

import module.Card;
import module.DefaultCardOrderingStrategy;

public class CardGameUtils {
    public static boolean isMatchingPair(Card firstCard, Card secondCard) {
        return DefaultCardOrderingStrategy.getFaceOrder(firstCard) == DefaultCardOrderingStrategy.getFaceOrder(secondCard);
    }
}
