package module;

public class BaCayScoreStrategy implements ScoreStrategy<StandardCard> {
    @Override
    public int computeScore(CardCollection<StandardCard> hand) {
        int sum = 0;
        for (StandardCard card : hand.getAllCards()) {
            sum += DefaultCardOrderingStrategy.getFaceOrder(card);
        }
        return sum % 10;
    }
}
