package module;

public class BaCayScoreStrategy implements ScoreStrategy {
    @Override
    public int computeScore(CardCollection hand) {
        int sum = 0;
        for (Card card : hand.getAllCards()) {
            sum += DefaultCardOrderingStrategy.getFaceOrder(card);
        }
        return sum % 10;
    }
}
