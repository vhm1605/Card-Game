package main.java.edu.hust.cardgame.strategy;

import main.java.edu.hust.cardgame.model.StandardCard;

public class TienLenCardOrderingStrategy implements CardOrderingStrategy<StandardCard> {
    private final DefaultStandardCardOrderingStrategy base = new DefaultStandardCardOrderingStrategy();

    @Override
    public int getFaceOrder(StandardCard card) {
        return switch (card.getFirst()) {
            case ACE -> 14;
            case TWO -> 15;
            default -> base.getFaceOrder(card);
        };
    }

    @Override
    public int getSuitOrder(StandardCard card) {
        return base.getSuitOrder(card);
    }
}
