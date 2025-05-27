package main.java.edu.hust.cardgame.strategy;

import main.java.edu.hust.cardgame.core.CardCollection;
import main.java.edu.hust.cardgame.core.StandardCard;

public class BaCayScoreStrategy implements ScoreStrategy<StandardCard> {
    public final CardOrderingStrategy<StandardCard> order = new DefaultStandardCardOrderingStrategy();
    @Override
    public int computeScore(CardCollection<StandardCard> hand) {
        int score = 0;
        for (StandardCard card : hand.getAllCards()) {
            score += order.getFaceOrder(card);
        }
        return score % 10;
    }
}
