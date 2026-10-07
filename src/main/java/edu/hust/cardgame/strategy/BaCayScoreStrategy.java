package edu.hust.cardgame.strategy;

import edu.hust.cardgame.core.CardCollection;
import edu.hust.cardgame.core.StandardCard;

public class BaCayScoreStrategy implements ScoreStrategy<StandardCard> {
    private final CardOrderingStrategy<StandardCard> order = new DefaultStandardCardOrderingStrategy();
    @Override
    public int computeScore(CardCollection<StandardCard> hand) {
        int score = 0;
        for (StandardCard card : hand.getAllCards()) {
            score += order.getFaceOrder(card);
        }
        return score % 10;
    }
}
